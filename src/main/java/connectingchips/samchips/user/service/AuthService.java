package connectingchips.samchips.user.service;

import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.global.email.EmailSender;
import connectingchips.samchips.global.email.dto.EmailRequestDto;
import connectingchips.samchips.global.exception.RestApiException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.global.utils.RedisUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static connectingchips.samchips.global.exception.AuthErrorCode.COOL_TIME_SEND_EMAIL;
import static connectingchips.samchips.global.exception.AuthErrorCode.INVALID_REFRESH_TOKEN;
import static connectingchips.samchips.global.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "RT-";
    private static final String EMAIL_AUTH_KEY_PREFIX = "EmailAuth-";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    @Value("${spring.mail.send-cool-time-millis}")
    private long sendCoolTimeMillis;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final RedisUtils redisUtils;
    private final S3Uploader s3Uploader;

    /* 자체 로그인 */
    @Transactional
    public AuthResponseDto.Token login(UserRequestDto.Login loginDto){
        // 입력한 accountId와 password로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getAccountId(), loginDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService의 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // authentication 객체로 token 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        redisUtils.setData(REFRESH_TOKEN_KEY_PREFIX + loginDto.getAccountId(), refreshToken, tokenProvider.refreshTokenExpirationMillis);

        return new AuthResponseDto.Token(accessToken, refreshToken);
    }

    /* 소셜 로그인 */
    @Transactional
    public AuthResponseDto.Token authenticateSocial(User user){
        User getUser = userRepository.findByAccountId(user.getAccountId()).orElse(null);

        if(getUser == null){
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            User createUser = User.builder()
                    .accountId(user.getAccountId())
                    .password(encodedPassword)
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .profileImage(randomDefaultProfileImage())
                    .gender(user.getGender())
                    .ageRange(user.getAgeRange())
                    .socialType(user.getSocialType())
                    .build();

            getUser = userRepository.save(createUser);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getAccountId(), user.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        redisUtils.setData(REFRESH_TOKEN_KEY_PREFIX + getUser.getAccountId(), refreshToken, tokenProvider.refreshTokenExpirationMillis);

        return new AuthResponseDto.Token(accessToken, refreshToken);
    }

    /* 인증 이메일 보내기 */
    @Transactional
    public void sendAuthenticationEmail(EmailRequestDto.Authentication authenticationDto) throws MessagingException {
        String toEmail = authenticationDto.getToEmail();;
        String emailKey = EMAIL_AUTH_KEY_PREFIX + toEmail;

        // 재전송 대기 시간 검사
        if(!checkSendEmailCoolTime(emailKey)){
            throw new RestApiException(COOL_TIME_SEND_EMAIL);
        }

        String subject = "작심삼칩 인증 메일";
        String template = "authEmail";
        String authCode = createAuthCode();
        HashMap<String, String> values = new HashMap<>();

        // 템플릿 값 설정
        values.put("email", toEmail);
        values.put("authCode", authCode);

        emailSender.sendTemplateEmail(toEmail, subject, template, values);
        redisUtils.setData(emailKey, authCode, authCodeExpirationMillis);
    }

    // 이메일 인증
    @Transactional
    public boolean authenticationEmail(String email, String authCode){
        String key = EMAIL_AUTH_KEY_PREFIX + email;
        Optional<Object> savedAuthCode = redisUtils.getData(key);

        // 인증 데이터 확인
        if(savedAuthCode.isPresent()) {
            return checkAuthCode(key, savedAuthCode.get().toString(), authCode);
        }
        return false;
    }

    /* 이메일 인증했는지 검증 */
    @Transactional
    public boolean verificationEmail(String email){
        String key = EMAIL_AUTH_KEY_PREFIX + email;
        Optional<Object> savedAuthCode = redisUtils.getData(key);

        // 인증 데이터가 존재하고 사용자가 인증 완료 버튼을 눌렀다면 true 반환
        if(savedAuthCode.isPresent()){
            String[] savedAuthInfos = savedAuthCode.get().toString().split("_");
            if(savedAuthInfos[0].equals("true")){
                redisUtils.deleteData(key);
                return true;
            }
        }
        return false;
    }

    /* 로그아웃 */
    @Transactional
    public void logout(Long userId, HttpServletRequest request){
        String accessToken = tokenProvider.resolveToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(NOT_FOUND_USER));
        Long expiredTime = tokenProvider.getExpiredTime(accessToken);

        // 현재 발급되어 있는 AccessToken을 사용하지 못하게 BlackList를 위해 Redis에 저장
        redisUtils.setData(accessToken, "logout", expiredTime);
        // 로그아웃 시, Redis의 RefreshToken 데이터 삭제
        redisUtils.deleteData(REFRESH_TOKEN_KEY_PREFIX + user.getAccountId());
    }

    /* accessToken 재발급 */
    @Transactional(readOnly = true)
    public AuthResponseDto.AccessToken reissueAccessToken(String refreshToken){
        // 리프레시 토큰 검증
        tokenProvider.validateToken(refreshToken);

        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        // 해당 User가 존재하는지 체크
        User getUser = userRepository.findByAccountId(authentication.getName())
                .orElseThrow(() -> new RestApiException(NOT_FOUND_USER));

        // refreshToken이 유효한지 체크
        String getRefreshToken = redisUtils.getData(REFRESH_TOKEN_KEY_PREFIX + getUser.getAccountId())
                .orElseThrow(() -> new RestApiException(INVALID_REFRESH_TOKEN))
                .toString();

        if(!getRefreshToken.equals(refreshToken))
            throw new RestApiException(INVALID_REFRESH_TOKEN);

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createAccessToken(authentication);

        return new AuthResponseDto.AccessToken(accessToken);
    }

    /**
     *  인증 코드 검사
     *  Redis에 저장되어 있는 authCode와 매개변수로 넘어온 authCode를 비교
     *  동일하면 value - "true", 만료 시간을 계산하여 다시 저장
      */
    private boolean checkAuthCode(String key, String savedAuthCode, String authCode){
        String[] savedAuthInfos = savedAuthCode.split("_");
        String code = authCode.split("_")[0];

        // 인증번호 비교
        if(savedAuthInfos[0].equals(code)){
            // 이메일 전송할 때의 시간과 현재 시간의 차이를 계산하여 데이터 만료 시간으로 설정
            long betweenToMillis = betweenToMillis(LocalDateTime.parse(savedAuthInfos[1]), LocalDateTime.now());
            long expiredTime = authCodeExpirationMillis - betweenToMillis;
            expiredTime = expiredTime > 0 ? expiredTime : 0;

            // 같은 key로 데이터를 삽입하여 덮어쓰기
            redisUtils.setData(key, "true_" + savedAuthInfos[1], expiredTime);
            return true;
        }
        return false;
    }

    /* 이메일 재전송 대기 시간 검사 */
    private boolean checkSendEmailCoolTime(String emailKey){
        Optional<Object> savedAuthCode = redisUtils.getData(emailKey);
        
        // 해당 이메일에 대한 데이터가 redis에 저장되어 있다면 시간 비교
        if(savedAuthCode.isPresent()){
            String[] savedAuthInfos = savedAuthCode.get().toString().split("_");
            long betweenToMillis = betweenToMillis(LocalDateTime.parse(savedAuthInfos[1]), LocalDateTime.now());

            // 최근 이메일을 전송한 시간과 현재 시간의 차이 값을 coolTime과 비교
            if(betweenToMillis <= sendCoolTimeMillis)
                return false;
        }

        // redis에 저장되어있지 않거나 coolTime
        return true;
    }

    /* 6자리의 인증번호와 현재 시간을 더한 AuthCode 생성 */
    private String createAuthCode(){
        Random random = new Random();
        StringBuffer number = new StringBuffer();

        for(int i = 0; i < 6; i++){
            // 10 미만의 랜덤 정수
            int randomNum = random.nextInt(10);
            number.append(randomNum);
        }
        
        number.append("_" + LocalDateTime.now());
        return number.toString();
    }

    /* 두 시간의 차이를 Millis로 반환 */
    private long betweenToMillis(LocalDateTime a, LocalDateTime b){
        return Duration.between(a, b).toMillis();
    }

    // 순환 참조 문제가 발생하여 임시로 AuthService에도 randomDefaultProfileImage 메서드 추가
    private String randomDefaultProfileImage(){
        List<String> fileNames = s3Uploader.find("profileImage/default/");

        if(fileNames.isEmpty()){
            return "";
        }else{
            if(fileNames.size() == 1){
                return "";
            }else{
                fileNames.remove(0);
            }
        }

        Random random = new Random();
        int randomIdx = random.nextInt(fileNames.size());

        return s3Uploader.getFileUrl(fileNames.get(randomIdx));
    }
}

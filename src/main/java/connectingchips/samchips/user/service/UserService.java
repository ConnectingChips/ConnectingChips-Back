package connectingchips.samchips.user.service;

import connectingchips.samchips.board.S3Uploader;
import connectingchips.samchips.exception.RestApiException;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@CacheConfig(cacheNames = "users")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    /* 유저 회원가입 */
    @Transactional
    public void signup(UserRequestDto.Signup signupDto){
        if(userRepository.existsByAccountId(signupDto.getAccountId())){
            throw new RestApiException(ALREADY_JOIN_MEMBERSHIP);
        }

        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        User user = User.builder()
                .accountId(signupDto.getAccountId())
                .password(encodedPassword)
                .nickname(signupDto.getNickname())
                .email(signupDto.getEmail())
                .profileImage(randomDefaultProfileImage())
                .socialType(SocialType.SAMCHIPS)
                .build();

        userRepository.save(user);
    }

    /* accountId로 유저 반환 */
    @Transactional(readOnly = true)
    @Cacheable(key = "#accountId", unless = "#result == null")
    public User getByAccountId(String accountId){
        User getUser = userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RestApiException(NOT_FOUND_USER));

        return getUser;
    }

    /* 아이디 사용 가능 여부 반환 */
    @Transactional(readOnly = true)
    public boolean checkAccountId(String accountId){
        return userRepository.existsByAccountId(accountId);
    }

    /* 유저 정보 수정 */
    @Transactional
    @CachePut(key = "#accountId")
    public User updateInfo(String accountId, UserRequestDto.Update updateDto){
        User getUser = getByAccountId(accountId);

        getUser.updateInfo(updateDto.getNickname());
        return getUser;
    }

    /* 유저 탈퇴 */
    @Transactional
    public void deleteByUserId(Long userId){
        if(!userRepository.existsById(userId)){
            throw new RestApiException(NOT_FOUND_USER);
        }

        userRepository.deleteById(userId);
    }

    /* 랜덤 기본 프로필 이미지 반환 */
    public String randomDefaultProfileImage(){
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

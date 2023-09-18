package connectingchips.samchips.user.service;

import connectingchips.samchips.exception.RestApiException;
import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                .socialType(SocialType.SAMCHIPS)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Info findByUserId(Long userId){
        UserResponseDto.Info findInfo = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(NOT_FOUND_USER));

        return findInfo;
    }

    @Transactional(readOnly = true)
    public boolean checkAccountId(String accountId){
        return userRepository.existsByAccountId(accountId);
    }

    @Transactional
    public void editInfo(Long userId, UserRequestDto.Edit editDto){
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(NOT_FOUND_USER));

        findUser.editInfo(editDto.getNickname());
    }

    @Transactional
    public void deleteByUserId(Long userId){
        if(!userRepository.existsById(userId)){
            throw new RestApiException(NOT_FOUND_USER);
        }

        userRepository.deleteById(userId);
    }
}

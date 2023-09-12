package connectingchips.samchips.user.service;

import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.dto.UserResponseDto;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(UserRequestDto.Signup signupDto){
        if(userRepository.existsByAccountId(signupDto.getAccountId())){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        userRepository.save(signupDto.toEntity());
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Info findByUserId(Long userId){
        UserResponseDto.Info findInfo = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        return findInfo;
    }

    @Transactional
    public void editInfo(Long userId, UserRequestDto.Edit editDto){
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        findUser.editInfo(editDto.getNickname());
    }

    @Transactional
    public void deleteByUserId(Long userId){
        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        userRepository.deleteById(userId);
    }
}
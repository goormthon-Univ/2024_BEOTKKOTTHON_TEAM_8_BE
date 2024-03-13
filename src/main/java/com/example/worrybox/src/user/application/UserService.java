package com.example.worrybox.src.user.application;

import com.example.worrybox.src.user.api.dto.request.PostJoinReq;
import com.example.worrybox.src.user.domain.User;
import com.example.worrybox.src.user.domain.repository.UserRepository;
import com.example.worrybox.utils.config.BaseException;
import com.example.worrybox.utils.config.BaseResponseStatus;
import com.example.worrybox.utils.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    /* Sign up with kakao API */
    @Transactional
    public Long join(PostJoinReq userReq) throws BaseException {
        Optional<User> userByName = userRepository.findByNameAndStatus(userReq.getName(), Status.A);
        if(userByName.isPresent()) {  // 해당 이름이 있는 경우
            User existingUser = userByName.get();
            // 비밀번호가 일치하는지 확인
            if(userReq.getPassword() == existingUser.getPassword()) {  // 일치하면 로그인
                return existingUser.getId();
            } else {  // 일치하지 않으면 중복 이름 에러 발생
                throw new BaseException(BaseResponseStatus.JOIN_INVALID_NAME);
            }
        } else {  // 해당 이름이 없는 경우 -> 새로 회원가입 진행
            User newUser = userRepository.save(User.of(userReq));
            return newUser.getId();  // UserId 반환
        }
    }
}

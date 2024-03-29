package com.example.worrybox.src.letter.application;

import com.example.worrybox.src.letter.api.dto.request.PostLetterReq;
import com.example.worrybox.src.letter.api.dto.response.GetLettersRes;
import com.example.worrybox.src.letter.domain.Letter;
import com.example.worrybox.src.letter.domain.repository.GetLetterId;
import com.example.worrybox.src.letter.domain.repository.LetterRepository;
import com.example.worrybox.src.user.domain.User;
import com.example.worrybox.src.user.domain.repository.UserRepository;
import com.example.worrybox.utils.config.BaseException;
import com.example.worrybox.utils.config.BaseResponseStatus;
import com.example.worrybox.utils.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {
    private final UserRepository userRepository;
    private final LetterRepository letterRepository;

    public Boolean checkLetter(Long userId) throws BaseException {
        Optional<User> userById = userRepository.findByIdAndStatus(userId, Status.A);
        if(userById.isEmpty()) {  // 존재하지 않는 유저 에러 발생
            throw new BaseException(BaseResponseStatus.BASE_INVALID_USER);
        }

        String today = getDate();
        System.out.println(today);
        List<GetLetterId> letter = letterRepository.findTodayLetters(userId, today);

        return !letter.isEmpty();
    }

    @Transactional
    public Long sendLetter(Long userId, PostLetterReq letterReq) throws BaseException, ParseException {
        // 해당 아이디 가진 유저가 존재하는지 검사
        User userById = userRepository.findByIdAndStatus(userId, Status.A)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BASE_INVALID_USER));

        String letter = letterReq.getLetter(), arrivalTime = letterReq.getArrivalDate();
        Letter newLetter = letterRepository.save(Letter.of(userById, letter, arrivalTime));

        return newLetter.getId();
    }

    public List<GetLettersRes> getLetter(Long userId) throws BaseException {
        // 해당 아이디 가진 유저가 존재하는지 검사
        userRepository.findByIdAndStatus(userId, Status.A)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.BASE_INVALID_USER));

        String today = getDate();
//        System.out.println(today);

        return letterRepository.findAllLetters(userId, today);
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();

        String date = formatter.format(today);
        System.out.println(date);

        return date;
    }
}

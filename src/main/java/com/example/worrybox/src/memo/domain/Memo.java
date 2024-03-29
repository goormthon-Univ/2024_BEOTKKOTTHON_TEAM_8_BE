package com.example.worrybox.src.memo.domain;

import com.example.worrybox.src.memo.api.dto.request.MemoRequestDto;
import com.example.worrybox.src.memo.api.dto.request.SolutionRequestDto;
import com.example.worrybox.src.user.domain.User;
import com.example.worrybox.utils.entity.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Memo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @CreatedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private String worryText;

    @Nullable
    private String solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 정적 팩토링 메서드
    public static Memo of(User user,MemoRequestDto memoRequestDto){
        return Memo.builder()
                .worryText(memoRequestDto.getWorryText())
                .solution("아직 입력된 해결책이 없습니다.")
                .status(Status.A)
                .user(user)
                .build();
    }
    public void updateStatus(Status status) {
        this.status = status;
    }

    // 솔루션 작성 메서드
    public void update(SolutionRequestDto solutionRequestDto){
        this.solution = solutionRequestDto.getSolution();
    }

    // updatedAt 필드 업데이트를 위한 메서드
    public void updateTime(Timestamp newUpdateTime) {
        this.updatedAt = newUpdateTime;
    }
}

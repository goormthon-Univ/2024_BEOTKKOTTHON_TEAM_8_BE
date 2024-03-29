package com.example.worrybox.src.user.domain;

import com.example.worrybox.src.cheering.domain.CheeringMessage;
import com.example.worrybox.src.memo.domain.Memo;
import com.example.worrybox.src.user.api.dto.request.PostJoinReq;
import com.example.worrybox.utils.entity.BaseEntity;
import com.example.worrybox.utils.entity.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Users")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    private String name;
    @NotNull
    private int password;

    private String worryStartTime;
    private String worryEndTime;
    private String FCMToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Memo> Memos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CheeringMessage> cheeringMessages = new ArrayList<>();

    public static User of(PostJoinReq postJoinReq) {
        return User.builder()
                .name(postJoinReq.getName())
                .password(postJoinReq.getPassword())
                .worryStartTime(postJoinReq.getStartTime())
                .worryEndTime(postJoinReq.getEndTime())
                .status(Status.A)
                .build();
    }
}

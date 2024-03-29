package com.example.worrybox.src.notice.api;

import com.example.worrybox.src.notice.api.dto.request.PostTokenReq;
import com.example.worrybox.src.notice.application.FCMService;
import com.example.worrybox.utils.config.BaseException;
import com.example.worrybox.utils.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class FCMController {
    private final FCMService FCMService;

    /* FCM Token 서버 저장 API */
    @Operation(summary = "FCM Token 전달", description="유저의 FCM Token을 전달합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "400", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "400", description = "입력값이 잘못되었습니다."),
            @ApiResponse(responseCode = "4000", description = "존재하지 않는 유저입니다."),
    })
    @PostMapping("/{userId}/token")
    public BaseResponse<String> getToken(@PathVariable Long userId, @Valid @RequestBody PostTokenReq postTokenReq) {
        try {
            return new BaseResponse<>(FCMService.getToken(userId, postTokenReq.getToken()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}

package com.example.gdsc.util.exception;

import com.example.gdsc.util.api.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CustomException은 서버에서 발생하는 예외를 처리하는 클래스입니다.
 * Service에서 예외가 발생했을 때, 어떤 예외가 왜 발생했는지를 클라이언트에게 알려주는 것이 좋습니다.
 * 이를 위해 CustomException은 ResponseCode를 필드로 가지고 있어, 이를 통해 클라이언트에게 어떤 예외가 발생했는지 알려줄 수 있습니다.
 */
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{

    private final ResponseCode responseCode;

    @Override
    public String getMessage() {
        return responseCode.getMessage();
    }
}

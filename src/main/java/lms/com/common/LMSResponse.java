package lms.com.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LMSResponse implements Serializable {

    public String msg;
    public Object data;
    public Integer statusCode;
    private long timestamp;

    public static LMSResponse fail(String errorMsg) {
        return LMSResponse.builder()
                .statusCode(Constant.FAILURE_CODE)
                .msg(errorMsg)
                .data("*****")
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
    public static LMSResponse success(String msg, Object data) {
        return LMSResponse.builder()
                .statusCode(Constant.SUCCESS_CODE)
                .msg(msg)
                .data(data)
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
    public static LMSResponse fail(String errorMsg, Object data) {
        return LMSResponse.builder()
                .statusCode(Constant.FAILURE_CODE)
                .msg(errorMsg)
                .data(data.toString())
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
}

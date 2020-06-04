package com.linkknown.ilearning.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageListResponse extends BaseResponse {

    private List<Message> messages;
    private Paginator paginator;

    @Data
    public static class Message {

        private int id;
        private Date last_updated_time;
        private String message_text;
        private String message_type;
        private String user_name;
    }
}

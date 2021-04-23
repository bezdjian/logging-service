package hb.loggingservice.mapper;

import hb.loggingservice.entity.Message;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class MessageMapper {

    public Message toEntityModel(MessageRequestModel messageRequestModel) {
        return Message.builder()
            .createdAt(LocalDateTime.now().withNano(0))
            .message(messageRequestModel.getMessage())
            .name(messageRequestModel.getName())
            .build();
    }

    public MessageResponseModel toMessageModel(Message message) {
        return MessageResponseModel.builder()
            .logId(message.getLog().getId())
            .createdAt(message.getCreatedAt())
            .message(message.getMessage())
            .name(message.getName())
            .build();
    }
}

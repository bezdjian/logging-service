package hb.loggingservice;

import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class TestUtil {

    public Log createMockLog(String testMessage) {
        Message mockMessage = createMockMessage(testMessage);
        Log log = Log.builder()
            .id(1L)
            .build();
        mockMessage.setLog(log);
        log.setMessages(List.of(mockMessage));
        return log;
    }

    public MessageRequestModel createMockMessageRequestModel(Long logId, String message) {
        return MessageRequestModel.builder()
            .logId(logId)
            .message(message)
            .name("test name")
            .build();
    }

    private Message createMockMessage(String testMessage) {
        return Message.builder()
            .message(testMessage)
            .name("test name")
            .createdAt(LocalDateTime.now())
            .build();
    }
}

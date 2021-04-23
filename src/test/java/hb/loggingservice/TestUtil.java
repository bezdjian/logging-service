package hb.loggingservice;

import hb.loggingservice.entity.Configuration;
import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Configuration createMockConfiguration(String configName, int value) {
        return Configuration.builder()
            .name(configName)
            .value(value)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public Log createMockLogWithFourMessages() {
        LocalDateTime fiveSecondsToLive = LocalDateTime.now().plusSeconds(5);
        LocalDateTime fifteenSecondsToLive = LocalDateTime.now().plusSeconds(15);
        LocalDateTime twelveSecondsOld = LocalDateTime.now().minusSeconds(12);
        LocalDateTime twentySecondsOld = LocalDateTime.now().minusSeconds(20);
        Message mockMessage1 = createMockMessage("message1", fiveSecondsToLive);
        Message mockMessage2 = createMockMessage("message2", fifteenSecondsToLive);
        Message mockMessage3 = createMockMessage("message3", twelveSecondsOld);
        Message mockMessage4 = createMockMessage("message4", twentySecondsOld);
        Log log = Log.builder().id(1L).build();
        mockMessage1.setLog(log);
        mockMessage2.setLog(log);
        mockMessage3.setLog(log);
        mockMessage4.setLog(log);
        log.setMessages(new ArrayList<>(Arrays.asList(mockMessage1, mockMessage2, mockMessage3, mockMessage4)));
        return log;
    }

    private Message createMockMessage(String testMessage, LocalDateTime time) {
        Message message = createMockMessage(testMessage);
        message.setCreatedAt(time);
        return message;
    }
}

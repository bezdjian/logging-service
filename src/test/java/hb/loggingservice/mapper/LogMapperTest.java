package hb.loggingservice.mapper;

import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.model.LogModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogMapperTest {

    @Test
    @DisplayName("Successfully maps log entity to log model")
    void toLogModel() {
        //Given
        final String testMessage = "test message";
        final Long logId = 1L;

        Message message = Message.builder()
            .createdAt(LocalDateTime.now())
            .message(testMessage)
            .name("test name")
            .build();
        Log log = Log.builder()
            .id(logId)
            .build();
        message.setLog(log);
        log.setMessages(List.of(message));

        //When
        LogModel logModel = LogMapper.toLogModel(log);
        //Then
        assertNotNull(logModel);
        assertFalse(logModel.getMessages().isEmpty());
        assertEquals(testMessage, logModel.getMessages().get(0).getMessage());
        assertEquals(logId, logModel.getLogId());
        System.out.println(logModel);
    }
}
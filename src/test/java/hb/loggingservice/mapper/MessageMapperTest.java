package hb.loggingservice.mapper;

import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageMapperTest {

    private static final String TEST_MESSAGE = "test message";

    @Test
    @DisplayName("Successfully maps message request to message entity")
    void toEntityModel() {
        //Given
        MessageRequestModel messageRequestModel = MessageRequestModel.builder()
            .logId(1L)
            .message(TEST_MESSAGE)
            .name("test name")
            .build();

        //When
        Message message = MessageMapper.toEntityModel(messageRequestModel);
        //Then
        assertNotNull(message);
        assertEquals(TEST_MESSAGE, message.getMessage());
    }

    @Test
    @DisplayName("Successfully maps message entity to message model")
    void toMessageModel() {
        //Given
        Message message = Message.builder()
            .createdAt(LocalDateTime.now())
            .message(TEST_MESSAGE)
            .name("test name")
            .log(Log.builder()
                .id(1L)
                .build())
            .build();

        //When
        MessageResponseModel messageResponseModel = MessageMapper.toMessageModel(message);
        //Then
        assertNotNull(messageResponseModel);
        assertEquals(TEST_MESSAGE, messageResponseModel.getMessage());
    }
}
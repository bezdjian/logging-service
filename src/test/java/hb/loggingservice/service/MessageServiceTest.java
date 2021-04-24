package hb.loggingservice.service;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.exception.MessageBadRequestException;
import hb.loggingservice.mapper.MessageMapper;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import hb.loggingservice.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MessageServiceTest {

    private static final String TEST_MESSAGE = "Test message";

    @InjectMocks
    private MessageService messageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private LogService logService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should add a message to a none existing log")
    void shouldAddMessageToNoneExistingLog() {
        //Given
        long logId = 1L;
        MessageRequestModel messageRequestModel = TestUtil.createMockMessageRequestModel(logId, TEST_MESSAGE);
        Message message = MessageMapper.toEntityModel(messageRequestModel);
        when(logService.findLogById(logId)).thenReturn(Optional.empty());
        Log newLog = Log.builder()
            .id(logId)
            .build();
        when(logService.createLog(logId)).thenReturn(newLog);
        message.setLog(newLog);
        when(messageRepository.save(message)).thenReturn(message);
        //When
        MessageResponseModel messageResponseModel = messageService.addMessage(messageRequestModel);
        //Then
        verify(messageRepository).save(message);
        verifyNoMoreInteractions(messageRepository);
        assertNotNull(messageResponseModel);
    }

    @Test
    @DisplayName("Should add a message to an existing log")
    void shouldAddMessageToAnExistingLog() {
        //Given
        long logId = 1L;
        MessageRequestModel messageRequestModel = TestUtil.createMockMessageRequestModel(logId, TEST_MESSAGE);
        Message message = MessageMapper.toEntityModel(messageRequestModel);
        Log log = TestUtil.createMockLog(TEST_MESSAGE);
        when(logService.findLogById(logId)).thenReturn(Optional.of(log));
        message.setLog(log);
        when(messageRepository.save(message)).thenReturn(message);
        //When
        messageService.addMessage(messageRequestModel);
        //Then
        verify(logService, never()).createLog(logId);
        verify(messageRepository).save(message);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @DisplayName("Should throw message bad request exception when given log id is less than 0")
    void shouldThrowBadRequestWhenGivenLogIdLessThanZero() {
        //Given
        long logId = -1L;
        MessageRequestModel messageRequestModel = TestUtil.createMockMessageRequestModel(logId, TEST_MESSAGE);
        Message message = MessageMapper.toEntityModel(messageRequestModel);
        when(logService.findLogById(logId)).thenReturn(Optional.empty());
        String expectedErrorMsg = "Log id must be greater than 0!";

        //When & Then
        assertThatThrownBy(() -> messageService.addMessage(messageRequestModel))
            .isInstanceOf(MessageBadRequestException.class)
            .hasMessage(expectedErrorMsg);
        verify(messageRepository, never()).save(message);
    }
}

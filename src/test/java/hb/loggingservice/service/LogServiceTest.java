package hb.loggingservice.service;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Log;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class LogServiceTest {

    private static final String TEST_MESSAGE = "test message";
    private static final Long LOG_ID = 1L;

    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should find all logs with messages without max age value")
    void shouldFindAllLogs() {
        //Given
        when(logRepository.findAll()).thenReturn(List.of(TestUtil.createMockLog(TEST_MESSAGE)));
        //When
        List<LogModel> logs = logService.findAllLogs();
        //Then
        assertFalse(logs.isEmpty());
        assertFalse(logs.get(0).getMessages().isEmpty());
        assertEquals(TEST_MESSAGE, logs.get(0).getMessages().get(0).getMessage());
    }

    @Test
    @DisplayName("Should find a log by its id with messages")
    void shouldFindLogById() {
        //Given
        when(logRepository.findById(LOG_ID))
            .thenReturn(Optional.of(TestUtil.createMockLog(TEST_MESSAGE)));
        //When
        Optional<Log> log = logService.findLogById(LOG_ID);
        //Then
        assertTrue(log.isPresent());
        assertEquals(LOG_ID, log.get().getId());
        assertFalse(log.get().getMessages().isEmpty());
        assertEquals(TEST_MESSAGE, log.get().getMessages().get(0).getMessage());
    }

    @Test
    @DisplayName("Should create a log with given id with no messages")
    void shouldCreateLogWithNoMessages() {
        //Given
        Log newLog = Log.builder()
            .id(LOG_ID)
            .build();
        when(logRepository.save(any(Log.class))).thenReturn(newLog);
        //When
        Log log = logService.createLog(LOG_ID);
        //Then
        assertEquals(LOG_ID, log.getId());
        assertTrue(log.getMessages().isEmpty());
    }

    @Test
    @DisplayName("Should get log by given id")
    void shouldFetLogById() {
        //Given
        when(logRepository.findById(LOG_ID))
            .thenReturn(Optional.of(TestUtil.createMockLog(TEST_MESSAGE)));
        //When
        Optional<LogModel> log = logService.getLog(LOG_ID);
        //Then
        assertTrue(log.isPresent());
        assertEquals(LOG_ID, log.get().getLogId());
        assertFalse(log.get().getMessages().isEmpty());
        assertEquals(TEST_MESSAGE, log.get().getMessages().get(0).getMessage());
    }
}
package hb.loggingservice.scheduler;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Message;
import hb.loggingservice.repository.ConfigurationRepository;
import hb.loggingservice.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RemoveOldMessageSchedulerTest {

    private static final String CONFIG_NAME = "MAX_AGE";
    @InjectMocks
    private RemoveOldMessageScheduler removeOldMessageScheduler;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ConfigurationRepository configurationRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should run scheduler and remove 2 older messages")
    void shouldRunSchedulerAndRemoveOlderMessages() {
        //Given
        int maxAgeValue = 10;
        when(configurationRepository.findConfigurationByName(CONFIG_NAME))
            .thenReturn(Optional.of(TestUtil.createMockConfiguration(CONFIG_NAME, maxAgeValue)));
        when(messageRepository.findAll()).thenReturn(
            TestUtil.createMockLogWithFourMessages().getMessages());
        //When
        removeOldMessageScheduler.run();
        //Then
        verify(messageRepository, times(2)).delete(any(Message.class));
    }
}
package hb.loggingservice.service;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Configuration;
import hb.loggingservice.model.ServiceStateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ServiceStateServiceTest {

    @InjectMocks
    private ServiceStateService stateService;
    @Mock
    private LogService logService;
    @Mock
    private ConfigurationService configurationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should display the state of the service with information")
    void shouldDisplayServiceStateWithInformation() {
        //Given
        final int maxAgeValue = 10;
        final String configName = "MAX_AGE";
        when(logService.findAllLogs()).thenReturn(
            List.of(TestUtil.createMockLogModel(1L, "test message"),
                TestUtil.createMockLogModel(2L, "test message 2"))
        );
        when(configurationService.findAllConfigurations())
            .thenReturn(List.of(Configuration.builder()
                .name(configName)
                .value(maxAgeValue)
                .build()));
        //When
        ServiceStateModel serviceState = stateService.getServiceState();
        //Then
        int expectedNumberOfLogs = 2;
        int expectedAverageMessages = 1;
        assertEquals(expectedNumberOfLogs, serviceState.getNumberOfLogs());
        assertEquals(expectedAverageMessages, serviceState.getAverageNumberOfMessagesPerLog());
        assertEquals(maxAgeValue, serviceState.getMaxAge());
    }

    @Test
    @DisplayName("Should display the state of the service with default information values")
    void shouldDisplayServiceStateWithDefaultInformation() {
        //Given
        when(logService.findLatestLogs()).thenReturn(Collections.emptyList());
        when(configurationService.findAllConfigurations())
            .thenReturn(Collections.emptyList());
        //When
        ServiceStateModel serviceState = stateService.getServiceState();
        //Then
        int expectedNumberOfLogs = 0;
        int expectedAverageMessages = 0;
        int expectedMaxAgeValue = 0;
        assertEquals(expectedNumberOfLogs, serviceState.getNumberOfLogs());
        assertEquals(expectedAverageMessages, serviceState.getAverageNumberOfMessagesPerLog());
        assertEquals(expectedMaxAgeValue, serviceState.getMaxAge());
    }
}
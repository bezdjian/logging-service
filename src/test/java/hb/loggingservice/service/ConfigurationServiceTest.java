package hb.loggingservice.service;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Configuration;
import hb.loggingservice.exception.ConfigurationBadRequestException;
import hb.loggingservice.exception.ConfigurationException;
import hb.loggingservice.repository.ConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ConfigurationServiceTest {

    @InjectMocks
    private ConfigurationService configurationService;
    @Mock
    private ConfigurationRepository configurationRepository;

    @Captor
    private ArgumentCaptor<Configuration> configurationCaptor;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should create configuration with given name and its value")
    void shouldCreateConfigurationWithNameAndValue() {
        //Given
        final int value = 10;
        final String configName = "MAX_AGE";
        when(configurationRepository.findConfigurationByName(configName))
            .thenReturn(Optional.empty());
        //When
        configurationService.saveConfiguration(configName, value);
        //Then
        verify(configurationRepository).save(configurationCaptor.capture());
        Configuration savedConfiguration = configurationCaptor.getValue();
        assertEquals(value, savedConfiguration.getValue());
        assertEquals(configName, savedConfiguration.getName());
    }

    @Test
    @DisplayName("Should update an existing configuration with given name and its value")
    void shouldUpdateConfigurationWithNameAndValue() {
        //Given
        final int value = 10;
        final String configName = "MAX_AGE";
        when(configurationRepository.findConfigurationByName(configName))
            .thenReturn(Optional.of(TestUtil.createMockConfiguration(configName, value)));
        //When
        configurationService.saveConfiguration(configName, value);
        //Then
        verify(configurationRepository).save(configurationCaptor.capture());
        Configuration savedConfiguration = configurationCaptor.getValue();
        assertEquals(value, savedConfiguration.getValue());
        assertEquals(configName, savedConfiguration.getName());
    }

    @Test
    @DisplayName("Should throw bad request for max age configuration value")
    void shouldThrowBadRequestForMaxAgeConfigurationValue() {
        //Given
        final int value = 0;
        final String expectedErrorMessage = "Max age value should be greater than 1!";
        String configName = "MAX_AGE";
        //When
        assertThatThrownBy(() -> configurationService.saveConfiguration(configName, value))
            .isInstanceOf(ConfigurationBadRequestException.class)
            .hasMessage(expectedErrorMessage);
        //Then
        verify(configurationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should catch and rethrow message exception")
    void shouldCatchAndRethrowMessageException() {
        //Given
        final String configName = "dummyName";
        final int value = 0;
        String expectedExceptionMessage = "Unable to save configuration " + configName;
        when(configurationRepository.save(any(Configuration.class)))
            .thenThrow(RuntimeException.class);

        //When & expect
        assertThatThrownBy(() -> configurationService.saveConfiguration(configName, value))
            .isInstanceOf(ConfigurationException.class)
            .hasMessage(expectedExceptionMessage);
    }

    @Test
    @DisplayName("Should find all configurations")
    void shouldFindAllConfigurations() {
        //Given
        final String configName = "MAX_AGE";
        final int maxAgeValue = 10;
        when(configurationRepository.findAll())
            .thenReturn(List.of(TestUtil.createMockConfiguration(configName, maxAgeValue)));
        //When
        List<Configuration> configurations = configurationService.findAllConfigurations();
        //Then
        assertFalse(configurations.isEmpty());
        assertEquals(1, configurations.size());
    }
}
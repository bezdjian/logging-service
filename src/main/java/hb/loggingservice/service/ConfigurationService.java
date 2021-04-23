package hb.loggingservice.service;

import hb.loggingservice.entity.Configuration;
import hb.loggingservice.exception.ConfigurationBadRequestException;
import hb.loggingservice.exception.ConfigurationException;
import hb.loggingservice.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private static final String CONFIG_NAME_MAX_AGE = "MAX_AGE";
    private final ConfigurationRepository configurationRepository;

    /**
     * Creates or updates a configuration with given name and value.
     *
     * @param configurationName Name of the configuration
     * @param value             Value of the configuration
     */
    public void saveConfiguration(String configurationName, int value) {
        validateConfigurationValue(configurationName, value);

        try {
            configurationRepository.findConfigurationByName(configurationName)
                .ifPresentOrElse(configuration -> updateConfiguration(configuration, value),
                    () -> createConfiguration(configurationName, value));
        } catch (Exception e) {
            String errorMessage = String.format("Unable to save configuration %s", configurationName);
            throw new ConfigurationException(errorMessage, e);
        }
    }

    public List<Configuration> findAllConfigurations() {
        return configurationRepository.findAll();
    }

    private void updateConfiguration(Configuration configuration, int value) {
        configuration.setValue(value);
        configuration.setTimestamp(LocalDateTime.now());
        configurationRepository.save(configuration);
    }

    private void createConfiguration(String configurationName, int value) {
        configurationRepository.save(Configuration.builder()
            .name(configurationName)
            .value(value)
            .timestamp(LocalDateTime.now())
            .build());
    }

    private void validateConfigurationValue(String configurationName, int value) {
        if (configurationName.equals(CONFIG_NAME_MAX_AGE) && value < 1)
            throw new ConfigurationBadRequestException("Max age value should be greater than 1!");
    }
}

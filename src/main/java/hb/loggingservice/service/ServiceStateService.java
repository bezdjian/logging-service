package hb.loggingservice.service;

import hb.loggingservice.entity.Configuration;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.model.ServiceStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceStateService {

    private static final String CONFIG_NAME = "MAX_AGE";
    private final LogService logService;
    private final ConfigurationService configurationService;

    /**
     * Returns the state of the service with information.
     *
     * @return ServiceState response model
     */
    public ServiceStateModel getServiceState() {
        List<LogModel> logs = logService.findAllLogs();

        int maxAgeValue = getMaxAgeValue();
        int numberOfMessages = getNumberOfAllMessages(logs);
        int numberOfLogs = logs.size();
        double averageMessagesPerLog = getAverageMessagesPerLog(logs);

        return ServiceStateModel.builder()
            .numberOfLogs(numberOfLogs)
            .totalNumberOfMessages(numberOfMessages)
            .maxAge(maxAgeValue)
            .averageNumberOfMessagesPerLog(averageMessagesPerLog)
            .build();
    }

    private double getAverageMessagesPerLog(List<LogModel> logs) {
        return logs.stream()
            .map(LogModel::getMessages)
            .mapToInt(List::size)
            .average()
            .stream()
            .map(average -> BigDecimal.valueOf(average)
                .setScale(2, RoundingMode.FLOOR)
                .doubleValue())
            .findFirst()
            .orElse(0);
    }

    private int getNumberOfAllMessages(List<LogModel> logs) {
        return logs.stream()
            .map(LogModel::getMessages)
            .mapToInt(List::size)
            .sum();
    }

    private Integer getMaxAgeValue() {
        return configurationService.findAllConfigurations()
            .stream()
            .filter(configuration -> configuration.getName().equals(CONFIG_NAME))
            .map(Configuration::getValue)
            .findFirst()
            .orElse(0);
    }
}

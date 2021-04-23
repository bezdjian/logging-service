package hb.loggingservice.service;

import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.mapper.LogMapper;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.repository.ConfigurationRepository;
import hb.loggingservice.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private static final String CONFIG_MAX_AGE = "MAX_AGE";
    private final LogRepository logRepository;
    private final ConfigurationRepository configurationRepository;

    /**
     * Finds and returns logs with messages.
     * If MAX_AGE configuration exists, remove messages that are older than max age value in seconds.
     *
     * @return List of LogModel mapped by Log entity
     */
    public List<LogModel> findLatestLogs() {
        List<Log> logList = logRepository.findAll();
        configurationRepository.findConfigurationByName(CONFIG_MAX_AGE)
            .ifPresent(configuration -> removeOlderMessages(logList, configuration.getValue()));

        return logList
            .stream()
            .map(LogMapper::toLogModel)
            .collect(Collectors.toList());
    }

    /**
     * Returns all the logs with their messages from the table
     *
     * @return List of LogModel mapped by Log entity
     */
    public List<LogModel> findAllLogs() {
        return logRepository.findAll()
            .stream()
            .map(LogMapper::toLogModel)
            .collect(Collectors.toList());
    }

    public Optional<Log> findLogById(Long id) {
        return logRepository.findById(id);
    }

    /**
     * Creates a new Log entity when needed
     *
     * @param id Long, Log ID given when requesting
     * @return Newly created Log entity
     */
    @Transactional
    public Log createLog(Long id) {
        return logRepository.save(Log.builder()
            .id(id)
            .build());
    }

    /**
     * Gets log by its id
     *
     * @param id Long, Log's ID
     * @return Optional Log entity
     */
    public Optional<LogModel> getLog(Long id) {
        return findLogById(id)
            .map(LogMapper::toLogModel);
    }

    private void removeOlderMessages(List<Log> logs, int maxAge) {
        logs.forEach(log -> log.getMessages().removeIf(message -> isOlder(message, maxAge)));
    }

    private boolean isOlder(Message message, int maxAge) {
        LocalDateTime maxAgeTimeInSeconds = LocalDateTime.now().minusSeconds(maxAge);
        return message.getCreatedAt().isBefore(maxAgeTimeInSeconds);
    }
}

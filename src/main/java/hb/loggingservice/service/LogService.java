package hb.loggingservice.service;

import hb.loggingservice.entity.Log;
import hb.loggingservice.mapper.LogMapper;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

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
}

package hb.loggingservice.scheduler;

import hb.loggingservice.entity.Configuration;
import hb.loggingservice.entity.Message;
import hb.loggingservice.repository.ConfigurationRepository;
import hb.loggingservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveOldMessageScheduler {

    private static final String CONFIG_NAME = "MAX_AGE";
    private final MessageRepository messageRepository;
    private final ConfigurationRepository configurationRepository;

    @Scheduled(fixedRateString = "${cronjob.removeOldMessages.interval}")
    public void run() {
        log.info("Running cron job for old message removal");
        configurationRepository.findConfigurationByName(CONFIG_NAME)
            .map(Configuration::getValue)
            .ifPresentOrElse(maxAgeValue -> {
                log.info("Configuration for maxAge value is {} seconds", maxAgeValue);
                List<Message> messages = messageRepository.findAll();
                log.info("Number of total message(s) {}", messages.size());

                List<Message> oldMessages = messages.stream()
                    .filter(message -> isOlder(maxAgeValue, message))
                    .collect(Collectors.toList());

                if (!oldMessages.isEmpty()) {
                    log.info("Number of old message(s) to be deleted {}", oldMessages.size());
                    oldMessages.forEach(messageRepository::delete);
                    log.info("Old message(s) are successfully deleted!");
                } else {
                    log.info("There are no old messages to be deleted.");
                }
            }, () -> log.info("Configuration for maxAge value not found"));
    }

    private boolean isOlder(Integer maxAgeValue, Message message) {
        LocalDateTime maxAgeTimeInSeconds = LocalDateTime.now().minusSeconds(maxAgeValue);
        return message.getCreatedAt().isBefore(maxAgeTimeInSeconds);
    }
}

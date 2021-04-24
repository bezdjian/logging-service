package hb.loggingservice.service;

import hb.loggingservice.entity.Log;
import hb.loggingservice.entity.Message;
import hb.loggingservice.exception.MessageBadRequestException;
import hb.loggingservice.mapper.MessageMapper;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import hb.loggingservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final LogService logService;

    /**
     * Adds a new message to a log with given log id.
     * This creates a new log if there is no log with given log id and sets to the message.
     *
     * @param messageRequest Request model to add a new message
     * @return Message model to return
     */
    @Transactional
    public MessageResponseModel addMessage(MessageRequestModel messageRequest) {
        validateLogId(messageRequest);
        Message message = MessageMapper.toEntityModel(messageRequest);
        Optional<Log> log = logService.findLogById(messageRequest.getLogId());

        log.ifPresentOrElse(
            message::setLog,
            () -> createNewLogForMessage(messageRequest, message));

        Message newMessage = messageRepository.save(message);
        return MessageMapper.toMessageModel(newMessage);
    }

    private void validateLogId(MessageRequestModel messageRequest) {
        if (messageRequest.getLogId() < 1)
            throw new MessageBadRequestException("Log id must be greater than 0!");
    }

    private void createNewLogForMessage(MessageRequestModel messageRequestModel, Message message) {
        Log newLog = logService.createLog(messageRequestModel.getLogId());
        message.setLog(newLog);
    }
}

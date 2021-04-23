package hb.loggingservice.mapper;

import hb.loggingservice.entity.Log;
import hb.loggingservice.model.LogModel;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class LogMapper {

    public LogModel toLogModel(Log log) {
        return LogModel.builder()
            .logId(log.getId())
            .messages(log.getMessages()
                .stream()
                .map(MessageMapper::toMessageModel)
                .collect(Collectors.toList()))
            .build();
    }
}

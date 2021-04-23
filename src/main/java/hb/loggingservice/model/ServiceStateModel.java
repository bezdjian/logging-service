package hb.loggingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStateModel {
    private int numberOfLogs;
    private int maxAge;
    private int totalNumberOfMessages;
    private double averageNumberOfMessagesPerLog;
}

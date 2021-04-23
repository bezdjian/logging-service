package hb.loggingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseModel {
    private Long logId;
    private String name;
    private String message;
    private LocalDateTime createdAt;
}

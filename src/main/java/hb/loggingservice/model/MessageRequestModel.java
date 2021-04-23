package hb.loggingservice.model;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequestModel {
    @ApiParam(value = "Log id to add message to", example = "1")
    private Long logId;
    @ApiParam(value = "Name of the submitting party", example = "Qvik")
    private String name;
    @ApiParam(value = "Message to be added", example = "example message text")
    private String message;
}

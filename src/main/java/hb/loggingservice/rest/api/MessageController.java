package hb.loggingservice.rest.api;

import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.MessageRequestModel;
import hb.loggingservice.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "Add message with given log id")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Message successfully created",
            response = MessageResponseModel.class),
        @ApiResponse(code = 500, message = "Internal server error"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/add")
    public ResponseEntity<Object> addMessage(
        @ApiParam(value = "Request body for message creation")
        @RequestBody MessageRequestModel messageRequestModel) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.addMessage(messageRequestModel));
    }
}

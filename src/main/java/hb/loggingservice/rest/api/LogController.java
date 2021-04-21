package hb.loggingservice.rest.api;

import hb.loggingservice.model.LogModel;
import hb.loggingservice.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @ApiOperation(value = "Fetch all logs with messages")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Logs successfully fetched",
            response = LogModel.class, responseContainer = "List"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping()
    public ResponseEntity<List<LogModel>> getAllLogs() {
        return ResponseEntity.ok(logService.findAllLogs());
    }

    @ApiOperation(value = "Fetch a log with messages by id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Log successfully fetched",
            response = LogModel.class),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{logId}")
    public ResponseEntity<LogModel> getLog(
        @ApiParam(value = "Log id to retrieve messages", example = "1")
        @PathVariable("logId") Long logId) {
        return ResponseEntity.of(logService.getLog(logId));
    }
}

package hb.loggingservice.rest.api;

import hb.loggingservice.model.ConfigurationNames;
import hb.loggingservice.model.ResponseModel;
import hb.loggingservice.service.ConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configure")
@RequiredArgsConstructor
@Api(tags = "Configuration")
public class ConfigurationController {

    private static final String SUCCESSFULLY_CREATED_MSG = "Configuration has been successfully saved for %s";
    private final ConfigurationService configurationService;

    @ApiOperation(value = "Create a configuration",
        notes = "- MAX_AGE: Numbers in seconds to set visibility on messages in logs")
    @ApiResponse(code = 200, message = "Configuration is created",
        response = ResponseModel.class)
    @PutMapping("/{value}")
    public ResponseEntity<ResponseModel> createConfiguration(
        @ApiParam(value = "The value of the configuration", example = "10")
        @PathVariable int value,
        @ApiParam(value = "Name of the configuration", example = "MAX_AGE")
        @RequestParam ConfigurationNames configurationName) {
        configurationService.saveConfiguration(configurationName.name(), value);
        return ResponseEntity.ok(ResponseModel.builder()
            .message(formatResponseMessage(configurationName))
            .status(HttpStatus.OK.value())
            .build());
    }

    private String formatResponseMessage(ConfigurationNames configurationName) {
        return String.format(SUCCESSFULLY_CREATED_MSG, configurationName.name());
    }
}

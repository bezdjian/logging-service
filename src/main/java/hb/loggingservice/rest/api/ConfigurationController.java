package hb.loggingservice.rest.api;

import hb.loggingservice.model.ConfigurationNames;
import hb.loggingservice.service.ConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
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

    private final ConfigurationService configurationService;

    @ApiOperation(value = "Create a configuration",
        notes = "- MAX_AGE: Numbers in seconds to set visibility on messages in logs")
    @ApiResponse(code = 200, message = "Configuration is created")
    @PutMapping("/{value}")
    public ResponseEntity<String> createConfiguration(
        @ApiParam(value = "The value of the configuration", example = "10")
        @PathVariable int value,
        @ApiParam(value = "Name of the configuration", example = "MAX_AGE")
        @RequestParam ConfigurationNames configurationName) {
        configurationService.saveConfiguration(configurationName.name(), value);
        return ResponseEntity.ok().build();
    }
}

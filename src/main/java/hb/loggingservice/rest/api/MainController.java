package hb.loggingservice.rest.api;

import hb.loggingservice.model.ServiceStateModel;
import hb.loggingservice.service.ServiceStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Main")
@RequiredArgsConstructor
public class MainController {

    private final ServiceStateService stateService;

    @ApiOperation(value = "Displays state of the service with information")
    @ApiResponse(code = 200, message = "Returns state of the service",
        response = ServiceStateModel.class)
    @GetMapping
    public ResponseEntity<ServiceStateModel> displayServiceState() {
        return ResponseEntity.ok(stateService.getServiceState());
    }
}

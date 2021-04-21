package hb.loggingservice.rest.api;

import hb.loggingservice.TestUtil;
import hb.loggingservice.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogControllerTest {

    private static final String BASE_URL = "/logs";
    private static final String TEST_MESSAGE = "test message";
    private static final Long LOG_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should get all logs")
    void shouldGetAllLogs() throws Exception {
        //Given
        when(logRepository.findAll()).thenReturn(List.of(TestUtil.createMockLog(TEST_MESSAGE)));
        //When & expect
        mockMvc.perform(get(BASE_URL))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.[0].logId").value(LOG_ID))
            .andExpect(jsonPath("$.[0].messages").isArray());
    }

    @Test
    @DisplayName("Should get a log by id")
    void shouldGetLogById() throws Exception {
        // Given
        when(logRepository.findById(LOG_ID)).thenReturn(Optional.of(TestUtil.createMockLog(TEST_MESSAGE)));
        // When & expect
        mockMvc.perform(get(BASE_URL + "/" + LOG_ID))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.logId").value(LOG_ID))
            .andExpect(jsonPath("$.messages").isArray());
    }
}
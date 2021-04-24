package hb.loggingservice.rest.api;

import hb.loggingservice.TestUtil;
import hb.loggingservice.repository.ConfigurationRepository;
import hb.loggingservice.repository.LogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    private static final String BASE_URL = "/";
    @MockBean
    ConfigurationRepository configurationRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LogRepository logRepository;

    @Test
    @DisplayName("Successfully returns service state with default information values")
    void shouldDisplayServiceStateWithDefaultValues() throws Exception {
        //When and expect
        int expectedNumberOfLogs = 0;
        int expectedMaxAge = 0;
        int expectedTotalNumberOfMessages = 0;
        int expectedAverageNumberOfMessagesPerLog = 0;

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.numberOfLogs").value(expectedNumberOfLogs))
            .andExpect(jsonPath("$.maxAge").value(expectedMaxAge))
            .andExpect(jsonPath("$.totalNumberOfMessages").value(expectedTotalNumberOfMessages))
            .andExpect(jsonPath("$.averageNumberOfMessagesPerLog").value(expectedAverageNumberOfMessagesPerLog));
    }

    @Test
    @DisplayName("Successfully returns service state with information values")
    void shouldDisplayServiceStateWithValues() throws Exception {
        //Given
        final String configName = "MAX_AGE";
        int maxAgeValue = 10;
        when(logRepository.findAll()).thenReturn(
            List.of(TestUtil.createMockLogWithFourMessages(),
                TestUtil.createMockLog("test message 1"),
                TestUtil.createMockLog("test message 2"))
        );
        when(configurationRepository.findAll())
            .thenReturn(List.of(TestUtil.createMockConfiguration(configName, maxAgeValue)));
        //When and expect
        int expectedNumberOfLogs = 3;
        int expectedMaxAge = 10;
        int expectedTotalNumberOfMessages = 6;
        double expectedAverageNumberOfMessagesPerLog = 2;

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.numberOfLogs").value(expectedNumberOfLogs))
            .andExpect(jsonPath("$.maxAge").value(expectedMaxAge))
            .andExpect(jsonPath("$.totalNumberOfMessages").value(expectedTotalNumberOfMessages))
            .andExpect(jsonPath("$.averageNumberOfMessagesPerLog").value(expectedAverageNumberOfMessagesPerLog));
    }
}
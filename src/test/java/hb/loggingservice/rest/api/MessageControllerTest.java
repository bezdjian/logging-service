package hb.loggingservice.rest.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageControllerTest {

    private static final Long LOG_ID = 1L;
    private static final String ADD_MESSAGE_URL = "/message/add";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("Should add a message to none existing Log")
    void shouldAddMessageToNoneExistingLog() throws Exception {
        // Given
        JSONObject messageRequest = mockAddMessageRequest();

        // When & expect
        mockMvc.perform(post(ADD_MESSAGE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageRequest.toString()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andExpect(jsonPath("$.logId").value(LOG_ID));
    }

    @Test
    @Order(2)
    @DisplayName("Should add a message to an existing Log")
    void shouldAddMessageToAnExistingLog() throws Exception {
        // Given
        JSONObject messageRequest = mockAddMessageRequest();

        // When & expect
        mockMvc.perform(post(ADD_MESSAGE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageRequest.toString()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andExpect(jsonPath("$.logId").value(LOG_ID));
    }

    private JSONObject mockAddMessageRequest() throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("logId", LOG_ID);
        jsonRequest.put("name", "messageName");
        jsonRequest.put("message", "test message");
        return jsonRequest;
    }
}
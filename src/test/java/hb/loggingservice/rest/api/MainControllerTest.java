package hb.loggingservice.rest.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    private static final String BASE_URL = "/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return Hello World!")
    void sayHello() throws Exception {
        //Given
        final String expectedResult = "Hello World!";
        //When and expect
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(content().string(expectedResult));
    }
}
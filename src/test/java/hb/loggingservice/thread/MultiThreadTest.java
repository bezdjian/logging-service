package hb.loggingservice.thread;

import hb.loggingservice.TestUtil;
import hb.loggingservice.entity.Log;
import hb.loggingservice.repository.LogRepository;
import hb.loggingservice.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MultiThreadTest {

    private static final int NUMBER_OF_THREADS = 100;

    @Autowired
    private MessageService messageService;
    @Autowired
    private LogRepository logRepository;

    @Test
    void shouldAddHundredMessages() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
        AtomicLong logId = new AtomicLong(1);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            service.execute(() -> {
                messageService
                    .addMessage(TestUtil.createMockMessageRequestModel(logId.getAndIncrement(), "Test message"));
                latch.countDown();
            });
        }
        latch.await();

        List<Log> logs = logRepository.findAll();
        assertEquals(NUMBER_OF_THREADS, logs.size());
    }
}

package com.asianaidt.ict.analyca.webserver.log;

import com.asianaidt.ict.analyca.webserver.service.ScheduleRunService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


@Disabled
//@SpringBootTest(classes = {ScheduleRunServiceImpl.class})
@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogTest {
    @Autowired
    ScheduleRunService scheduleRunService;

//    private final ScheduleRunService scheduleRunService;
//    public LogTest(ScheduleRunService scheduleRunService) {
//        this.scheduleRunService = scheduleRunService;
//    }

    @Disabled
    @Test
    public void logTest() {
        scheduleRunService.scheduleRun(45L);
    }
}

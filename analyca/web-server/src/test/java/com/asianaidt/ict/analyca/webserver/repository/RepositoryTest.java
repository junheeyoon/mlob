package com.asianaidt.ict.analyca.webserver.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.repository.ScheduleRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@SpringBootTest
//@SpringBootTest(classes = {ScheduleRepository.class})
//@SpringBootTest(classes = {ScheduleServiceImpl.class})
//@SpringBootTest(classes = {ScheduleServiceImpl.class, ScheduleRepository.class})
//@ContextConfiguration
//@EnableJpaRepositories(basePackageClasses = {ScheduleRepository.class})
//@EnableJpaRepositories(basePackageClasses = {ScheduleServiceImpl.class})
//@EnableJpaRepositories(basePackageClasses = {ScheduleServiceImpl.class, ScheduleRepository.class})
//@EntityScan(basePackageClasses = {Schedule.class})

//@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

//    @Autowired
//    private ScheduleRepository aaa;
//
////    @Autowired
////    private ScheduleServiceImpl aaa;

    @Disabled
    @Test
    public void findScheduleById() {
//        Schedule req = new Schedule();
//        req.setId(3L);
////        Schedule res = scheduleServiceImpl.findScheduleById(3L);
//        Schedule res = aaa.findScheduleById(3L);
//        System.out.println("##### Repository Test ");
//        System.out.println(res);
//
//        assertEquals(res.getId(),req.getId());
    }


    @Disabled
    @Test
    public void findAllByDeleted() {
//        List<Schedule> listSchedule = aaa.findByDeleted(Schedule.deletedStatus.EXIST);
//
//        System.out.println("");
    }
}

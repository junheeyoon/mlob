package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule save(Schedule schedule);

    List<Schedule> findAll();

    Optional<Schedule> findById(Long id);

    Schedule findByScheduleName(String s);

    List<Schedule> findByDeleted(Schedule.deletedStatus deleted);

    List<Schedule> findByDtEndAfterAndDeleted(LocalDateTime now, Schedule.deletedStatus deleted);
}

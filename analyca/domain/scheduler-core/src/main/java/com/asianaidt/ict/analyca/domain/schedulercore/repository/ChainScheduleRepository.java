package com.asianaidt.ict.analyca.domain.schedulercore.repository;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChainScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByScheduleName(String s);
}

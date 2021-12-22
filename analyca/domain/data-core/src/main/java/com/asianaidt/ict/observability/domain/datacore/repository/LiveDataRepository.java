package com.asianaidt.ict.observability.domain.datacore.repository;

import com.asianaidt.ict.observability.domain.datacore.model.LiveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveDataRepository extends JpaRepository<LiveData, Long> {

    LiveData save(LiveData liveData);
    List<LiveData> findAll();
}

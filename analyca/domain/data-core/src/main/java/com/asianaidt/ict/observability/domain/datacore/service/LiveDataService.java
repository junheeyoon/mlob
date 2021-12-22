package com.asianaidt.ict.observability.domain.datacore.service;

import com.asianaidt.ict.observability.domain.datacore.model.LiveData;

import java.util.List;

public interface LiveDataService {

    LiveData save(LiveData liveData);
    List<LiveData> findAll();
}

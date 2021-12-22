package com.asianaidt.ict.observability.domain.datacore.service;

import com.asianaidt.ict.observability.domain.datacore.model.LiveData;
import com.asianaidt.ict.observability.domain.datacore.repository.LiveDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiveDataServiceImpl implements LiveDataService {

    private final LiveDataRepository liveDataRepository;

    public LiveDataServiceImpl(LiveDataRepository liveDataRepository) {
        this.liveDataRepository = liveDataRepository;
    }


    @Override
    public LiveData save(LiveData liveData) {
        return liveDataRepository.save(liveData);
    }

    @Override
    public List<LiveData> findAll() {
        return liveDataRepository.findAll();
    }


}

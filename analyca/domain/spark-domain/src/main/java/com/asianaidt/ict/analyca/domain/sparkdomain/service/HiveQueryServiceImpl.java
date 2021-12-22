package com.asianaidt.ict.analyca.domain.sparkdomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HiveQueryServiceImpl implements HiveQueryService {

    private final JdbcTemplate jdbcTemplate;

    public HiveQueryServiceImpl(@Qualifier("hiveJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getQueryResult(String query) {
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public void queryExecute(String query)
    {
        jdbcTemplate.execute(query);
    }
}

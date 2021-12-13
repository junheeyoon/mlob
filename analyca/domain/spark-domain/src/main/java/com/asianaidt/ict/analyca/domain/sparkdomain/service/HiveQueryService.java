package com.asianaidt.ict.analyca.domain.sparkdomain.service;

import java.util.List;
import java.util.Map;

public interface HiveQueryService {
    List<Map<String, Object>> getQueryResult(String query);
    void queryExecute(String query);
}

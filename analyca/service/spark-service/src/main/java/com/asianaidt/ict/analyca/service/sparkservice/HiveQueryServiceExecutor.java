package com.asianaidt.ict.analyca.service.sparkservice;

import com.asianaidt.ict.analyca.domain.sparkdomain.service.HiveQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HiveQueryServiceExecutor {

    private final HiveQueryService hiveQueryService;

    public HiveQueryServiceExecutor(HiveQueryService hiveQueryService) {
        this.hiveQueryService = hiveQueryService;
    }

    /**
     * getDatabases
     * @return
     */
    public List<Map<String, Object>> getDatabases() {
        return hiveQueryService.getQueryResult("show databases");
    }

    /**
     * get table list for database
     */
    public List<Map<String, Object>> getTables(String databaseName) {
        if (databaseName.equals("")) databaseName = "default";
        String sql = String.format("use %s", databaseName);
        hiveQueryService.queryExecute(sql);

        String sql2 = "show tables";
        return hiveQueryService.getQueryResult(sql2);
    }
}

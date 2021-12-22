package com.asianaidt.ict.analyca.eurekaclient.repository;

import com.asianaidt.ict.analyca.eurekaclient.domain.Crawler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends JpaRepository<Crawler, Long>{
}

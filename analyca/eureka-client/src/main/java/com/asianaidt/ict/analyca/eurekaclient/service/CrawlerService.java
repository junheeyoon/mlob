package com.asianaidt.ict.analyca.eurekaclient.service;

import com.asianaidt.ict.analyca.eurekaclient.domain.Crawler;

import java.util.List;

public interface CrawlerService {

    List<Crawler> findAll();

    String konlpy(String s);
}

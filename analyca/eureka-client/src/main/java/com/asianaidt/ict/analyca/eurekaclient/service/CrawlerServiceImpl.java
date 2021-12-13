package com.asianaidt.ict.analyca.eurekaclient.service;

import com.asianaidt.ict.analyca.eurekaclient.repository.CrawlerRepository;
import com.asianaidt.ict.analyca.eurekaclient.domain.Crawler;
import org.springframework.stereotype.Service;

import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;


@Service
public class CrawlerServiceImpl implements CrawlerService{

    private final CrawlerRepository crawlerRepository;

    public CrawlerServiceImpl(CrawlerRepository crawlerRepository) {
        this.crawlerRepository = crawlerRepository;
    }

    @Override
    public List<Crawler> findAll() {
        return crawlerRepository.findAll();
    }

    @Override
    public String konlpy(String s) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        KomoranResult analyzeResultList = komoran.analyze(s);
        List<Token> tokenList = analyzeResultList.getTokenList();

        String resultString = "";
        for (Token token : tokenList) {
            resultString += token.getMorph();
            resultString += "/";
            resultString += token.getPos();
            resultString += " ";
        }
        return resultString;
    }
}

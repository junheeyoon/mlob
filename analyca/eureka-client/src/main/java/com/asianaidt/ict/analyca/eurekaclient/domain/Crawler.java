package com.asianaidt.ict.analyca.eurekaclient.domain;


import lombok.*;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Entity
@Table(name = "an_ext_crawler")
@Getter
@Setter
public class Crawler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCrawler;

    @Column(length = 100)
    private String channel;

    @Column(length = 4000)
    private String searchKeyword;

    @Column(length = 4000)
    private String contentTitle;

    @Column(length = 4000)
    private String contentText;
}

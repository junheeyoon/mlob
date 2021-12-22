package com.asianaidt.ict.analyca.domain.securitycore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class Authority {
    @Id
    @GeneratedValue
    private Long authorityId;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}

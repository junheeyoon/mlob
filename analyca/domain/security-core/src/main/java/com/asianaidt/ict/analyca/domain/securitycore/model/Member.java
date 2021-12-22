package com.asianaidt.ict.analyca.domain.securitycore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Member {
    public final static String ROLE_DELIMITER = ",";
    @Id
    @GeneratedValue
    @JsonIgnore
    @ToString.Exclude
    private Long memberId;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private LocalDateTime created;
    private LocalDateTime updated;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Authority> authorites = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }

    public void addAuthority(Authority authority) {
        authority.setMember(this);
        this.authorites.add(authority);
    }

    /**
     * 사용자 권한을 이름으로 반환하는 함수.
     * 현재(2020.02.27) 기준으로 사용자는 1개의 Role(Authority)를 갖고 있어 결과는 ROLE_DELIMITER 로 구분되지 않은 하나의 정보가
     *
     * @return ROLE_DELIMITER 로 구분된 사용자 권한 스트링
     */
    public String getRole() {
        return authorites.stream().map(a -> a.getRole().getName()).collect(Collectors.joining(ROLE_DELIMITER));
    }
}

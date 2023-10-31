package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Poop extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Column(name = "clean_yn")
    private Boolean cleanYn;

    public Poop(Member memberId, Boolean cleanYn) {
        this.memberId = memberId;
        this.cleanYn = cleanYn;
    }

    public void update (Boolean cleanYn){
        this.cleanYn = cleanYn;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public Boolean getCleanYn() {
        return cleanYn;
    }

    public void setCleanYn(Boolean cleanYn) {
        this.cleanYn = cleanYn;
    }
}

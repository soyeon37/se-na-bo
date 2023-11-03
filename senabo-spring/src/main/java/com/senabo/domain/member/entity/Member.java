package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import com.senabo.domain.bath.entity.Bath;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.disease.entity.Disease;
import com.senabo.domain.expense.entity.Expense;
import com.senabo.domain.feed.entity.Feed;
import com.senabo.domain.member.dto.request.UpdateInfoRequest;
import com.senabo.domain.poop.entity.Poop;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.walk.entity.Walk;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "species", nullable = false)
    private String species;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "affection")
    private int affection;

    @Column(name = "stress_level")
    private int stressLevel;

    @Column(name = "house_latitude", nullable = false)
    private BigDecimal houseLatitude;

    @Column(name = "house_longitude", nullable = false)
    private BigDecimal houseLongitude;

    @Column(name = "total_time")
    private int totalTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "enter_time")
    private LocalDateTime enterTime;

    @Column(nullable = true)
    private String uid;

    @Column(nullable = true)
    private String deviceToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>(List.of(Role.ROLE_USER));

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrushingTeeth> brushingTeethList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bath> bathList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Communication> communicationList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disease> diseaseList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenseList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feedList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poop> poopList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stress> stressList;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Walk> walkList;



    public Member(String name, String email, String species, String sex, BigDecimal houseLatitude, BigDecimal houseLongitude, String uid, String deviceToken) {
        this.name = name;
        this.email = email;
        this.species = species;
        this.sex = sex;
        this.houseLatitude = houseLatitude;
        this.houseLongitude = houseLongitude;
        this.uid = uid;
        this.deviceToken = deviceToken;
        affection = 0;
        stressLevel = 50;
    }

    public void update(UpdateInfoRequest request){
        this.name = request.name();
        this.sex = request.sex();
        this.species = request.species();
        this.houseLatitude = request.houseLatitude();
        this.houseLongitude = request.houseLongitude();
    }
    public void updateStress(int stressLevel){
        this.stressLevel = stressLevel;
    }


    public void updateAffection(int affection) {
        this.affection = affection;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream()
//                .map(Role::name)
//                .map(SimpleGrantedAuthority::new)
//                .toList();
//    }
}

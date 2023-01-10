package com.footstep.domain.posting.domain.place;

import com.footstep.domain.base.BaseTimeEntity;
import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.posting.Posting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "place_id")
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    private City city;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Posting> postings = new ArrayList<>();

    @Builder
    public Place(String name, String address, Double latitude, Double longitude, City city, Status status) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.status = status;
        this.postings = new ArrayList<>();
    }
}

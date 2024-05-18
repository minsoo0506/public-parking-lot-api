package com.mnsoo.parkinglot.domain.persist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mnsoo.parkinglot.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "image")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filepath;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonBackReference // 순환 참조 방지
    private ReviewEntity review;
}

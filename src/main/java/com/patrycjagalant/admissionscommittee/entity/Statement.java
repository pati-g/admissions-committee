package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "statements", indexes = {
        @Index(name = "request_ID", columnList = "request_ID")
})
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "request_ID")
    @ToString.Exclude
    private ApplicationRequest applicationRequest;

    @NotNull(message = "Please provide a valid number of points (only integers are allowed)")
    @Size(min = 1)
    @Column(name = "points")
    private Integer points;

    @NotBlank
    @Column(name = "enrollment")
    private Character enrollment;

}
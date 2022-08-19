package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @Column(name = "name")
    private String name;

    @Column(name = "budget_places")
    private Integer budgetPlaces;

    @Column(name = "total_places")
    private Integer totalPlaces;

    @OneToMany(mappedBy = "faculty")
    @ToString.Exclude
    private List<EnrollmentRequest> requests;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Faculties_Subjects",
            joinColumns = @JoinColumn(name = "faculty_ID"),
            inverseJoinColumns = @JoinColumn(name = "subject_ID"))

    @ToString.Exclude
    private Set<Subject> subjects;

}
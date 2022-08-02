package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Faculty name is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Budget places number is mandatory")
    @Min(1)
    @Column(name = "budget_places")
    private Integer budgetPlaces;

    @NotBlank(message = "Total places number is mandatory")
    @Min(1)
    @Column(name = "total_places")
    private Integer totalPlaces;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Faculty faculty = (Faculty) o;
        return id != null && Objects.equals(id, faculty.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
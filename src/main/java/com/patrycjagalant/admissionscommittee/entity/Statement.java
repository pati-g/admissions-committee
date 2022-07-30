package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "statements", indexes = {
        @Index(name = "registration_ID", columnList = "registration_ID")
})
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_ID")
    @ToString.Exclude
    private ApplicationRequest applicationRequest;

    @NotNull
    @Size(min = 1)
    @Column(name = "points")
    private Integer points;

    @NotNull
    @Column(name = "enrollment")
    private Character enrollment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Statement statement = (Statement) o;
        return id != null && Objects.equals(id, statement.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
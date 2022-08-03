package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
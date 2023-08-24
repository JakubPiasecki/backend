package pl.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.backend.entity.Skill;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private Date employmentDate;

    @ManyToMany
    private List<Skill> skills;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

}

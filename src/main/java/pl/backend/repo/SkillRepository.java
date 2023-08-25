package pl.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.backend.entity.Skill;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}

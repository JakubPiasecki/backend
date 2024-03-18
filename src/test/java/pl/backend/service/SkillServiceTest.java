package pl.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.backend.entity.Skill;
import pl.backend.repo.SkillRepository;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill skill;

    @BeforeEach
    void setUp() {
        skill = new Skill();
        skill.setId(1);
    }

    @Test
    void fetchAllSkills() {
        // Given
        when(skillRepository.findAll()).thenReturn(Collections.singletonList(skill));

        // When
        List<Skill> result = skillService.getAllSkills();

        // Then
        assertThat(result).hasSize(1).contains(skill);
    }

    @Test
    void fetchThrowsError() {
        // Given
        when(skillRepository.findAll()).thenThrow(new RuntimeException("Unexpected Error"));

        // When & Then
        assertThatThrownBy(() -> skillService.getAllSkills())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected Error");
    }
}

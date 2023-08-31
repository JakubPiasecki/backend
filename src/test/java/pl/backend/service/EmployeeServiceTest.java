package pl.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.backend.dto.EmployeeDTO;
import pl.backend.entity.Employee;
import pl.backend.exception.EmployeeNotFoundException;
import pl.backend.mapper.EmployeeMapper;
import pl.backend.repo.EmployeeRepository;
import pl.backend.repo.SkillRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeMapper = mock(EmployeeMapper.class);
        skillRepository = mock(SkillRepository.class);
        employeeService = new EmployeeService(employeeRepository, skillRepository, employeeMapper);
        employee = new Employee();
        employee.setId(UUID.randomUUID());

        UUID id = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        Date employmentDate = new Date();
        List<Integer> skillsIds = Collections.singletonList(1);
        UUID managerId = UUID.randomUUID();

        employeeDTO = new EmployeeDTO(id, firstName, lastName, employmentDate, skillsIds, managerId);
    }

    @Test
    void testGetById() {
        // Given
        when(employeeRepository.existsById(any(UUID.class))).thenReturn(true);
        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.of(employee));
        when(employeeMapper.entityToDTO(employee)).thenReturn(employeeDTO);

        // When
        EmployeeDTO result = employeeService.getEmployeeById(employee.getId());

        // Then
        assertThat(result).isEqualTo(employeeDTO);
    }

    @Test
    void testGetByInvalidId() {
        // Given
        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeService.getEmployeeById(UUID.randomUUID()))
                .isInstanceOf(EmployeeNotFoundException.class);
    }
}

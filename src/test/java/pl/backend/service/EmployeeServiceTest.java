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
    void shouldReturnEmployeeById() {
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
    void shouldThrowEmployeeNotFoundExceptionForInvalidId() {
        // Given
        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeService.getEmployeeById(UUID.randomUUID()))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenUpdatingNonexistentEmployee() {
        // Given
        UUID nonExistentEmployeeId = UUID.randomUUID();
        when(employeeRepository.existsById(nonExistentEmployeeId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> employeeService.updateEmployee(nonExistentEmployeeId, employeeDTO))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldReturnAllEmployees() {
        // Given
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<EmployeeDTO> result = employeeService.getAllEmployees();

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteEmployee() {
        // Given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // When
        employeeService.deleteEmployee(employeeId);

        // Then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenDeletingNonexistentEmployee() {
        // Given
        UUID nonExistentEmployeeId = UUID.randomUUID();
        when(employeeRepository.existsById(nonExistentEmployeeId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> employeeService.deleteEmployee(nonExistentEmployeeId))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldReturnEmployeesByName() {
        // Given
        String name = "John";
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findByFirstNameIgnoreCaseContaining(name)).thenReturn(employees);

        // When
        List<EmployeeDTO> result = employeeService.getEmployeesByName(name);

        // Then
        assertThat(result).hasSize(2);
    }
}

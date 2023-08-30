package pl.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.backend.dto.EmployeeDTO;
import pl.backend.entity.Employee;
import pl.backend.entity.Skill;
import pl.backend.exception.EmployeeBadRequestException;
import pl.backend.exception.EmployeeNotFoundException;
import pl.backend.exception.MissingFirstNameException;
import pl.backend.exception.MissingLastNameException;
import pl.backend.mapper.EmployeeMapper;
import pl.backend.repo.EmployeeRepository;
import pl.backend.repo.SkillRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final SkillRepository skillRepository;

    public EmployeeService(EmployeeRepository employeeRepository, SkillRepository skillRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(UUID id) {
        checkEmployeeExistsById(id);
        Employee employee = employeeRepository.findById(id).orElse(null);
        return employeeMapper.entityToDTO(employee);
    }

    @Transactional
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.dtoToEntity(employeeDTO);
        populateEmployeeFields(employee, employeeDTO);
        validateEmployee(employee);
        return employeeMapper.entityToDTO(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO updatedEmployeeDTO) {
        checkEmployeeExistsById(id);
        Employee updatedEmployee = employeeMapper.dtoToEntity(updatedEmployeeDTO);
        validateIdConsistency(id, updatedEmployee.getId());
        populateEmployeeFields(updatedEmployee, updatedEmployeeDTO);
        validateEmployee(updatedEmployee);
        updatedEmployee.setId(id);
        return employeeMapper.entityToDTO(employeeRepository.save(updatedEmployee));
    }

    public void deleteEmployee(UUID id) {
        checkEmployeeExistsById(id);
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDTO> getEmployeesByName(String name) {
        return employeeRepository.findByFirstNameIgnoreCaseContaining(name).stream()
                .map(employeeMapper::entityToDTO)
                .collect(Collectors.toList());
    }


    private void populateEmployeeFields(Employee employee, EmployeeDTO employeeDTO) {
        List<Skill> skills = skillRepository.findAllById(employeeDTO.skillsIds());
        employee.setSkills(skills);
        employee.setManager(employeeRepository.findById(employeeDTO.managerId()).orElse(null));
    }

    private void validateEmployee(Employee employee) {
        if (employee.getFirstName() == null) {
            throw new MissingFirstNameException("First name is required");
        }
        if (employee.getLastName() == null) {
            throw new MissingLastNameException("Last name is required");
        }
    }

    private void checkEmployeeExistsById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
    }

    private void validateIdConsistency(UUID pathId, UUID bodyId) {
        if (bodyId != null && !pathId.equals(bodyId)) {
            throw new EmployeeBadRequestException("Inconsistent IDs between path and body");
        }
    }
}


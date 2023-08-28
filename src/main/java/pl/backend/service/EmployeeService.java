package pl.backend.service;

import org.springframework.stereotype.Service;
import pl.backend.entity.Employee;
import pl.backend.exception.*;
import pl.backend.repo.EmployeeRepository;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        checkEmployeeExistsById(id);
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee addEmployee(Employee employee) {
        validateEmployee(employee);
        UUID managerId = employee.getManager().getId();
        if (managerId != null) {
            Employee manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Manager with id " + managerId + " not found"));
            employee.setManager(manager);
        }
        return employeeRepository.save(employee);
    }


    public Employee updateEmployee(UUID id, Employee updatedEmployee) {
        validateEmployee(updatedEmployee);
        checkEmployeeExistsById(id);
        validateIdConsistency(id, updatedEmployee.getId());

        updatedEmployee.setId(id);
        return employeeRepository.save(updatedEmployee);
    }


    public void deleteEmployee(UUID id) {
        checkEmployeeExistsById(id);
        employeeRepository.deleteById(id);
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

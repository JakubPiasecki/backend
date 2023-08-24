package pl.backend.service;

import org.springframework.stereotype.Service;
import pl.backend.entity.Employee;
import pl.backend.exception.EmployeeAlreadyExistsException;
import pl.backend.exception.EmployeeBadRequestException;
import pl.backend.exception.EmployeeNotFoundException;
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
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    public Employee addEmployee(Employee employee) {
        if (employee.getId() != null && employeeRepository.existsById(employee.getId())) {
            throw new EmployeeAlreadyExistsException("Employee with id " + employee.getId() + " already exists");
        }
        if (employee.getFirstName() == null || employee.getLastName() == null) {
            throw new EmployeeBadRequestException("Missing required fields");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(UUID id, Employee updatedEmployee) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        if (updatedEmployee.getFirstName() == null || updatedEmployee.getLastName() == null) {
            throw new EmployeeBadRequestException("Missing required fields");
        }
        updatedEmployee.setId(id);
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        employeeRepository.deleteById(id);
    }
}



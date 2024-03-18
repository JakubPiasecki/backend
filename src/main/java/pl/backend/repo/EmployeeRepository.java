package pl.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.backend.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByFirstNameIgnoreCaseContaining(String name);
}

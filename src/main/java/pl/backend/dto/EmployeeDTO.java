package pl.backend.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public record EmployeeDTO(
        UUID id,
        String firstName,
        String lastName,
        Date employmentDate,
        List<Integer> skillsIds,
        UUID managerId) {

}
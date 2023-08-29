package pl.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import pl.backend.dto.EmployeeDTO;
import pl.backend.entity.Employee;
import pl.backend.entity.Skill;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class EmployeeMapper {

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "skillsIds", source = "skills", qualifiedByName = "getSkillId")
    public abstract EmployeeDTO entityToDTO(Employee employee);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "skills", ignore = true)
    public abstract Employee dtoToEntity(EmployeeDTO employeeDTO);

    @Named("getSkillId")
    public List<Integer> getSkillId(List<Skill> skills) {
        return skills.stream().map(Skill::getId).toList();
    }
}

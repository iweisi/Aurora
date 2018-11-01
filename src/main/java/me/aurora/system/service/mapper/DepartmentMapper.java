package me.aurora.system.service.mapper;

import me.aurora.common.mapper.EntityMapper;
import me.aurora.system.domain.Department;
import me.aurora.system.service.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author 郑杰
 * @date 2018/10/25 11:35:05
 */
@Mapper(componentModel = "spring",uses = {RoleMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {

    /**
     * 自定义转换
     * @param menu
     * @param rolesSelect
     * @return
     */
    @Mapping(source = "rolesSelect",target = "rolesSelect")
    DepartmentDTO toDto(Department menu, String rolesSelect);

}

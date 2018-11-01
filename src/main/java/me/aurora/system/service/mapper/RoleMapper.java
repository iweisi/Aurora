package me.aurora.system.service.mapper;

import me.aurora.common.mapper.EntityMapper;
import me.aurora.system.domain.Role;
import me.aurora.system.service.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 郑杰
 * @date 2018/08/27 12:34:22
 */
@Mapper(componentModel = "spring",uses = {PerMissionMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

}

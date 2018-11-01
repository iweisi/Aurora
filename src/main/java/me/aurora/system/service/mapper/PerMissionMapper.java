package me.aurora.system.service.mapper;

import me.aurora.common.mapper.EntityMapper;
import me.aurora.system.domain.Permission;
import me.aurora.system.service.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 郑杰
 * @date 2018/08/27 13:05:57
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PerMissionMapper extends EntityMapper<PermissionDTO, Permission> {
}

package me.aurora.monitor.service.mapper;

import me.aurora.monitor.domain.SysLog;
import me.aurora.monitor.service.dto.SysLogDTO;
import me.aurora.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 郑杰
 * @date 2018/08/28 7:06:05
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysLogMapper extends EntityMapper<SysLogDTO, SysLog> {

}

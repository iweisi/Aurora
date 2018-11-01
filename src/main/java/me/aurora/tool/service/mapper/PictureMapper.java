package me.aurora.tool.service.mapper;

import me.aurora.common.mapper.EntityMapper;
import me.aurora.tool.domain.Picture;
import me.aurora.tool.service.dto.PictureDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author 郑杰
 * @date 2018/09/20 14:09:43
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PictureMapper extends EntityMapper<PictureDto, Picture> {

    /**
     * 转DTO
     * @param picture
     * @return
     */
    @Mapping(target = "userName",source = "picture.user.username")
    @Override
    PictureDto toDto(Picture picture);
}

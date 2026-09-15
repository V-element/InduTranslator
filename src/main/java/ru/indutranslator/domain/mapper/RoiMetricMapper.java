package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.RoiMetricDto;
import ru.indutranslator.domain.entity.enterprise.RoiMetric;

@Mapper(componentModel = "spring")
public interface RoiMetricMapper {

    RoiMetricMapper INSTANCE = Mappers.getMapper(RoiMetricMapper.class);

    RoiMetricDto toDto(RoiMetric metric);
    RoiMetric toEntity(RoiMetricDto metricDto);
}

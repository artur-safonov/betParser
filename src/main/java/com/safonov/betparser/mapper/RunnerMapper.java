package com.safonov.betparser.mapper;

import com.safonov.betparser.dto.RunnerData;
import com.safonov.schema.Runner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RunnerMapper {

    @Mapping(source = "price", target = "coefficient")
    RunnerData runnerToRunnerData(Runner runner);

}

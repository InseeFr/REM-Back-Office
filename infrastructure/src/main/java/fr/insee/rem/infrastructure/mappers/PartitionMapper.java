package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.PartitionDto;
import fr.insee.rem.infrastructure.entity.PartitionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PartitionMapper {

    PartitionMapper INSTANCE = Mappers.getMapper(PartitionMapper.class);

    @Mapping(target = "partitionSurveyUnitLinkEntities", ignore = true)
    PartitionEntity dtoToEntity(PartitionDto partition);

    PartitionDto entityToDto(PartitionEntity partitionEntity);

    List<PartitionDto> listEntityToListDto(List<PartitionEntity> partitionEntities);

    List<PartitionEntity> listDtoToListEntity(List<PartitionDto> partitions);
}

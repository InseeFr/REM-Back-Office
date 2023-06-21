package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.PartitionSurveyUnitLinkDto;
import fr.insee.rem.infrastructure.entity.PartitionSurveyUnitLinkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        PartitionMapper.class,
        SurveyUnitMapper.class
})
public interface PartitionSurveyUnitLinkMapper {

    PartitionSurveyUnitLinkMapper INSTANCE = Mappers.getMapper(PartitionSurveyUnitLinkMapper.class);

    @Mapping(
            target = "id", expression = "java(new fr.insee.rem.infrastructure.entity.PartitionSurveyUnitLinkPK" +
            "(partitionSurveyUnitLink.getPartition().getPartitionId(), partitionSurveyUnitLink" +
            ".getSurveyUnit()" +
            ".getRepositoryId()))")
    PartitionSurveyUnitLinkEntity dtoToEntity(PartitionSurveyUnitLinkDto partitionSurveyUnitLink);

    PartitionSurveyUnitLinkDto entityToDto(PartitionSurveyUnitLinkEntity partitionSurveyUnitLinkEntity);

    List<PartitionSurveyUnitLinkDto> listEntityToListDto(List<PartitionSurveyUnitLinkEntity> partitionSurveyUnitLinkEntities);

    List<PartitionSurveyUnitLinkEntity> listDtoToListEntity(List<PartitionSurveyUnitLinkDto> partitionSurveyUnitLinks);
}

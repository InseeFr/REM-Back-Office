package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.EmailDto;
import fr.insee.rem.infrastructure.entity.Email;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmailMapper {

    EmailMapper INSTANCE = Mappers.getMapper(EmailMapper.class);

    Email dtoToEntity(EmailDto email);

    EmailDto entityToDto(Email email);

    List<EmailDto> listEntityToListDto(List<Email> emails);

    List<Email> listDtoToListEntity(List<EmailDto> emails);
}

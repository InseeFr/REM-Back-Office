package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.EmailDto;
import fr.insee.rem.infrastructure.entity.Email;

@Mapper
public interface EmailMapper {

    EmailMapper INSTANCE = Mappers.getMapper(EmailMapper.class);

    Email dtoToEntity(EmailDto emailDto);

    EmailDto entityToDto(Email email);

    List<EmailDto> listEntityToListDto(List<Email> emails);

    List<Email> listDtoToListEntity(List<EmailDto> emailsDto);
}

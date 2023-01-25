package fr.insee.rem.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import fr.insee.rem.domain.dtos.PersonDto;
import fr.insee.rem.infrastructure.entity.Person;

@Mapper(uses = {
    AddressMapper.class, PhoneNumberMapper.class, EmailMapper.class
})
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto entityToDto(Person person);

    Person dtoToEntity(PersonDto personDto);

    List<PersonDto> listEntityToListDto(List<Person> persons);

    List<Person> listDtoToListEntity(List<PersonDto> personsDto);
}

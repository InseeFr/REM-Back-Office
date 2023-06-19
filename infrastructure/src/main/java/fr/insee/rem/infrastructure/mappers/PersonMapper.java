package fr.insee.rem.infrastructure.mappers;

import fr.insee.rem.domain.dtos.PersonDto;
import fr.insee.rem.infrastructure.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        AddressMapper.class,
        PhoneNumberMapper.class,
        EmailMapper.class
})
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto entityToDto(Person person);

    Person dtoToEntity(PersonDto person);

    List<PersonDto> listEntityToListDto(List<Person> persons);

    List<Person> listDtoToListEntity(List<PersonDto> persons);
}

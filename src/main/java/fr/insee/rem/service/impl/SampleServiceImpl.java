package fr.insee.rem.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import fr.insee.rem.dto.SampleDto;
import fr.insee.rem.dto.SuIdsDto;
import fr.insee.rem.dto.SurveyUnitCsvDto;
import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SampleSurveyUnitPK;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleAlreadyExistsException;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.repository.SampleRepository;
import fr.insee.rem.repository.SampleSurveyUnitRepository;
import fr.insee.rem.repository.SurveyUnitRepository;
import fr.insee.rem.service.SampleService;
import fr.insee.rem.service.SurveyUnitService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SampleServiceImpl implements SampleService {

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    SurveyUnitService surveyUnitService;

    @Autowired
    SampleSurveyUnitRepository sampleSurveyUnitRepository;

    @Autowired
    SurveyUnitRepository surveyUnitRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Response addSampleFromCSVFile(Long sampleId, MultipartFile sampleFile) throws SampleNotFoundException, CsvFileException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        if (sampleFile.isEmpty()) {
            throw new CsvFileException(String.format("File {%s} doesn't exist or is empty", sampleFile.getOriginalFilename()));
        }

        Sample sample = findSample.get();

        try (Reader reader = new BufferedReader(new InputStreamReader(sampleFile.getInputStream()))) {

            CsvToBean<SurveyUnitCsvDto> csvToBean =
                new CsvToBeanBuilder<SurveyUnitCsvDto>(reader).withType(SurveyUnitCsvDto.class).withSeparator(';').withIgnoreLeadingWhiteSpace(true)
                    .withEscapeChar('\0').withThrowExceptions(false).build();

            List<SurveyUnitCsvDto> surveyUnitsDto = csvToBean.parse();

            if ( !csvToBean.getCapturedExceptions().isEmpty()) {
                csvToBean.getCapturedExceptions().stream().forEach(e -> log.error(e.getMessage(), e));
                throw new CsvFileException("File read error");
            }

            for (SurveyUnitCsvDto suDto : surveyUnitsDto) {
                SurveyUnit su = new SurveyUnit(suDto);
                SampleSurveyUnit ssu = new SampleSurveyUnit(sample, su, suDto.getPoleGestionOpale(), suDto.getAffectationIdep());
                entityManager.persist(ssu);
            }

            return new Response(String.format("%s surveyUnits created", surveyUnitsDto.size()), HttpStatus.OK);
        }
        catch (Exception e) {
            throw new CsvFileException("File read error", e);
        }

    }

    @Override
    public Response deleteSample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        Sample sample = findSample.get();
        List<SampleSurveyUnitPK> ssuPKList = sampleSurveyUnitRepository.findBySample(sample).stream().map(SampleSurveyUnit::getId).toList();
        sampleSurveyUnitRepository.deleteAllById(ssuPKList);
        sampleRepository.delete(sample);
        return new Response(String.format("sample %s deleted", sampleId), HttpStatus.OK);
    }

    @Override
    public List<SurveyUnitDto> getSurveyUnitsBySample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }

        List<SampleSurveyUnit> ssuList = sampleSurveyUnitRepository.findBySampleWithSurveyUnit(findSample.get());
        return ssuList.stream().map(ssu -> new SurveyUnitDto(ssu.getSurveyUnit().getId(), ssu.getSurveyUnit().getSurveyUnitData(), ssu.getAffectationIdep(),
            ssu.getPoleGestionOpale(), ssu.getRegisteredDate())).toList();
    }

    @Override
    public SampleDto putSample(String label) throws SampleAlreadyExistsException {
        Optional<Sample> findSample = sampleRepository.findByLabel(label);
        if (findSample.isPresent()) {
            throw new SampleAlreadyExistsException(label);
        }
        Sample sample = new Sample();
        sample.setLabel(label);
        sample = sampleRepository.save(sample);
        return new SampleDto(sample.getId(), sample.getLabel());
    }

    @Override
    public SampleDto getSample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        Sample s = findSample.get();
        return new SampleDto(s.getId(), s.getLabel());
    }

    @Override
    public List<SampleDto> getAllSamples() {
        List<Sample> samples = sampleRepository.findAll();
        return samples.stream().map(s -> new SampleDto(s.getId(), s.getLabel())).toList();
    }

    @Override
    public SuIdsDto getListOfIds(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        Sample sample = findSample.get();
        List<Long> surveyUnitsIds = sampleSurveyUnitRepository.findAllIdsBySample(sampleId);
        return new SuIdsDto(sampleId, sample.getLabel(), surveyUnitsIds);
    }

}

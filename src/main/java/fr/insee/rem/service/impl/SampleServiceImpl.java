package fr.insee.rem.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import fr.insee.rem.dto.SampleDto;
import fr.insee.rem.dto.SurveyUnitCsvDto;
import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.repository.SampleRepository;
import fr.insee.rem.repository.SampleSurveyUnitRepository;
import fr.insee.rem.repository.SurveyUnitRepository;
import fr.insee.rem.service.SampleService;
import fr.insee.rem.service.SurveyUnitService;

@Service
@Transactional
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
                    .withEscapeChar('\0').build();

            List<SurveyUnitCsvDto> surveyUnitsDto = csvToBean.parse();

            List<SurveyUnit> surveyUnits = surveyUnitsDto.stream().map(SurveyUnit::new).collect(Collectors.toList());

            surveyUnitRepository.saveAll(surveyUnits);

            for (SurveyUnit su : surveyUnits) {
                SampleSurveyUnit sampleSurveyUnit = new SampleSurveyUnit(sample, su);
                entityManager.persist(sampleSurveyUnit); // force inserts
            }

            return new Response(String.format("%s surveyUnits created", surveyUnitsDto.size()), HttpStatus.OK);
        }
        catch (IOException e) {
            throw new CsvFileException("File read error", e);
        }

    }

    @Override
    public Response deleteSample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        sampleSurveyUnitRepository.deleteAll(sampleSurveyUnitRepository.findBySample(findSample.get()));
        sampleRepository.delete(findSample.get());
        return new Response(String.format("sample %s deleted", sampleId), HttpStatus.OK);
    }

    @Override
    public List<SurveyUnitDto> getSurveyUnitsBySample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        List<SampleSurveyUnit> sampleSurveyUnits = sampleSurveyUnitRepository.findBySample(findSample.get());
        return sampleSurveyUnits.stream().map(SampleSurveyUnit::getSurveyUnit).map(su -> new SurveyUnitDto(su.getId(), su.getSurveyUnitData()))
            .collect(Collectors.toList());
    }

    @Override
    public SampleDto putSample(String label) {
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
        return samples.stream().map(s -> new SampleDto(s.getId(), s.getLabel())).collect(Collectors.toList());
    }

}

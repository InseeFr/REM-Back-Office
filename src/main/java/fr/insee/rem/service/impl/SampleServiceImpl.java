package fr.insee.rem.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

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

            CsvToBean<SurveyUnitDto> csvToBean =
                new CsvToBeanBuilder<SurveyUnitDto>(reader).withType(SurveyUnitDto.class).withSeparator(';').withIgnoreLeadingWhiteSpace(true)
                    .withEscapeChar('\0').build();

            List<SurveyUnitDto> surveyUnitsDto = csvToBean.parse();

            List<SurveyUnit> surveyUnits = surveyUnitsDto.stream().map(SurveyUnit::new).collect(Collectors.toList());

            List<SampleSurveyUnit> sampleSurveyUnits = new ArrayList<>();

            for (SurveyUnit su : surveyUnits) {
                SampleSurveyUnit sampleSurveyUnit = new SampleSurveyUnit(sample, su);
                sampleSurveyUnits.add(sampleSurveyUnit);
            }
            surveyUnitRepository.saveAll(surveyUnits);
            sampleSurveyUnitRepository.saveAll(sampleSurveyUnits);

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
        List<SampleSurveyUnit> sampleSurveyUnits = sampleSurveyUnitRepository.findBySample(findSample.get());
        List<SurveyUnit> suWithSingleSsu =
            sampleSurveyUnits.stream().filter(ssu -> ssu.getSurveyUnit().getSampleSurveyUnit().size() == 1).map(SampleSurveyUnit::getSurveyUnit)
                .collect(Collectors.toList());

        sampleSurveyUnitRepository.deleteAll(sampleSurveyUnits);
        surveyUnitRepository.deleteAll(suWithSingleSsu);
        return new Response(String.format("sample %s deleted", sampleId), HttpStatus.OK);
    }

    @Override
    public List<SurveyUnit> getSurveyUnitsBySample(Long sampleId) {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if (findSample.isPresent()) {
            return surveyUnitRepository.findAllBySample(findSample.get());
        }
        else {
            return Collections.emptyList();
        }

    }

    @Override
    public Response putSample(String label) {
        Sample sample = new Sample();
        sample.setLabel(label);
        sample = sampleRepository.save(sample);
        return new Response(String.format("sample %s create", sample.getId()), HttpStatus.OK);
    }

    @Override
    public Sample getSample(Long sampleId) throws SampleNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        return findSample.get();
    }

    @Override
    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

}

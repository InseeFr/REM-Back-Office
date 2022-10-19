package fr.insee.rem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;
import fr.insee.rem.repository.SampleRepository;
import fr.insee.rem.repository.SampleSurveyUnitRepository;
import fr.insee.rem.repository.SurveyUnitRepository;
import fr.insee.rem.service.SurveyUnitService;

@Service
@Transactional
public class SurveyUnitServiceImpl implements SurveyUnitService {

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    SurveyUnitRepository surveyUnitRepository;

    @Autowired
    SampleSurveyUnitRepository sampleSurveyUnitRepository;

    @Override
    public Response addSurveyUnitToSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        Optional<SurveyUnit> findSurveyUnit = surveyUnitRepository.findById(surveyUnitId);
        if ( !findSurveyUnit.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        SampleSurveyUnit sampleSurveyUnit = new SampleSurveyUnit(findSample.get(), findSurveyUnit.get());
        sampleSurveyUnitRepository.save(sampleSurveyUnit);
        return new Response(String.format("SurveyUnit %s add to sample %s", surveyUnitId, sampleId), HttpStatus.OK);
    }

    @Override
    public Response addSurveyUnitsToSample(List<Long> surveyUnitIds, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        List<SampleSurveyUnit> sampleSurveyUnits = new ArrayList<>();
        for (Long id : surveyUnitIds) {
            Optional<SurveyUnit> findSurveyUnit = surveyUnitRepository.findById(id);
            if ( !findSurveyUnit.isPresent()) {
                throw new SurveyUnitNotFoundException(id);
            }
            SampleSurveyUnit sampleSurveyUnit = new SampleSurveyUnit(findSample.get(), findSurveyUnit.get());
            sampleSurveyUnits.add(sampleSurveyUnit);
        }
        sampleSurveyUnitRepository.saveAll(sampleSurveyUnits);
        return new Response(String.format("%s surveyUnits added to sample %s", surveyUnitIds.size(), sampleId), HttpStatus.OK);
    }

}

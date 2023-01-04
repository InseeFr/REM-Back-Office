package fr.insee.rem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.Sample;
import fr.insee.rem.entities.SampleSurveyUnit;
import fr.insee.rem.entities.SampleSurveyUnitPK;
import fr.insee.rem.entities.SurveyUnit;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SampleSurveyUnitNotFoundException;
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
        SampleSurveyUnit sampleSurveyUnit = new SampleSurveyUnit(findSample.get(), findSurveyUnit.get(), null, null);
        sampleSurveyUnitRepository.save(sampleSurveyUnit);
        return new Response(String.format("SurveyUnit %s add to sample %s", surveyUnitId, sampleId), HttpStatus.OK);
    }

    @Override
    public Response deleteSurveyUnit(Long surveyUnitId) throws SurveyUnitNotFoundException {
        Optional<SurveyUnit> findSurveyUnit = surveyUnitRepository.findById(surveyUnitId);
        if ( !findSurveyUnit.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        SurveyUnit su = findSurveyUnit.get();
        List<SampleSurveyUnit> sampleSurveyUnits = sampleSurveyUnitRepository.findBySurveyUnit(su);
        sampleSurveyUnitRepository.deleteAll(sampleSurveyUnits);
        surveyUnitRepository.delete(su);
        return new Response(String.format("SurveyUnit %s deleted", surveyUnitId), HttpStatus.OK);
    }

    @Override
    public Response removeSurveyUnitFromSample(Long surveyUnitId, Long sampleId) throws SampleNotFoundException, SurveyUnitNotFoundException {
        Optional<Sample> findSample = sampleRepository.findById(sampleId);
        if ( !findSample.isPresent()) {
            throw new SampleNotFoundException(sampleId);
        }
        Optional<SurveyUnit> findSurveyUnit = surveyUnitRepository.findById(surveyUnitId);
        if ( !findSurveyUnit.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        SampleSurveyUnitPK ssuPK = new SampleSurveyUnitPK(sampleId, surveyUnitId);
        sampleSurveyUnitRepository.deleteById(ssuPK);
        return new Response(String.format("SurveyUnit %s removed from sample %s", surveyUnitId, sampleId), HttpStatus.OK);
    }

    @Override
    public SurveyUnitDto getSurveyUnit(Long surveyUnitId, Long sampleId) throws SurveyUnitNotFoundException, SampleSurveyUnitNotFoundException {
        Optional<SurveyUnit> findSurveyUnit = surveyUnitRepository.findById(surveyUnitId);
        if ( !findSurveyUnit.isPresent()) {
            throw new SurveyUnitNotFoundException(surveyUnitId);
        }
        Optional<SampleSurveyUnit> findSampleSurveyUnit = sampleSurveyUnitRepository.findById(new SampleSurveyUnitPK(sampleId, surveyUnitId));
        if ( !findSampleSurveyUnit.isPresent()) {
            throw new SampleSurveyUnitNotFoundException(sampleId, surveyUnitId);
        }
        SurveyUnit su = findSurveyUnit.get();
        SampleSurveyUnit ssu = findSampleSurveyUnit.get();
        return new SurveyUnitDto(su.getId(), su.getSurveyUnitData(), ssu.getPoleGestionOpale(), ssu.getAffectationIdep(), ssu.getRegisteredDate());
    }

}

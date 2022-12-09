package fr.insee.rem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.insee.rem.config.DefaultSecurityConfiguration;
import fr.insee.rem.entities.Response;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.exception.SurveyUnitNotFoundException;
import fr.insee.rem.service.SurveyUnitService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SurveyUnitController.class)
@Import(DefaultSecurityConfiguration.class)
class SurveyUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SurveyUnitService surveyUnitService;

    @Test
    void addSurveyUnitToSample_success() throws Exception {
        Mockito.when(surveyUnitService.addSurveyUnitToSample(1l, 1l)).thenReturn(new Response("It's a test", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("/survey-unit/1/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void addSurveyUnitToSample_error_404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(surveyUnitService.addSurveyUnitToSample(1l, 1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.post("/survey-unit/1/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        SurveyUnitNotFoundException ex2 = new SurveyUnitNotFoundException(2l);
        Mockito.when(surveyUnitService.addSurveyUnitToSample(2l, 2l)).thenThrow(ex2);
        mockMvc.perform(MockMvcRequestBuilders.post("/survey-unit/2/sample/2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void deleteSurveyUnitToSample_success() throws Exception {
        Mockito.when(surveyUnitService.deleteSurveyUnit(1l)).thenReturn(new Response("It's a test", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.delete("/survey-unit/1")).andExpect(status().isOk());
    }

    @Test
    void deleteSurveyUnitToSample_error_404() throws Exception {
        SurveyUnitNotFoundException ex = new SurveyUnitNotFoundException(1l);
        Mockito.when(surveyUnitService.deleteSurveyUnit(1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.delete("/survey-unit/1")).andExpect(status().isNotFound());
    }

    @Test
    void removeSurveyUnitFromSample_success() throws Exception {
        Mockito.when(surveyUnitService.removeSurveyUnitFromSample(1l, 1l)).thenReturn(new Response("It's a test", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.delete("/survey-unit/1/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void removeSurveyUnitFromSample_error_404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(surveyUnitService.removeSurveyUnitFromSample(1l, 1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.delete("/survey-unit/1/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        SurveyUnitNotFoundException ex2 = new SurveyUnitNotFoundException(2l);
        Mockito.when(surveyUnitService.removeSurveyUnitFromSample(2l, 2l)).thenThrow(ex2);
        mockMvc.perform(MockMvcRequestBuilders.delete("/survey-unit/2/sample/2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

}

package fr.insee.rem.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.insee.rem.config.DefaultSecurityConfiguration;
import fr.insee.rem.dto.SampleDto;
import fr.insee.rem.dto.SuIdsDto;
import fr.insee.rem.dto.SurveyUnitDto;
import fr.insee.rem.entities.Response;
import fr.insee.rem.entities.SurveyUnitData;
import fr.insee.rem.exception.CsvFileException;
import fr.insee.rem.exception.SampleAlreadyExistsException;
import fr.insee.rem.exception.SampleNotFoundException;
import fr.insee.rem.service.SampleService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SampleController.class)
@Import(DefaultSecurityConfiguration.class)
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SampleService sampleService;

    @Test
    void getAllSamples_success() throws Exception {
        SampleDto sample1 = new SampleDto(1l, "Sample1");
        SampleDto sample2 = new SampleDto(2l, "Sample2");
        SampleDto sample3 = new SampleDto(3l, "Sample3");
        List<SampleDto> samples = new ArrayList<>(Arrays.asList(sample1, sample2, sample3));
        Mockito.when(sampleService.getAllSamples()).thenReturn(samples);
        mockMvc.perform(MockMvcRequestBuilders.get("/sample/all").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[2].label", is("Sample3")));
    }

    @Test
    void getSample_success() throws Exception {
        SampleDto sample1 = new SampleDto(1l, "Sample1");
        Mockito.when(sampleService.getSample(1l)).thenReturn(sample1);
        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.label", is("Sample1")));
    }

    @Test
    void putSample_success() throws Exception {
        SampleDto sample1 = new SampleDto(1l, "Sample1");
        Mockito.when(sampleService.putSample("Sample1")).thenReturn(sample1);
        mockMvc
            .perform(MockMvcRequestBuilders.put("/sample/create").contentType(MediaType.TEXT_PLAIN_VALUE).accept(MediaType.APPLICATION_JSON).content("Sample1"))
            .andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.label", is("Sample1")));
    }

    @Test
    void getListOfIds_success() throws Exception {
        SuIdsDto suIdsDto = new SuIdsDto(1l, "test", Arrays.asList(1l, 2l, 3l));
        Mockito.when(sampleService.getListOfIds(1l)).thenReturn(suIdsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1/survey-units-ids").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.listOfIds", hasSize(3))).andExpect(jsonPath("$.listOfIds[1]", is(2)));
    }

    @Test
    void putSample_error_409() throws Exception {
        SampleAlreadyExistsException ex = new SampleAlreadyExistsException("sample");
        Mockito.when(sampleService.putSample("sample")).thenThrow(ex);
        mockMvc
            .perform(MockMvcRequestBuilders.put("/sample/create").contentType(MediaType.TEXT_PLAIN_VALUE).accept(MediaType.APPLICATION_JSON).content("sample"))
            .andExpect(status().isConflict());
    }

    @Test
    void addSampleFromCSVFile_success() throws Exception {
        MockMultipartFile sampleFile = new MockMultipartFile("sample", "samplefile.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "It's a test".getBytes());
        Mockito.when(sampleService.addSampleFromCSVFile(1l, sampleFile)).thenReturn(new Response("It's a test", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/sample/1/addFromCSVFile").file(sampleFile)).andExpect(status().isOk());
    }

    @Test
    void deleteSample_success() throws Exception {
        Mockito.when(sampleService.deleteSample(1l)).thenReturn(new Response("It's a test", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.delete("/sample/1")).andExpect(status().isOk());
    }

    @Test
    void getSurveyUnitsBySample_success() throws Exception {
        SurveyUnitData suData1 = new SurveyUnitData();
        suData1.setIdentUnite(111111l);
        SurveyUnitData suData2 = new SurveyUnitData();
        suData2.setIdentUnite(222222l);
        SurveyUnitData suData3 = new SurveyUnitData();
        suData3.setIdentUnite(333333l);
        SurveyUnitDto su1 = new SurveyUnitDto(1l, suData1, null, null, new Date());
        SurveyUnitDto su2 = new SurveyUnitDto(2l, suData2, null, null, new Date());
        SurveyUnitDto su3 = new SurveyUnitDto(3l, suData3, null, null, new Date());
        List<SurveyUnitDto> suDtos = new ArrayList<>(Arrays.asList(su1, su2, su3));
        Mockito.when(sampleService.getSurveyUnitsBySample(1l)).thenReturn(suDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1/survey-units").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[1].identUnite", is(222222)));
    }

    @Test
    void addSampleFromCSVFile_error_400() throws Exception {
        MockMultipartFile sampleFile = new MockMultipartFile("sample", "samplefile.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "It's a test".getBytes());
        CsvFileException ex = new CsvFileException(String.format("File {%s} doesn't exist or is empty", sampleFile.getOriginalFilename()));
        Mockito.when(sampleService.addSampleFromCSVFile(1l, sampleFile)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/sample/1/addFromCSVFile").file(sampleFile)).andExpect(status().isBadRequest());
    }

    @Test
    void addSampleFromCSVFile_error_404() throws Exception {
        MockMultipartFile sampleFile = new MockMultipartFile("sample", "samplefile.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "It's a test".getBytes());
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(sampleService.addSampleFromCSVFile(1l, sampleFile)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/sample/1/addFromCSVFile").file(sampleFile)).andExpect(status().isNotFound());
    }

    @Test
    void deleteSample_error_404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(sampleService.deleteSample(1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.delete("/sample/1")).andExpect(status().isNotFound());
    }

    @Test
    void getSample_error_404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(sampleService.getSample(1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void getSurveyUnitsBySample_error_404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(sampleService.getSurveyUnitsBySample(1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1/survey-units").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void getListOfIds_erreur404() throws Exception {
        SampleNotFoundException ex = new SampleNotFoundException(1l);
        Mockito.when(sampleService.getListOfIds(1l)).thenThrow(ex);
        mockMvc.perform(MockMvcRequestBuilders.get("/sample/1/survey-units-ids").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

}

package com.restdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.restdemo.entity.CloudVendor;
import com.restdemo.exception.CloudVendorNotFoundException;
import com.restdemo.exception.InternalServerException;
import com.restdemo.service.CloudVendorService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CloudVendorController.class)
class CloudVendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudVendorService cloudVendorService;

    CloudVendor cloudVendorOne;
    CloudVendor cloudVendorTwo;
    List<CloudVendor> cloudVendorList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        cloudVendorOne = new CloudVendor("1", "Amazon", "USA", "1234567890");
        cloudVendorTwo = new CloudVendor("2", "GCP", "UK", "1234567890");
        cloudVendorList.add(cloudVendorOne);
        cloudVendorList.add(cloudVendorTwo);
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("Test Get Cloud Vendor By Id")
    class TestGetCloudVendorDetails {
        @Test
        @DisplayName("Success Scenario:Get Cloud Vendor By Id")
        void testGetCloudVendorDetails_Found() throws Exception {
            when(cloudVendorService.getCloudVendor("1")).thenReturn(cloudVendorOne);
            mockMvc.perform(get("/cloudvendor/1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Negative Scenario:Cloud Vendor Not Found")
        void testGetCloudVendorDetails_NotFound() throws Exception {
            when(cloudVendorService.getCloudVendor("1")).thenThrow(new CloudVendorNotFoundException("Cloud Vendor Not Found"));
            mockMvc.perform(get("/cloudvendor/1"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetAllCloudVendorDetail {
        @Test
        @DisplayName("Success scenario:Get All Cloud Vendor")
        void getAllCloudVendorDetails_Found() throws Exception {
            when(cloudVendorService.getAllCloudVendors())
                    .thenReturn(cloudVendorList);
            mockMvc.perform(get("/cloudvendor/"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Negative Scenario:Cloud Vendor Empty")
        void getAllCloudVendorDetails_NotFound() throws Exception {
            when(cloudVendorService.getAllCloudVendors())
                    .thenThrow(new CloudVendorNotFoundException("Cloud Vendors does not exist"));
            mockMvc.perform(get("/cloudvendor/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class SaveCloudVendor {
        @Test
        void createCloudVendorDetails_isStatusCode200() throws Exception {

            String requestJson = getJsonObject(cloudVendorTwo);
            when(cloudVendorService.createCloudVendor(cloudVendorTwo)).thenReturn("Success");
            mockMvc.perform(post("/cloudvendor/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void createCloudVendorDetails_isStatusCode400() throws Exception {
            cloudVendorTwo.setVendorPhoneNumber("123456789");
            String requestJson = getJsonObject(cloudVendorTwo);
            when(cloudVendorService.createCloudVendor(cloudVendorTwo)).thenReturn("Success");
            MvcResult mvcResult = mockMvc.perform(post("/cloudvendor/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))

                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            Map<String, String> error = new HashMap<>();
            error.put("vendorPhoneNumber", "Phone Number should be 10 digits");

            String actualResult = mvcResult.getResponse().getContentAsString();
            String expectedResult = getJsonObject(error);
            assertThat(expectedResult).isEqualToIgnoringWhitespace(actualResult);
        }

        @Test
        void createCloudVendorDetails_isStatusCode415() throws Exception {
            String requestJson = getJsonObject(cloudVendorTwo);
            when(cloudVendorService.createCloudVendor(cloudVendorTwo)).thenReturn("Success");
            mockMvc.perform(post("/cloudvendor/")
                            .contentType(MediaType.APPLICATION_ATOM_XML)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }

    }

    @Nested
    class UpdateCloudVendorDetails {
        @Test
        void updateCloudVendorDetails_Found() throws Exception {

            String requestJson = getJsonObject(cloudVendorTwo);

            when(cloudVendorService.updateCloudVendor(cloudVendorTwo)).thenReturn("Success");
            mockMvc.perform(put("/cloudvendor/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
        @Test
        void updateCloudVendorDetails_BadRequest() throws Exception {
            cloudVendorTwo.setVendorPhoneNumber("123456");
            String requestJson = getJsonObject(cloudVendorTwo);

            when(cloudVendorService.updateCloudVendor(cloudVendorTwo)).thenReturn("Success");
            mockMvc.perform(put("/cloudvendor/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }


    @Test
    void deleteCloudVendorDetails() throws Exception {
        when(cloudVendorService.deleteCloudVendor("1"))
                .thenReturn("Success");
        this.mockMvc.perform(delete("/cloudvendor/1")).andDo(print()).andExpect(status().isOk());

    }


    public static String getJsonObject(Object cloudVendor) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(cloudVendor);
        return requestJson;
    }
}
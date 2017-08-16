package me.ordepdev.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
@AutoConfigureStubRunner(workOffline = true, ids = "me.ordepdev:validator-external-api:+:stubs:8080")
public class ConsumerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ConsumerController controller;
    @Autowired private RestTemplate restTemplate;
    private JacksonTester<ItemRequest> jsonRequest;
    private JacksonTester<ItemResponse> jsonResponse;

    @Before
    public void setup() {
        ObjectMapper objectMappper = new ObjectMapper();
        JacksonTester.initFields(this, objectMappper);
    }

    @Test
    public void store() throws Exception {

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest.write(new ItemRequest(uuid, Size.SMALL)).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse.write(new StoredItem(new ItemRequest(uuid, Size.SMALL))).getJson()))
                .andDo(document("store"));
    }

    @Test
    public void validate_withValidSize_shouldReturnHttpOk() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ResponseEntity<Response> response = restTemplate.exchange(
                "http://localhost:8080/validate", HttpMethod.POST,
                new HttpEntity<>("{\"size\":\"SMALL\"}", headers),
                Response.class
        );

        assertThat(response.getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualTo("{\"message\":\"Size is valid.\"}");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Response implements Serializable {

        private static final long serialVersionUID = -5953641903602056102L;

        private String message;

        @Override
        public final String toString() {
            return String.format("{\"message\":\"%s\"}", this.message);
        }
    }
}
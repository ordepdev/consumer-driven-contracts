package me.ordepdev.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(workOffline = true, ids = "me.ordepdev:validator-external-api:+:stubs:8080")
public class ConsumerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ConsumerController controller;
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
}
package me.ordepdev.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
class ConsumerController {

    private final RestTemplate restTemplate;

    ConsumerController(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(path = "/store", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> store(@RequestBody ItemRequest request) {

        final ResponseEntity<String> response = this.restTemplate.exchange(
                RequestEntity
                        .post(URI.create("http://localhost:8080/validate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request),
                String.class
        );

        if (HttpStatus.OK != response.getStatusCode()) {
            return new ResponseEntity<>(
                    new RejectedItem(request),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new StoredItem(request),
                HttpStatus.OK
        );
    }

}

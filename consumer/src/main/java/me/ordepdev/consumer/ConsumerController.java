package me.ordepdev.consumer;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@RestController
class ConsumerController {

    private final RestTemplate restTemplate;

    ConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(path = "/store", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> store(@RequestBody ItemRequest request) {

        ResponseEntity<String> response = this.restTemplate.exchange(
                RequestEntity
                        .post(URI.create("http://localhost:8080/validate"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request),
                String.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            return new ResponseEntity<>(new RejectedItem(request), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new StoredItem(request), HttpStatus.OK);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ItemRequest {
    private UUID uuid;
    private Size size;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ItemResponse {
    UUID uuid;
    Size size;
    Status status;

    ItemResponse(ItemRequest request) {
        this.uuid = request.getUuid();
        this.size = request.getSize();
    }
}

@Getter
@Setter
@NoArgsConstructor
class StoredItem extends ItemResponse {

    StoredItem(ItemRequest request) {
        super(request);
        this.status = Status.STORED;
    }
}

@Getter
@Setter
@NoArgsConstructor
class RejectedItem extends ItemResponse {

    RejectedItem(ItemRequest request) {
        super(request);
        this.status = Status.REJECTED;
    }
}

enum Size {
    SMALL, MEDIUM, LARGE
}

enum Status {
    STORED, REJECTED
}

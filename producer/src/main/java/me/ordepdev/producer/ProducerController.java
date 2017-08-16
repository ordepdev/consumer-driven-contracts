package me.ordepdev.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
class ProducerController {

    @PostMapping(path = "/validate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> validate(@RequestBody ItemRequest request) {

        if (Size.SMALL.equals(request.getSize())) {
            return new ResponseEntity<>(
                    "Size is invalid.",
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                "Size is valid.",
                HttpStatus.OK
        );
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ItemRequest {
    private UUID uuid;
    private Size size;
}

enum Size {
    SMALL, MEDIUM, LARGE
}

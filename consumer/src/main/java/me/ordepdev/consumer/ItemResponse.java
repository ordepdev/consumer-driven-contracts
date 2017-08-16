package me.ordepdev.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
class ItemResponse {

    private UUID uuid;
    private Size size;
    private Status status;

    ItemResponse(final UUID uuid, final Size size, final Status status) {
        this.uuid = uuid;
        this.size = size;
        this.status = status;
    }
}

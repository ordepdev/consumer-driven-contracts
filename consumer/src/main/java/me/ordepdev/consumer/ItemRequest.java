package me.ordepdev.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ItemRequest {

    private UUID uuid;
    private Size size;
}

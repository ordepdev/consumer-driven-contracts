package me.ordepdev.consumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class StoredItem extends ItemResponse {

    StoredItem(final ItemRequest request) {
        super(request.getUuid(), request.getSize(), Status.STORED);
    }
}

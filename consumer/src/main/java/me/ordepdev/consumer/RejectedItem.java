package me.ordepdev.consumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class RejectedItem extends ItemResponse {

    RejectedItem(final ItemRequest request) {
        super(request.getUuid(), request.getSize(), Status.REJECTED);
    }
}

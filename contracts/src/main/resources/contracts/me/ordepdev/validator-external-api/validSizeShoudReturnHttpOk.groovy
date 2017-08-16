package contracts.validator.api

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "POST"
        url "/validate"
        body ([
                size: "SMALL"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body([
                message: "Size is valid."
        ])
        headers {
            contentType(applicationJson())
        }
    }
}

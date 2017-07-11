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
            header("Content-Type", "application/json")
        }
    }
    response {
        status 200
        body([
                message: "Size is valid."
        ])
        headers {
            header("Content-Type", value(consumer("application/json"), producer(regex("application/json.*"))))
        }
    }
}

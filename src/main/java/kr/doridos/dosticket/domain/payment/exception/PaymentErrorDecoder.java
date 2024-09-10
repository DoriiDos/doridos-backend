package kr.doridos.dosticket.domain.payment.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class PaymentErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(PaymentErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        PaymentError paymentError = null;

        try (InputStream body = response.body().asInputStream()) {
            paymentError = objectMapper.readValue(body, PaymentError.class);
        } catch (IOException e) {
            logger.error("Failed to process payment error response", e);
            return new RuntimeException("Error reading response body", e);
        }

        HttpStatus status = HttpStatus.valueOf(response.status());

        if (status.is4xxClientError()) {
            logger.info("{}", status);
            return new PaymentException(paymentError.getCode(), paymentError.getMessage(), status);
        } else if (status.is5xxServerError()) {
            logger.info("{}", status);
            return new PaymentException(paymentError.getCode(), paymentError.getMessage(), status);
        }
        return new RuntimeException("Unexpected error: " + paymentError.getMessage());
    }
}

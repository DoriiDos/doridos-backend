package kr.doridos.dosticket.domain.payment.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import kr.doridos.dosticket.domain.payment.config.PaymentProperties;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PaymentAuthInterceptor implements RequestInterceptor {
    private static final String AUTH_HEADER_PREFIX = "Basic ";

    private final PaymentProperties paymentProperties;

    public PaymentAuthInterceptor(final PaymentProperties paymentProperties) {
        this.paymentProperties = paymentProperties;
    }

    @Override
    public void apply(final RequestTemplate template) {
        final String authHeader = createPaymentAuthorizationHeader();
        template.header("Authorization", authHeader);
    }

    private String createPaymentAuthorizationHeader() {
        final byte[] encodedBytes = Base64.getEncoder().encode((paymentProperties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedBytes);
    }
}

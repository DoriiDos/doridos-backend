package kr.doridos.dosticket.domain.payment.config;

import feign.Request;

import kr.doridos.dosticket.domain.payment.config.interceptor.PaymentAuthInterceptor;
import kr.doridos.dosticket.domain.payment.config.interceptor.PaymentLoggingInterceptor;
import kr.doridos.dosticket.domain.payment.exception.PaymentErrorDecoder;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.TimeUnit;

public class PaymentFeignConfig {

    private final PaymentProperties paymentProperties;

    public PaymentFeignConfig(PaymentProperties paymentProperties) {
        this.paymentProperties = paymentProperties;
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(2, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
    }

    @Bean
    public PaymentErrorDecoder paymentErrorDecoder() {
        return new PaymentErrorDecoder();
    }

    @Bean
    PaymentAuthInterceptor paymentAuthInterceptor() {
        return new PaymentAuthInterceptor(paymentProperties);
    }

    @Bean
    PaymentLoggingInterceptor paymentLoggingInterceptor() {
        return new PaymentLoggingInterceptor();
    }
}

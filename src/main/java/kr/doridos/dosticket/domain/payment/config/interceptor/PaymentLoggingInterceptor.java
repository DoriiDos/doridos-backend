package kr.doridos.dosticket.domain.payment.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class PaymentLoggingInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLoggingInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        logger.info("Payment Request: {} {}", template.method(), template.url());
        logger.info("Payment Request Body: {}", new String(template.body()));
    }
}


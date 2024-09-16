package kr.doridos.dosticket.global.redis.util;

import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class DistributedLockKeyGenerator {
    public static List<Long> generateKeys(int[] paramIndexes, Object[] args, String key) {
        List<Long> seatIds = new ArrayList<>();
        for (int index : paramIndexes) {
            Object param = args[index];
            if (param instanceof ReservationRequest) {
                ReservationRequest request = (ReservationRequest) param;
                seatIds.addAll(request.getSeatIds()); // 좌석 ID 리스트 추출
            }
        }
        return seatIds;
    }
}


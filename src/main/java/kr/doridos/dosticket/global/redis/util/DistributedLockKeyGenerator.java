package kr.doridos.dosticket.global.redis.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class DistributedLockKeyGenerator {
    public static Object generate(int[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable("arg" + i, args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}

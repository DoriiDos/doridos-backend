package kr.doridos.dosticket.global.redis.aop;

import kr.doridos.dosticket.exception.ErrorCode;
import kr.doridos.dosticket.global.redis.DistributedLock;
import kr.doridos.dosticket.global.redis.util.DistributedLockKeyGenerator;
import kr.doridos.dosticket.global.redis.exception.LockFailException;
import kr.doridos.dosticket.global.redis.exception.LockInterruptedException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(kr.doridos.dosticket.global.redis.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + DistributedLockKeyGenerator.generate(signature.getParameterNames(),
                joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            if (!rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit())) {
                throw new LockFailException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }
            System.out.println(key);
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new LockInterruptedException(ErrorCode.LOCK_INTERRUPTED);
        } finally {
            rLock.unlock();
        }
    }
}
//package chao.app.ami.monitor.aspect;
//
//import com.yingshi.monitor.MonitorManager;
//import com.yingshi.monitor.ThreadMonitor;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * Created by qinchao on 2017/9/6.
// */
//
//@Aspect
//public class ThreadPoolExecutorAspect {
//
//    @Pointcut("call(java.util.concurrent.ThreadPoolExecutor+.new(..))")
//    public void threadPoolExecutorInitPointcut() {
//
//    }
//
//    @Around("threadPoolExecutorInitPointcut()")
//    public Object afterThreadInitAOP(ProceedingJoinPoint joinPoint) {
//        Object[] objects = joinPoint.getArgs();
//        ThreadPoolExecutor threadPool;
//        try {
//            threadPool = (ThreadPoolExecutor) joinPoint.proceed(objects);
//        } catch (Throwable throwable) {
//            return null;
//        }
//        ThreadMonitor monitor = MonitorManager.getThreadMonitor();
//        if (monitor != null) {
//            monitor.cacheThreadPool(threadPool, String.valueOf(joinPoint.getThis()));
//        }
//        return threadPool;
//    }
//
//
//
//
//    @Pointcut("call(void java.util.concurrent.ThreadPoolExecutor+.execute(java.lang.Runnable))")
//    public void threadPoolExecutePointcut() {
//
//    }
//
//    @After("threadPoolExecutePointcut()")
//    public void afterThreadExecuteAOP(JoinPoint joinPoint) {
//        Object _target = joinPoint.getTarget();
//
//        if (!(_target instanceof ThreadPoolExecutor)) {
//            return;
//        }
//        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) _target;
//        ThreadMonitor monitor = MonitorManager.getThreadMonitor();
//        if (monitor != null) {
//            monitor.cacheThreadPool(threadPool, String.valueOf(joinPoint.getThis()));
//        }
//    }
//}

//package chao.app.ami.monitor.aspect;
//
//import com.yingshi.monitor.DumpMonitor;
//import com.yingshi.monitor.MonitorManager;
//import com.yingshi.monitor.SourceInfo;
//import com.yingshi.monitor.ThreadMonitor;
//import com.yingshi.monitor.ThreadUtil;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//
///**
// * Created by qinchao on 2017/9/6.
// */
//
//@Aspect
//public class ThreadAspect {
//
//    @Pointcut("call(java.lang.Thread+.new(..))")
//    public void threadInitPointcut() {
//
//    }
//
//    @Around("threadInitPointcut() && !within(ThreadMonitor)")
//    public Object afterThreadInitAOP(ProceedingJoinPoint joinPoint) {
//        Object[] objects = joinPoint.getArgs();
//        Thread thread = null;
//        try {
//            thread = (Thread) joinPoint.proceed(objects);
//        } catch (Throwable throwable) {
//            return null;
//        }
//        ThreadMonitor monitor = MonitorManager.getThreadMonitor();
//        if (monitor != null) {
//            SourceInfo sourceInfo = ThreadUtil.getSourceLocation(ThreadMonitor.class.getSimpleName(), DumpMonitor.class.getSimpleName());
//            monitor.cacheThread(thread, null, sourceInfo);
//        }
//        return thread;
//    }
//}

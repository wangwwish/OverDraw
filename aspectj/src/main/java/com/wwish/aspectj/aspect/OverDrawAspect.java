
package com.wwish.aspectj.aspect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 跟踪被DebugTrace注解标记的方法和构造函数
 */
@Aspect
public class OverDrawAspect {

  private static volatile boolean enabled = true;



  private static final String POINTCUT_METHOD =
      "execution(@com.wwish.aspectj.annotation.Activity * *(..))";

  private static final String POINTCUT_FRAGMENT_METHOD =
          "execution(@com.wwish.aspectj.annotation.Fragment * *(..))";


  @Pointcut(POINTCUT_METHOD)
  public void methodAnnotatedWithActivity() {}

  @Pointcut(POINTCUT_FRAGMENT_METHOD)
  public void methodAnnotatedWithFragment() {}



  @Around("methodAnnotatedWithFragment()")
  public Object weaveFragmentJoinPoint(ProceedingJoinPoint joinPoint)throws Throwable{
    enterFragmentMethod(joinPoint);
    Object result = joinPoint.proceed();
    return result;
  }


  @Around("methodAnnotatedWithActivity()")
  public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

    enterMethod(joinPoint);

//
    Object result = joinPoint.proceed();
//
    return result;
  }

  private  void enterFragmentMethod(ProceedingJoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();

    Class<?> cls = signature.getDeclaringType();
    Object object = joinPoint.getThis();

    printOverDrawCounter(((Fragment)object).getActivity());
    printOverDrawCounter(asTag(cls), ((Fragment)object).getActivity());
    if (!enabled) return;
  }
  private  void enterMethod(ProceedingJoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();

    Class<?> cls = signature.getDeclaringType();
    Object object=joinPoint.getThis();
    Activity activity=(Activity)object;
//    printOverDrawCounter(activity);
    printOverDrawCounter(asTag(cls),activity);
    if (!enabled) return;

  }

  private static void exitMethod(ProceedingJoinPoint joinPoint, Object result, long lengthMillis) {
    if (!enabled) return;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

    }

    Signature signature = joinPoint.getSignature();

    Class<?> cls = signature.getDeclaringType();
    String methodName = signature.getName();
    boolean hasReturnType = signature instanceof MethodSignature
            && ((MethodSignature) signature).getReturnType() != void.class;

    StringBuilder builder = new StringBuilder("\u21E0 ")
            .append(methodName)
            .append(" [")
            .append(lengthMillis)
            .append("ms]");

    if (hasReturnType) {
      builder.append(" = ");
      builder.append(Strings.toString(result));
    }

    Log.v(asTag(cls), builder.toString());
  }

  private static String asTag(Class<?> cls) {
    if (cls.isAnonymousClass()) {
      return asTag(cls.getEnclosingClass());
    }
    return cls.getSimpleName();
  }
  private void printOverDrawCounter(Activity activity){
    printOverDrawCounter("wangwei-test",activity);
  }

  private void printOverDrawCounter(String tag,Activity activity){


    Log.d(tag,"------------------ : "+activity.getTitle());
    if(Build.VERSION.SDK_INT !=19){
      return;
    }
    //

    Log.d(tag,"------------------ : begin");
    try{
//            UiOverDraw.this.getSystemService(Context.ACTIVITY_SERVICE)
//             ActivityService activityService=(ActivityServiceImpl);
      Class<?> view =Class.forName("android.view.View");
      Method methodGetHardwareRenderer=view.getDeclaredMethod("getHardwareRenderer");
      methodGetHardwareRenderer.setAccessible(true);
      Object Gl20Renderer=methodGetHardwareRenderer.invoke(activity.getWindow().getDecorView().getRootView());
      if(Gl20Renderer !=null){
        Field fieldDebugOverDrawLayer=Gl20Renderer.getClass().getSuperclass().getDeclaredField("mDebugOverdrawLayer");
        fieldDebugOverDrawLayer.setAccessible(true);
        Object GLES20RenderLayer =fieldDebugOverDrawLayer.get(Gl20Renderer);
        if(GLES20RenderLayer !=null){
          Method methodGetCanvas=GLES20RenderLayer.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getCanvas");
          methodGetCanvas.setAccessible(true);
          Object GLES20Canvas=methodGetCanvas.invoke(GLES20RenderLayer);
          Method methodGetOverdraw=Gl20Renderer.getClass().getDeclaredMethod("getOverdraw",Class.forName("android.view.HardwareCanvas"));
          methodGetOverdraw.setAccessible(true);
          Float result=(Float)methodGetOverdraw.invoke(Gl20Renderer,GLES20Canvas);
          Log.d(tag,"------------------ : "+result);
        }

      }


    }catch (Exception exception){
      Log.d(tag,"------------------ : "+exception.toString());
    }
  }


}

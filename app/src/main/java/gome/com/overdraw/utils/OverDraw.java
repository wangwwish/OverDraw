package gome.com.overdraw.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by wangwei-ds10 on 2018/1/24.
 */

public class OverDraw {
     public static void printActivityOverDrawCounter(Activity activity){
        if(Build.VERSION.SDK_INT !=19){
            return;
        }
        //

        Log.d("wangwei-test","------------------ : begin");
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
                    Log.d("wangwei-test","------------------ : "+result);
                    Log.d(activity.getClass().getSimpleName(),"------------------ : "+result);
                }

            }


        }catch (Exception exception){
            Log.d("wangwei-test","------------------ : "+exception.toString());
        }
    }

    public static void printFragmentOverDrawCounter(Fragment fragment){
        if(Build.VERSION.SDK_INT !=19){
            return;
        }
        //

        Log.d("wangwei-test","------------------ : begin");
        try{
//            UiOverDraw.this.getSystemService(Context.ACTIVITY_SERVICE)
//             ActivityService activityService=(ActivityServiceImpl);
            Class<?> view =Class.forName("android.view.View");
            Method methodGetHardwareRenderer=view.getDeclaredMethod("getHardwareRenderer");
            methodGetHardwareRenderer.setAccessible(true);
            Object Gl20Renderer=methodGetHardwareRenderer.invoke(fragment.getActivity().getWindow().getDecorView().getRootView());
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
                    Log.d("wangwei-test","------------------ : "+result);
                    Log.d(fragment.getContext().getClass().getSimpleName(),"------------------ : "+result);
                }

            }


        }catch (Exception exception){
            Log.d("wangwei-test","------------------ : "+exception.toString());
        }
    }
}

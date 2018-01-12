package gome.com.overdraw;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wwish.aspectj.annotation.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gome.com.overdraw.fragment.LeftFragment;
import gome.com.overdraw.fragment.RightFragment;

public class MainActivity extends AppCompatActivity implements LeftFragment.MyListener {

    /**
     * 实现MyListener,当LeftFragment中点击第一页的时候，让RightFragment显示第一页信息,同理当点击第二页的时候，RightFragment显示第二页信息
     *
     * @param index
     *            显示的页数
     */
    public void showMessage(int index)
    {
        if (1 == index)
            showMessageView.setText(R.string.first_page);
        if (2 == index)
            showMessageView.setText(R.string.second_page);
        if (3 == index)
            showMessageView.setText(R.string.third_page);
    }

    /** 得到RightFragment中显示信息的控件 */
    private TextView showMessageView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Activity--->onCreate");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 动态增加Fragment
        RightFragment rightFragment = new RightFragment();
        LeftFragment leftFragment = new LeftFragment();
        transaction.add(R.id.left_layout, leftFragment, "leftfragment");
        transaction.add(R.id.right_layout, rightFragment, "rightfragment");
        transaction.commit();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        System.out.println("Activity--->onResume"); //注意：findview放到这里
        showMessageView = (TextView) findViewById(R.id.right_show_message);
    }



    @Override
    @Activity
    protected void onPause() {
        super.onPause();
    }

    private void printOverDrawCounter(){
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
            Object Gl20Renderer=methodGetHardwareRenderer.invoke(getWindow().getDecorView().getRootView());
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
                }

            }


        }catch (Exception exception){
            Log.d("wangwei-test","------------------ : "+exception.toString());
        }
    }

    public void writeDebugHwOverdrawOptions() {
        int SYSPROPS_TRANSACTION = ('_'<<24)|('S'<<16)|('P'<<8)|'R';
        Class clz = null;
        try {
            clz = Class.forName("android.os.ServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }
        Method method = null;
        Method methodcheckService = null;
        try {
            method = clz.getMethod("listServices");
            methodcheckService = clz.getMethod("checkService", String.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }
        String[] services = null;
        try {
            services = (String[]) method.invoke(clz);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.d("wangwei-test","------------------ : "+e.toString());
        }
        Parcel data = null;
        for (String service : services) {
            IBinder obj = null;
            try {
                obj = (IBinder) methodcheckService.invoke(clz, service);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d("wangwei-test","------------------ : "+e.toString());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Log.d("wangwei-test","------------------ : "+e.toString());
            }
            if (obj != null) {
                data = Parcel.obtain();
                try {
                    obj.transact(SYSPROPS_TRANSACTION, data, null, 0);
                } catch (RemoteException e) {
                    Log.d("wangwei-test","------------------ : "+e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("wangwei-test","------------------ : "+e.toString());
                }
            }
            data.recycle();
        }
    }
}
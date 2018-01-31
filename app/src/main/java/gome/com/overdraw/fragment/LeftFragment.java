package gome.com.overdraw.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wwish.ganalytics.db.builder.OverDrawInsertBuilder;
import com.wwish.ganalytics.db.builder.OverDrawQueryBuilder;
import com.wwish.ganalytics.db.entity.OverDraw;

import java.util.List;

import gome.com.overdraw.R;

/**
 * Created by wwish on 2018/1/11.
 */

public class LeftFragment extends Fragment {


    /** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */
    public interface MyListener
    {
        public void showMessage(int index);
    }

    private MyListener myListener;
    private Button firstButton;
    private Button secondButton;
    private Button thirdButton;

    /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        System.out.println("LeftFragment--->onAttach");

        myListener = (MyListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("LeftFragment--->onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("LeftFragment--->onCreateView");
        return inflater.inflate(R.layout.fragment_left, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("LeftFragment--->onResume");

        firstButton = (Button) getActivity().findViewById(R.id.first_button);
        secondButton = (Button) getActivity().findViewById(R.id.second_button);
        thirdButton = (Button) getActivity().findViewById(R.id.third_button);

        MyButtonClickListener clickListener = new MyButtonClickListener();
        firstButton.setOnClickListener(clickListener);
        secondButton.setOnClickListener(clickListener);
        thirdButton.setOnClickListener(clickListener);
    }

    /** 按钮的监听器 */
    class MyButtonClickListener implements View.OnClickListener
    {
        public void onClick(View v) {
        Button button = (Button) v;
        if (button == firstButton)
            myListener.showMessage(1);
        if (button == secondButton){
            myListener.showMessage(2);
            System.out.print(getTest());
        }

        if (button == thirdButton){
            //                myListener.showMessage(3);
            insertOverDrawToDatabase(getActivity(), 1.1f);
        }


    }
        private  void insertOverDrawToDatabase(Activity activity, Float result) {
            OverDraw overDraw=new OverDraw();
            overDraw.setT(System.currentTimeMillis());
            overDraw.setName(activity.getClass().getSimpleName());
            overDraw.setOver(String.valueOf(result));
            overDraw.setUpload(false);
            overDraw.setVersion("1.0.1");

            OverDrawInsertBuilder.insertData(activity,overDraw);
        }

        private String getTest(){
           List<OverDraw> list= OverDrawQueryBuilder.getQueryAll(getActivity()).list();
           StringBuilder stringBuilder=new StringBuilder("");
            for(OverDraw overDraw:list){

                stringBuilder.append(overDraw.getOver());
                Log.d("wangwei","----------------"+overDraw.getOver());
                stringBuilder.append("\n");
            }
            Log.d("wangwei","----------------"+stringBuilder.toString());
            return stringBuilder.toString();
        }
    }
}


package gome.com.overdraw.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gome.com.overdraw.R;

/**
 * Created by wwish on 2018/1/11.
 */

public class RightFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        System.out.println("RightFragment--->onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("RightFragment--->onCreateView");
        return inflater.inflate(R.layout.fragment_right, container, false);
    }

    @Override
    @com.wwish.aspectj.annotation.Fragment
    public void onPause() {
        super.onPause();
    }
}

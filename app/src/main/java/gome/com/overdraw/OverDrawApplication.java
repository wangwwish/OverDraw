package gome.com.overdraw;

import android.app.Application;


import com.facebook.stetho.Stetho;
import com.wwish.ganalytics.OverDrawManager;

/**
 * Created by wangwei-ds10 on 2018/1/25.
 */

public class OverDrawApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initAnalytics();
    }

    private void initAnalytics(){
        Stetho.initializeWithDefaults(this);
        OverDrawManager.getInstance(getApplicationContext()).init(this);
    }
}

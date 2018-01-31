package com.wwish.ganalytics.db.builder;

import android.content.Context;

import com.wwish.ganalytics.db.DBManager;
import com.wwish.ganalytics.db.dao.OverDrawDao;
import com.wwish.ganalytics.db.entity.OverDraw;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class OverDrawInsertBuilder {
    public static long insertData(Context context, OverDraw overDraw){
        AbstractDao dao = DBManager.getInstance(context).getDao(new OverDraw());
        long insert = dao.insert(overDraw);
        return insert;
    }
}

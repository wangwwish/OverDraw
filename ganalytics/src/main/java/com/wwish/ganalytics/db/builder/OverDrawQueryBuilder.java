package com.wwish.ganalytics.db.builder;

import android.content.Context;

import com.wwish.ganalytics.db.DBManager;
import com.wwish.ganalytics.db.dao.OverDrawDao;
import com.wwish.ganalytics.db.entity.OverDraw;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by wangwei-ds10 on 2018/1/25.
 */

public class OverDrawQueryBuilder {
    public static Query getQueryOrderByTimeLimit(Context context, int count){
        AbstractDao dao = DBManager.getInstance(context).getDao(new OverDraw());
        QueryBuilder queryBuilder = dao.queryBuilder();
        Query query = queryBuilder.orderDesc(OverDrawDao.Properties.T).limit(count).build();
        return query;
    }

    public static Query getQueryAll(Context context){
        AbstractDao dao = DBManager.getInstance(context).getDao(new OverDraw());
        QueryBuilder queryBuilder = dao.queryBuilder();
        Query query = queryBuilder.orderDesc(OverDrawDao.Properties.T).build();
        return query;
    }
}


package com.wwish.ganalytics.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.wwish.ganalytics.db.entity.OverDraw;

import com.wwish.ganalytics.db.dao.OverDrawDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig overDrawDaoConfig;

    private final OverDrawDao overDrawDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        overDrawDaoConfig = daoConfigMap.get(OverDrawDao.class).clone();
        overDrawDaoConfig.initIdentityScope(type);

        overDrawDao = new OverDrawDao(overDrawDaoConfig, this);

        registerDao(OverDraw.class, overDrawDao);
    }
    
    public void clear() {
        overDrawDaoConfig.clearIdentityScope();
    }

    public OverDrawDao getOverDrawDao() {
        return overDrawDao;
    }

}

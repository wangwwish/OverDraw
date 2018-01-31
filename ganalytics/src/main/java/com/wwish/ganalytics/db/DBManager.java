package com.wwish.ganalytics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.wwish.ganalytics.db.dao.DaoMaster;
import com.wwish.ganalytics.db.dao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by gome.analytics-ds on 2016/11/18.
 */
public class DBManager {

    private final static String dbName = "OverDrawView";
    private static volatile DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManager(Context context) {
        this.context = context.getApplicationContext();
        openHelper = new DaoMaster.DevOpenHelper(this.context, dbName, null);
    }

    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void insert(final Object object) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.insert(object);
    }

    public void insert(final Object object, final DbCrudListener listener) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.setListenerMainThread(new AsyncOperationListener(){
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(object);
                    }
                 }else{
                    if(listener!=null){
                        listener.doFail();
                    }

                }
            }
        });
        asyn.insert(object);
    }

    public void  insertList(List objects) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.insertInTx(objects.get(0).getClass(),objects);
    }

    public void insertList(final List objects, final DbCrudListener listener) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.setListenerMainThread(new AsyncOperationListener(){

            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(objects);
                    }
                }else{
                    if(listener!=null){
                        listener.doFail();
                    }
                }
            }
        });
        asyn.insertInTx(objects.get(0).getClass(),objects);
    }

    public void queryListByRules(final Query query, final DbCrudListener listener) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.setListenerMainThread(new AsyncOperationListener(){
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(operation.getResult());
                    }
                }else{
                    if(listener!=null){
                        listener.doFail();
                    }
                }
            }
        });
        asyn.queryList(query);
    }

    public void queryList(final Object object, final DbCrudListener listener) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        AbstractDao dao = daoSession.getDao(object.getClass());
        QueryBuilder queryBuilder = dao.queryBuilder();
        Query query = queryBuilder.build();
        asyn.setListenerMainThread(new AsyncOperationListener(){
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(operation.getResult());
                    }
                }else{
                    if(listener!=null){
                        listener.doFail();
                    }
                }
            }
        });
        asyn.queryList(query);
    }

    public void delete(final Object object) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.delete(object);
    }

    public void deleteList(final List objects){
        if (objects == null || objects.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.deleteInTx(objects.get(0).getClass(),objects);
    }

    public void deleteList(final List objects, final DbCrudListener listener){
        if (objects == null || objects.isEmpty()) {
            return;
        }
        try {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            AsyncSession asyn = daoSession.startAsyncSession();
            asyn.setListenerMainThread(new AsyncOperationListener() {
                @Override
                public void onAsyncOperationCompleted(AsyncOperation operation) {
                    if(operation.isCompletedSucessfully()){
                        if(listener != null){
                            listener.doSuccess(objects);
                        }
                    }else {
                        if(listener != null){
                            listener.doFail();
                        }
                    }
                }
            });
            asyn.deleteInTx(objects.get(0).getClass(),objects);
        }catch (Exception e){

        }
    }

    public void deleteAll(Class tables){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        daoSession.deleteAll(tables);
    }

    public void delete(final Object object, final DbCrudListener listener) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.setListenerMainThread(new AsyncOperationListener(){

            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(object);
                    }
                }else{
                    if(listener!=null){
                        listener.doFail();
                    }
                }
            }
        });
        asyn.delete(object);
    }

    public void update(Object object) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.update(object);
    }

    public void update(Object object, final DbCrudListener listener) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AsyncSession asyn = daoSession.startAsyncSession();
        asyn.setListenerMainThread(new AsyncOperationListener(){

            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if(operation.isCompletedSucessfully()){
                    if(listener!=null){
                        listener.doSuccess(operation.getResult());
                    }
                }else{
                    if(listener!=null){
                        listener.doFail();
                    }
                }
            }
        });
        asyn.update(object);
    }

    public AbstractDao getDao(Object object){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AbstractDao dao = daoSession.getDao(object.getClass());
        return dao;
    }

    public interface DbCrudListener{
        void doSuccess(Object object);
        void doFail();
    }



}

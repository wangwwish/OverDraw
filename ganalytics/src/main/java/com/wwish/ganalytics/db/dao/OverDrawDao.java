package com.wwish.ganalytics.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wwish.ganalytics.db.entity.OverDraw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "OVER_DRAW".
*/
public class OverDrawDao extends AbstractDao<OverDraw, Long> {

    public static final String TABLENAME = "OVER_DRAW";

    /**
     * Properties of entity OverDraw.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "name");
        public final static Property Over = new Property(2, String.class, "over", false, "over");
        public final static Property T = new Property(3, long.class, "t", false, "t");
        public final static Property Version = new Property(4, String.class, "version", false, "version");
        public final static Property Upload = new Property(5, boolean.class, "upload", false, "upload");
    }


    public OverDrawDao(DaoConfig config) {
        super(config);
    }
    
    public OverDrawDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"OVER_DRAW\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"name\" TEXT," + // 1: name
                "\"over\" TEXT," + // 2: over
                "\"t\" INTEGER NOT NULL ," + // 3: t
                "\"version\" TEXT," + // 4: version
                "\"upload\" INTEGER NOT NULL );"); // 5: upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"OVER_DRAW\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OverDraw entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String over = entity.getOver();
        if (over != null) {
            stmt.bindString(3, over);
        }
        stmt.bindLong(4, entity.getT());
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(5, version);
        }
        stmt.bindLong(6, entity.getUpload() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OverDraw entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String over = entity.getOver();
        if (over != null) {
            stmt.bindString(3, over);
        }
        stmt.bindLong(4, entity.getT());
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(5, version);
        }
        stmt.bindLong(6, entity.getUpload() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public OverDraw readEntity(Cursor cursor, int offset) {
        OverDraw entity = new OverDraw( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // over
            cursor.getLong(offset + 3), // t
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // version
            cursor.getShort(offset + 5) != 0 // upload
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OverDraw entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOver(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setT(cursor.getLong(offset + 3));
        entity.setVersion(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUpload(cursor.getShort(offset + 5) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OverDraw entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OverDraw entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OverDraw entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

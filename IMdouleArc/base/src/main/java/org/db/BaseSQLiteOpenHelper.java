package org.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author: yuzzha
 * Date: 2019-06-26 9:12
 * Description: 数据库基类
 * Remark:
 */
public abstract class BaseSQLiteOpenHelper {

    public Context mContext;
    protected SQLiteOpenHelper mSQLiteOpenHelper;
    public SQLiteDatabase db;

    /**
     * 创建数据库
     *
     * @param mContext Context
     * @param name     数据库 Name
     * @param version  数据库 Version
     */
    public BaseSQLiteOpenHelper(Context mContext, String name, int version) {
        this.mContext = mContext;
        mSQLiteOpenHelper = new BaseSqliteOpenHelp(mContext, name, null, version);
    }

    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return mSQLiteOpenHelper;
    }

    //创建数据库 系统回调
    protected abstract void onCreateSQLiteDB(SQLiteDatabase db);

    //update数据库 系统回调
    protected abstract void onUpgradeSQLiteDB(SQLiteDatabase db, int oldVersion, int newVersion);

    //数据库降级 回调函数
    protected void onDowngradeSQLiteDB(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private class BaseSqliteOpenHelp extends SQLiteOpenHelper {

        public BaseSqliteOpenHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onCreateSQLiteDB(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgradeSQLiteDB(db, oldVersion, newVersion);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onDowngradeSQLiteDB(db, oldVersion, newVersion);
        }
    }

}

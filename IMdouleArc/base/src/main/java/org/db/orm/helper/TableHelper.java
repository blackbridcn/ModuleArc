package org.db.orm.helper;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.db.orm.annotation.Column;
import org.db.orm.annotation.Id;
import org.db.orm.annotation.Table;
import org.utils.StringUtils;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.helper.Logger;

/**
 *
 */
public class TableHelper {
    private static final String TAG = "ACTableHelper";

    /**
     * 创建表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        Table table = null;
        if (clazz.isAnnotationPresent(Table.class)) {
            table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        if (StringUtils.isEmpty(tableName)) {
            Logger.e(TAG, "ERROR: 未注解@Table: " + clazz.getName());
            return;
        }
        if (tabbleIsExist(db, tableName)) {
            String alert = table.alert();
            if (alert != null) {
                if (AlterTableHelpe.ADD_COLUMN.equals(alert)) {//表中新增加字段
                    checkAndAlterColumn(db, clazz.getDeclaredFields(), tableName);
                }
            }
            Logger.e(TAG, "ERROR: " + clazz.getName() + " 已经存在");
            return;
        }

        final StringBuilder sb = new StringBuilder();
        //  外键
        final StringBuilder foreignSb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (");
        final int titleLen = sb.length();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //没有注解@Column 不创建数据库字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String columnType;
            if (column.type().equals("")) columnType = getColumnType(field.getType());
            else {
                columnType = column.type();
            }
            //ID特殊处理,放置最前面
            if (field.isAnnotationPresent(Id.class)) {
                //实体类定义为Integer类型后不能生成Id异常
                if (((field.getType() == Integer.TYPE) || (field.getType() == Integer.class)) && field.getAnnotation(Id.class).type() == BaseOrmColumns.TYPE_INCREMENT) {
                    sb.insert(titleLen, column.name() + " " + columnType + " primary key autoincrement, ");
                } else {
                    sb.insert(titleLen, column.name() + " " + columnType + " primary key, ");
                }
            } else if (column.foreign()) {
                //外键
                foreignHandle(field, sb, foreignSb, column.name());
            } else {
                sb.append(column.name()).append(" ").append(columnType);
                if (column.length() != 0) {
                    sb.append("(").append(column.length()).append(")");
                }
                sb.append(", ");
            }
        }

        //加入外键
        sb.append(foreignSb);
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");
        String sql = sb.toString();
        Logger.d(TAG, "create table [" + tableName + "]: " + sql);
        db.execSQL(sql);
    }

    /**
     * 外键处理
     *
     * @param field 外键field
     */
    private static void foreignHandle(Field field, StringBuilder sb, StringBuilder foreignSb, String foreignName) {
        Class<?> clazz = field.getType();
        while (!Object.class.equals(clazz)) {
            String tableName = null;
            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = clazz.getAnnotation(Table.class);
                tableName = table.name();
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(Id.class)) {
                    Column column = f.getAnnotation(Column.class);
                    String idType;
                    if (column.type().equals("")) idType = getColumnType(f.getType());
                    else {
                        idType = column.type();
                    }
                    sb.append(foreignName).append(" ").append(idType).append(", ");
                    foreignSb.append(" FOREIGN KEY(").append(foreignName).append(") REFERENCES ").append(tableName).append("(").append(column.name()).append("), ");
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @SuppressLint("Recycle")
    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor;
        try {
            String sql = "select count(*) as c from sqlite_master " + "where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Logger.e("tabbleIsExist", "ERROR: " + e.toString());
        }
        return result;
    }

    /**
     * 删除表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "DROP TABLE IF EXISTS " + tableName;
        Logger.d(TAG, "dropTable[" + tableName + "]:" + sql);
        db.execSQL(sql);
    }

    /**
     * @param db        SQLiteDatabase
     * @param tableName tableName
     * @param <T>
     */
    private static <T> void checkAndAlterColumn(SQLiteDatabase db, Field[] fields, String tableName) {
        try {
            Column column;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (!fields[i].isAnnotationPresent(Column.class)) {
                    continue;
                }
                column = fields[i].getAnnotation(Column.class);
                if (StringUtils.isNotEmpty(column.alter()) && StringUtils.isNotEmpty(column.name())) {  //alter注解并且字段名字不能为空
                    if (TextUtils.equals(BaseOrmColumns.ADD_COLUMN, column.alter())) {//是否为新增字段
                        if (!checkColumnExists(db, tableName, column.name())) //该字段是否已经存在 不存在就新添加字段
                            AddColumn(db, column, fields[i], column.name(), tableName);
                    }
                }
            }

        } catch (Exception e) {
            Logger.e("checkColumnExists", " ERROR: " + e.toString());
        }
    }

    /**
     * 新增字段
     *
     * @param db         SQLiteDatabase
     * @param column     Column
     * @param field      Field
     * @param columnName 字段名
     * @param tableName  表名
     * @param <T>
     */
    private static <T> void AddColumn(SQLiteDatabase db, Column column, Field field, String columnName, String tableName) {
        String columnType;
        StringBuilder sql = null;
        columnType = column.type();
        if (StringUtils.isEmpty(columnType)) {
            columnType = getColumnType(field.getType());
        }
        sql = new StringBuilder();
        sql.append("ALTER TABLE '").append(tableName).append("' ADD COLUMN ").append(columnName).append(" ").append(columnType).append(" ");
        if (column.length() != 0) {
            sql.append("(").append(Integer.toString(column.length())).append(") ");
        }
        sql.append(";");
        Log.i(TAG, "AddColumn: sql: " + sql.toString());
        db.execSQL(sql.toString());

    }

    /**
     * 检查表中是父存在该字段
     *
     * @param db         SQLiteDatabase
     * @param tableName  表名
     * @param columnName 需要查询的字段名
     * @param <T>        JavaBean
     * @return result
     */
    private static <T> boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select * from sqlite_master where name = ? and sql like ?";
            cursor = db.rawQuery(sql, new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            Logger.e("checkColumnExists", columnName + " ERROR: " + e.toString());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }


    /**
     * 获取列类型.
     *
     * @param fieldType the field type
     * @return 列类型
     */
    private static String getColumnType(Class<?> fieldType) {
        if (String.class == fieldType) {
            return "TEXT";
        }
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            return "INTEGER";
        }
        if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            return "BIGINT";
        }
        if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            return "FLOAT";
        }
        if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
            return "INT";
        }
        if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            return "DOUBLE";
        }
        if (Blob.class == fieldType) {
            return "BLOB";
        }
        return "TEXT";
    }

    /***
     * 获取指定数据库中所有的表名
     * <p>
     * [ 已近过滤掉 android_metadata 和sqlite_sequence 两个系统数据库 ]
     * </>
     * @param db 数据库
     * @return 所有表的表名
     */
    public static List<String> extractTableName(SQLiteDatabase db) {
        List<String> tableList = null;
        Cursor cursor = null;
        try {
            String sql = "select name from sqlite_master where type='table' order by name ;";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                String table;
                tableList = new ArrayList<String>();
                do {
                    table = cursor.getString(0);
                    if (!TextUtils.equals(table, "android_metadata") && !TextUtils.equals(table, "sqlite_sequence")) {
                        tableList.add(table);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) cursor.close();
        }
        return tableList;
    }

    /**
     * 获取数据表中列名
     *
     * @param db        数据库
     * @param tableName 表名
     * @return 列名数组
     */
    public static String[] extractColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("name");
                if (columnIndex == -1) {
                    return null;
                }
                int index = 0;
                columnNames = new String[cursor.getCount()];
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }

    /**
     * 获取连个表中相同列Column名
     *
     * @param db         数据库对象
     * @param tableName1 表名1
     * @param tableName2 表名1
     * @return 两个表中公共列名List [ 返回 null时表示是没有公共列名]
     */
    public static List<String> extractCommomColumn(SQLiteDatabase db, String tableName1, String tableName2) {
        String[] name1 = extractColumnNames(db, tableName1);
        String[] name2 = extractColumnNames(db, tableName2);
        List<String> commomColumns = null;
        if (name1 != null && name1.length > 0) {
            commomColumns = new ArrayList<>(name1.length);
            for (String name : name2)
                for (String names : name1)
                    if (TextUtils.equals(names, name)) {
                        commomColumns.add(names);
                    }
        }
        return commomColumns;
    }

    /**
     * 临时表中恢复数据 [删除临时表]
     *
     * @param db     数据库
     * @param tables 数据库中表名
     */
    private static void restoreData(SQLiteDatabase db, List<String> tables) {
        try {
            db.beginTransaction();
            for (String tableName : tables) {
                String tempTableName = tableName + "_Temp";
                List<String> commomColumn = extractCommomColumn(db, tableName, tempTableName);
                String columns = TextUtils.join(",", commomColumn);
                String restoreSql = "INSERT INTO " + tableName + "(" + columns + ") SELECT " + columns + " FROM " + tempTableName;
                db.execSQL(restoreSql);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        dropTempTable(db, tables);
    }

    private static void dropTempTable(SQLiteDatabase db, List<String> tables) {
        try {
            db.beginTransaction();
            for (String tableName : tables) {
                String tempTableName = tableName + "_Temp";
                String dropTableSql = "DROP TABLE IF EXISTS " + tempTableName;
                db.execSQL(dropTableSql);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static void onUpgrade(SQLiteOpenHelper mSQLiteOpenHelper, SQLiteDatabase db) {
        List<String> tables = TableHelper.extractTableName(db);
        try {
            db.beginTransaction();
            for (String tableName : tables) {
                String tempTableName = tableName + "_Temp";
                //将原来是表 renanme 为临时表
                String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        //创建新表
        mSQLiteOpenHelper.onCreate(db);
        TableHelper.restoreData(db, tables);
    }
}

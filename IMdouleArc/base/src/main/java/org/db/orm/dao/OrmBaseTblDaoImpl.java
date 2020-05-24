package org.db.orm.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import org.db.orm.annotation.Column;
import org.db.orm.annotation.Id;
import org.db.orm.annotation.Table;
import org.db.orm.dao.helper.OrmTabColumn;
import org.db.orm.helper.BaseOrmColumns;
import org.db.orm.utils.SqliteLog;
import org.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
@SuppressWarnings("unchecked")
public abstract class OrmBaseTblDaoImpl<T> implements IOrmTableDao<T> {
    private static final String TAG = "OrmBaseTblDaoImpl";
    //数据库对象
    public SQLiteDatabase db = null;
    //当前dao类的class
    public Class<T> clazz;
    //表名
    public String tableName;
    //所有Field,Column
    private List<OrmTabColumn> fieldORColumnList;
    //id字段
    public String idColumn;

    public OrmBaseTblDaoImpl(Class<T> clazz) {
        db = getSQLiteOpenHelper().getReadableDatabase();
        fieldORColumnList = new ArrayList<>();
        if (clazz == null) {
            this.clazz = ((Class<T>) ((ParameterizedType) super
                    .getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0]);
        } else {
            this.clazz = clazz;
        }

        if (this.clazz.isAnnotationPresent(Table.class)) {
            Table table = this.clazz.getAnnotation(Table.class);
            this.tableName = table.name();
        }

        Field[] allFields = this.clazz.getDeclaredFields();
        OrmTabColumn ormTabColumn;
        // 找到ID，field，Column， Foreign ID装入OrmTabColumn
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                ormTabColumn = new OrmTabColumn();
                Column column = field.getAnnotation(Column.class);
                ormTabColumn.setName(column.name());
                ormTabColumn.setForeign(column.foreign());
                ormTabColumn.setLength(column.length());
                ormTabColumn.setType(column.type());
                ormTabColumn.setField(field);
                ormTabColumn.setFieldType(field.getType());
                if (field.isAnnotationPresent(Id.class)) {
                    this.idColumn = column.name();
                }
                //存入Foreign的ID
                if (ormTabColumn.isForeign()) {
                    Field[] fields = ormTabColumn.getFieldType().getDeclaredFields();
                    for (Field f : fields) {
                        if (f.isAnnotationPresent(Id.class)) {
                            f.setAccessible(true);
                            ormTabColumn.setForeignField(f);
                            ormTabColumn.setForeignFieldType(f.getType());
                            break;
                        }
                    }
                }
                fieldORColumnList.add(ormTabColumn);
            }
        }

        SqliteLog.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName
                + " idColumn:" + this.idColumn);
    }

    public abstract SQLiteOpenHelper getSQLiteOpenHelper();

    @Override
    public void insert(T entity) {
        String sql;
        try {
            sql = buildInsertSql(entity);
            SqliteLog.d(TAG, "[insert]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[insert] into DB Exception: " + e.toString());
        }
    }

    @Override
    public long insertReturnId(T entity) {
        int id = -1;
        Cursor cursor = null;
        try {
            String sql = buildInsertSql(entity);
            SqliteLog.d(TAG, "[insert]: insert into " + this.tableName + " " + sql);
            db.execSQL(sql);
            cursor = db.rawQuery("select LAST_INSERT_ROWID() from " + this.tableName, null);
            cursor.moveToFirst();
            id = cursor.getInt(0);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[insert] into DB Exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return id;
    }

    @Override
    public void insertIfNotExist(T entity) {
        if (!isExist(entity)) {
            insert(entity);
        }
    }

    @Override
    public void insertList(List<T> entityList) {
        //开启事务
        db.beginTransaction();
        try {
            //批量处理操作
            for (T entity : entityList) {
                db.execSQL(buildInsertSql(entity));
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            SqliteLog.e(TAG, e.toString());
        } finally {
            //结束事务
            db.endTransaction();
        }
    }

    public void insertListData(List<T> entityList) {
        //开启事务
        db.beginTransaction();
        try {
            //批量处理操作
            for (T entity : entityList) {
                db.insert(this.tableName, null, buildContentValues(entity));
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            SqliteLog.e(TAG, e.toString());
        } finally {
            //结束事务
            db.endTransaction();
        }
    }

    private ContentValues buildContentValues(T entity) throws IllegalAccessException {
        ContentValues values = new ContentValues(fieldORColumnList.size());
        String value;
        Field field;
        Object fieldValue;
        for (OrmTabColumn fieldORColumn : fieldORColumnList) {
            field = fieldORColumn.getField();
            fieldValue = field.get(entity);
            if (fieldValue == null) {
                continue;
            }
            if (!fieldORColumn.isForeign()) {
                if ((field.isAnnotationPresent(Id.class)
                        && field.getAnnotation(Id.class).type() == BaseOrmColumns.TYPE_INCREMENT)) {
                    continue;
                }
                value = String.valueOf(fieldValue);
            } else {
                //取出关联表主键
                Object foreignValue = fieldORColumn.getForeignField().get(fieldValue);
                value = String.valueOf(foreignValue);
            }
            values.put(fieldORColumn.getName(), value);
        }
        return values;
    }

    @Override
    public void delete(int id) {
        try {
            String where = this.idColumn + " = " + "'" + Integer.toString(id) + "'";
            String sql = buildDeleteSql(where);
            SqliteLog.d(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            SqliteLog.d(TAG, "[delete] DB Exception: " + e.toString());
        }
    }

    @Override
    public void delete(String where) {
        try {
            String sql = buildDeleteSql(where);
            SqliteLog.e(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception ex) {
            SqliteLog.e(TAG, ex.toString());
        }
    }

    @Override
    public void deleteAll() {
        try {
            String sql = buildDeleteSql(null);
            SqliteLog.d(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            SqliteLog.d(TAG, "[delete] DB Exception：" + e.toString());
        }
    }

    @Override
    public void update(T entity) {
        try {
            String sql = buildUpdateSql(entity, null);
            SqliteLog.d(TAG, "[update] :" + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[update] DB Exception: " + e.toString());
        }
    }

    @Override
    public void update(String sqlWhere){
        SqliteLog.d(TAG, "[update] :" + sqlWhere);
        db.execSQL(sqlWhere);
    }

    @Override
    public void update(T entity, String where) {
        try {
            String sql = buildUpdateSql(entity, where);
            SqliteLog.d(TAG, "[update] :" + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[update] DB Exception: " + e.toString());
        }
    }

    @Override
    public int update(ContentValues contentValues, String where, String[] whereValue) {
        int rows = 0;
        try {
            rows = db.update(this.tableName, contentValues, where, whereValue);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[update] DB Exception: " + e.toString());
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public void insertSaveOrUpdate(T entity) {
        if (isExist(entity)) {
            update(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public List<T> queryList() {
        return queryList(null, null, null, null, null, null, null);
    }

    @Override
    public T queryOne(long id) {
        String selection = this.idColumn + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        List<T> list = queryList(null, selection, selectionArgs, null, null, null,
                null);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public T queryOne(String[] columns, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(this.tableName, columns, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                return excuteQuery(columns, cursor);
            }
        } catch (Exception e) {
            SqliteLog.e(TAG, "[queryOne] from DB exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        SqliteLog.e(TAG,"[queryOne] query result cursor is null");
        return null;
    }

    @Override
    public List<T> queryList(String selection, String[] selectionArgs) {
        return queryList(null, selection, selectionArgs, null, null, null, null);
    }

    @Override
    public int queryCount(String selection, String[] selectionArgs) {
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(this.tableName, null, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            SqliteLog.e(TAG, "[queryCount] from DB exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public int queryCount(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(this.tableName, columns, selection, selectionArgs, null, null, orderBy);
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            SqliteLog.e(TAG, "[queryCount] from DB exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            for (String s : selectionArgs) {
                sql = sql.replaceFirst("\\?", "'" + String.valueOf(s) + "'");
            }
            SqliteLog.d(TAG, "[rawQuery]: " + sql);
            cursor = db.rawQuery(sql, selectionArgs);
            executeQuery(list, cursor);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[rawQuery] from DB Exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public List<T> queryList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return this.queryList(false, this.tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public List<T> queryList(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return this.queryList(false, this.tableName, columns, selection, selectionArgs, null, null, orderBy, null);
    }

    @Override
    public List<T> queryList(boolean distinct, String tables, String[] columns, String where, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = buildQueryString(distinct, tables, columns, where, groupBy, having, orderBy, limit);
            SqliteLog.d(TAG, "[queryList] : " + sql);
            cursor = db.rawQueryWithFactory(null, sql, selectionArgs, null, null);
            executeQuery(list, cursor);
        } catch (Exception e) {
            SqliteLog.e(TAG, "[queryList] from DB Exception：" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    public String buildQueryString(boolean distinct, String tables, String[] columns, String where, String groupBy, String having, String orderBy, String limit) {
        if (TextUtils.isEmpty(groupBy) && !TextUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!TextUtils.isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }
        StringBuilder query = new StringBuilder(120);
        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append("* ");
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }

    private void appendClause(StringBuilder s, String name, String clause) {
        if (!TextUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }


    public void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;
        for (int i = 0; i < n; i++) {
            String column = columns[i];
            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }


    @Override
    public boolean isExist(T obj) {
        Cursor cursor = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(*) from ").append(this.tableName).append(" where (");
            List<String> strings = new ArrayList<>();

            Field[] fields = obj.getClass().getDeclaredFields();
            Column column;
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Column.class)) continue;//如果不是注解字段循环下一个
                column = field.getAnnotation(Column.class);
                if (column.foreign()) continue;//这里默认不去比较关联外键
                String selectionArg = field.get(obj).toString();
                if (!StringUtils.isEmpty(selectionArg)) {
                    sb.append(column.name()).append("=?").append(" and ");
                    strings.add(selectionArg);
                }
            }
            sb.delete(sb.length() - 5, sb.length() - 1).append(")");
            String sql = sb.toString();
            SqliteLog.d(TAG, "[isExist]sql: " + sql);
            cursor = db.rawQuery(sql, strings.toArray(new String[strings.size()]));
            if (null != cursor && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (Exception ex) {
            SqliteLog.e(TAG, ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public boolean isExist(String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = db.query(this.tableName, null, selection, selectionArgs, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (Exception e) {
            SqliteLog.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public void execSql(String sql, Object[] selectionArgs) {
        try {
            db.execSQL(sql, selectionArgs);
        } catch (Exception ex) {
            SqliteLog.e(TAG, ex.toString());
        }
    }

    /**
     * 构造更新sql语句.
     *
     * @param entity 映射实体
     * @return sql语句
     */
    private String buildUpdateSql(final T entity, String where) throws IllegalAccessException {
        StringBuilder updateSql = new StringBuilder("update " + this.tableName + " set ");

        Field field;
        Object fieldValue;
        String value;
        for (OrmTabColumn fieldORColumn : fieldORColumnList) {
            field = fieldORColumn.getField();
            fieldValue = field.get(entity);
            if (fieldValue == null)
                continue;
            if (!fieldORColumn.isForeign()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (where == null) {
                        where = this.idColumn + " = " + "'" + String.valueOf(fieldValue) + "'";
                    }
                    continue;
                }
                value = String.valueOf(fieldValue);
            } else {
                //取出关联表主键
                Object foreignValue = fieldORColumn.getForeignField().get(fieldValue);
                value = String.valueOf(foreignValue);
            }
            updateSql.append(fieldORColumn.getName()).append("=").append("'").append(
                    value).append("',");
        }
        return updateSql.deleteCharAt(updateSql.length() - 1)
                .append(" where ")
                .append(where)
                .toString();
    }

    /**
     * 构造插入sql语句
     *
     * @param entity 映射实体
     * @return sql语句
     */
    private String buildInsertSql(final T entity) throws IllegalAccessException {
        StringBuilder insertSql = new StringBuilder("insert into " + this.tableName + " (");
        StringBuilder valueSb = new StringBuilder(" values (");

        String value;
        Field field;
        Object fieldValue;
        for (OrmTabColumn fieldORColumn : fieldORColumnList) {
            field = fieldORColumn.getField();
            fieldValue = field.get(entity);
            if (fieldValue == null) {
                continue;
            }
            if (!fieldORColumn.isForeign()) {
                if ((field.isAnnotationPresent(Id.class)
                        && field.getAnnotation(Id.class).type() == BaseOrmColumns.TYPE_INCREMENT)) {
                    continue;
                }
                value = String.valueOf(fieldValue);
            } else {
                //取出关联表主键
                Object foreignValue = fieldORColumn.getForeignField().get(fieldValue);
                value = String.valueOf(foreignValue);
            }
            insertSql.append(fieldORColumn.getName()).append(",");
            valueSb.append("'").append(value).append("',");
        }
        insertSql.deleteCharAt(insertSql.length() - 1).append(")");
        valueSb.deleteCharAt(valueSb.length() - 1).append(")");
        return insertSql.append(valueSb).toString();
    }

    /**
     * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
     *
     * @param strWhere 删除条件
     * @return sql语句
     */
    public String buildDeleteSql(String strWhere) {
        StringBuilder strSQL = new StringBuilder("delete from " + this.tableName);
        if (!StringUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }
        return strSQL.toString();
    }

    /**
     * 查询数据库表并自动装箱到clazz对象中
     *
     * @throws Exception
     */
    public void executeQuery(List<T> list, Cursor cursor) throws Exception {
        Object entity;
        Field field;
        while (cursor.moveToNext()) {
            entity = clazz.newInstance();
            for (OrmTabColumn fieldORColumn : fieldORColumnList) {
                field = fieldORColumn.getField();
                int c = cursor.getColumnIndex(fieldORColumn.getName());
                if (c < 0) continue; // 如果不存在则循环下个属性值
                //如果是关联关系就先不加载关联数据库表，只返回外键
                if (fieldORColumn.isForeign()) {
                    Object foreignEntity = fieldORColumn.getFieldType().newInstance();
                    if (!cursor.isNull(c)) {
                        executeField(clazz, fieldORColumn.getForeignField(), foreignEntity, cursor, fieldORColumn.getForeignFieldType(), c);
                        field.set(entity, foreignEntity);
                    }
                } else {
                    executeField(clazz, field, entity, cursor, fieldORColumn.getFieldType(), c);
                }
            }
            list.add((T) entity);
        }
    }


    public T excuteQuery(String[] columns, Cursor cursor) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T entity = null;
        while (cursor.moveToNext()) {
            entity = clazz.newInstance();
            for (int i = 0; i < columns.length; i++) {
                int columnIndex = cursor.getColumnIndex(columns[i]);
                if (columnIndex < 0) continue;
                Field mField = clazz.getField(columns[i]);
                if (mField != null)
                    executeSetField(mField, entity, cursor, mField.getType(), columnIndex);
            }
        }
        return entity;
    }


    String SET_PREFIX = "set";
    String methodName;
    String tblColumn;

    public void executeField(Class<T> clazz, Field field, Object entity, Cursor cursor, Class<?> fieldType, int c) throws Exception {
        tblColumn = field.getName();
        methodName = tblColumn.substring(0, 1).toUpperCase() + tblColumn.substring(1);
        Method setMethod = clazz.getDeclaredMethod(SET_PREFIX + methodName, new Class[]{field.getType()});
        if ((Integer.TYPE == fieldType)
                || (Integer.class == fieldType)) {
            //field.set(entity, cursor.getInt(c));
            setMethod.invoke(entity, cursor.getInt(c));
        } else if (String.class == fieldType) {
            //field.set(entity, cursor.getString(c));
            setMethod.invoke(entity, cursor.getString(c));
        } else if ((Long.TYPE == fieldType)
                || (Long.class == fieldType)) {
            // field.set(entity, cursor.getLong(c));
            setMethod.invoke(entity, cursor.getLong(c));
        } else if ((Float.TYPE == fieldType)
                || (Float.class == fieldType)) {
            // field.set(entity, cursor.getFloat(c));
            setMethod.invoke(entity, cursor.getFloat(c));
        } else if ((Short.TYPE == fieldType)
                || (Short.class == fieldType)) {
            // field.set(entity, cursor.getShort(c));
            setMethod.invoke(entity, cursor.getShort(c));
        } else if ((Double.TYPE == fieldType)
                || (Double.class == fieldType)) {
            // field.set(entity, cursor.getDouble(c));
            setMethod.invoke(entity, cursor.getDouble(c));
        } else if (Date.class == fieldType) {
            Date date = new Date();
            date.setTime(cursor.getLong(c));
            //field.set(entity, date);
            setMethod.invoke(entity, cursor.getLong(c));
        } else if (Blob.class == fieldType) {
            // field.set(entity, cursor.getBlob(c));
            setMethod.invoke(entity, cursor.getBlob(c));
        } else if (Character.TYPE == fieldType) {
            String fieldValue = cursor.getString(c);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // field.set(entity, fieldValue.charAt(0));
                setMethod.invoke(entity, fieldValue.charAt(0));
            }
        } else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
            String temp = cursor.getString(c);
            if ("true".equals(temp) || "1".equals(temp)) {
                //field.set(entity, true);
                setMethod.invoke(entity, true);
            } else {
                setMethod.invoke(entity, false);
                // field.set(entity, false);
            }
        }
    }

    public void executeSetField(Field field, Object entity, Cursor cursor, Class<?> fieldType, int c) throws IllegalAccessException {
        if ((Integer.TYPE == fieldType)
                || (Integer.class == fieldType)) {
            field.set(entity, cursor.getInt(c));
        } else if (String.class == fieldType) {
            field.set(entity, cursor.getString(c));
        } else if ((Long.TYPE == fieldType)
                || (Long.class == fieldType)) {
            field.set(entity, cursor.getLong(c));
        } else if ((Float.TYPE == fieldType)
                || (Float.class == fieldType)) {
            field.set(entity, cursor.getFloat(c));
        } else if ((Short.TYPE == fieldType)
                || (Short.class == fieldType)) {
            field.set(entity, cursor.getShort(c));
        } else if ((Double.TYPE == fieldType)
                || (Double.class == fieldType)) {
            field.set(entity, cursor.getDouble(c));
        } else if (Date.class == fieldType) {
            Date date = new Date();
            date.setTime(cursor.getLong(c));
            field.set(entity, date);
        } else if (Blob.class == fieldType) {
            field.set(entity, cursor.getBlob(c));
        } else if (Character.TYPE == fieldType) {
            String fieldValue = cursor.getString(c);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                field.set(entity, fieldValue.charAt(0));
            }
        } else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
            String temp = cursor.getString(c);
            if ("true".equals(temp) || "1".equals(temp)) {
                field.set(entity, true);
            } else {
                field.set(entity, false);
            }
        }
    }
}

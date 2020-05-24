package org.db.orm.helper;

/**
 *
 */
public interface BaseOrmColumns {

    /**
     * The unique ID for a row.
     * <P>Type: INTEGER (long)</P>
     */
    String _ID = "_id";

    /**
     * 主键自增.
     */
    int TYPE_INCREMENT = 1;

    /**
     * 主键自己插入.
     */
    int TYPE_NOT_INCREMENT = 0;

    /**
     * 添加字段column
     */
    String ADD_COLUMN = "ADD_COLUMN";
}

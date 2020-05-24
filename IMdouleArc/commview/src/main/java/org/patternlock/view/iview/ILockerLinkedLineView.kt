package org.patternlock.view.iview

import android.graphics.Canvas
import org.patternlock.view.data.CellData


/**
 * File: ILockerLinkedLineView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 1:06 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create ILockerLinkedLineView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
interface ILockerLinkedLineView {

    /**
     *绘制图案密码之间的连接线
     */
    fun drawLine(canvas: Canvas,
                 hitIndexList:List<Int>,
                 cellData:List<CellData>,
                 endX:Float,
                 endY:Float,
                 isError:Boolean)

}
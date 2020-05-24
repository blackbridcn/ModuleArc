package org.patternlock.view.iview

import android.graphics.Canvas
import org.patternlock.view.data.CellData


/**
 * File: IIndicatorLinkedLineView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 7:29 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create IIndicatorLinkedLineView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
interface IIndicatorLinkedLineView {

    /**
     * 绘制指示器连接线
     *
     * @param canvas
     * @param hitIndexList
     * @param cellBeanList
     * @param isError
     */
    fun draw(canvas: Canvas,
             hitIndexList: List<Int>,
             cellBeanList: List<CellData>,
             isError: Boolean)
}
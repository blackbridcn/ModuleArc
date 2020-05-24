package org.patternlock.view.iview

import android.graphics.Canvas
import org.patternlock.view.data.CellData


/**
 * File: IHitCellView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 5:15 PM
 * Description:定义 ： 绘制已经连接的每个点的图样样式的 interface 接口
 * -----------------------------------------------------------------
 * 2020/2/27 : Create IHitCellView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
interface IHitDotView {

    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    fun draw(canvas: Canvas, cellData: CellData, isError: Boolean)

}
package org.patternlock.view.iview

import android.graphics.Canvas
import org.patternlock.view.data.CellData


/**
 * File: INormalCellView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 1:51 PM
 * Description: 绘制正常情况时 每个的点的样式的 Interface 接口
 * -----------------------------------------------------------------
 * 2020/2/27 : Create INormalCellView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
interface IDotView {

    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param CellData the target cell view
     */
    fun drawDop(canvas: Canvas, targetData: CellData)

}
package org.patternlock.view.data

import kotlin.math.sqrt


/**
 * File: CellData.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 5:02 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create CellData.java (yuzhuzhang);
 *
 *  @param id 表示该cell的编号，9个cell的编号如下：
 *
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * @param x 表示该cell的x坐标（相对坐标）
 * @param y 表示该cell的y坐标（相对坐标）
 * @param radius 表示该cell的半径
 * @param isHit 表示该cell是否被设置的标记
 * -----------------------------------------------------------------
 */
data class CellData (val id: Int, val x: Float, val y: Float, val radius: Float, var isHit: Boolean = false) {
    /**
     * 是否触碰到该view
     *
     * @param x
     * @param y
     * @return
     */
    fun hitCell(x: Float, y: Float, enableSkip: Boolean): Boolean {
        val dx = this.x - x
        val dy = this.y - y
        val r = if (enableSkip) this.radius else this.radius * 1.5f
        return sqrt((dx * dx + dy * dy).toDouble()) <= r.toDouble()
    }
}
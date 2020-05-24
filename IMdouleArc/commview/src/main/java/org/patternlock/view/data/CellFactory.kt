package org.patternlock.view.data


/**
 * File: CellFactory.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 7:43 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create CellFactory.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
internal class CellFactory(private val width: Int, private val height: Int) {
    val cellBeanList: List<CellData> by lazy {
        val result = ArrayList<CellData>()
        result.clear()

        val pWidth = this.width / 8f
        val pHeight = this.height / 8f

        for (i: Int in 0..2) {
            for (j: Int in 0..2) {
                val id = (i * 3 + j) % 9
                val x = (j * 3 + 1) * pWidth
                val y = (i * 3 + 1) * pHeight
                result.add(CellData(id, x, y, pWidth))
            }
        }
       // Logger.d("CellFactory", "result = $result")
        return@lazy result
    }
}
package org.patternlock.def.data

import android.graphics.Canvas
import android.graphics.Paint
import org.patternlock.view.data.CellData
import org.patternlock.view.iview.IDotView


/**
 * File: DefaultLockeDotCellView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 5:34 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create DefaultLockeDotCellView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
class DefaultLockerDotView(val styleDecorator: DefaultStyleDecorator) : IDotView {
    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun drawDop(canvas: Canvas, cellBean: CellData) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.styleDecorator.normalColor
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        // draw fill circle
        this.paint.color = this.styleDecorator.fillColor
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.styleDecorator.lineWidth, this.paint)

        canvas.restoreToCount(saveCount)
    }
}

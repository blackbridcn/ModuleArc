package org.patternlock.def.data

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import org.patternlock.view.data.CellData
import org.patternlock.view.iview.IHitDotView


/**
 * File: DefaultLockerHitCellView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 5:46 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create DefaultLockerHitCellView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
class DefaultLockerHitDotView (val styleDecorator: DefaultStyleDecorator) : IHitDotView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellData, isError: Boolean) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        // draw fill circle
        this.paint.color = this.styleDecorator.fillColor
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.styleDecorator.lineWidth, this.paint)

        // draw inner circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 5f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}
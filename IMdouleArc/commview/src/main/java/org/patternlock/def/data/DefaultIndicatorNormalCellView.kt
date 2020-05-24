package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import org.patternlock.def.data.DefaultConfig
import org.patternlock.def.data.DefaultStyleDecorator
import org.patternlock.view.data.CellData
import org.patternlock.view.iview.IDotView

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorNormalCellView(val styleDecorator: DefaultStyleDecorator) : IDotView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun drawDop(canvas: Canvas, cellBean: CellData) {
        val saveCount = canvas.save()

        //outer circle
        this.paint.color = this.styleDecorator.normalColor
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        //inner circle
        this.paint.color = this.styleDecorator.fillColor
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.styleDecorator.lineWidth, this.paint)

        canvas.restoreToCount(saveCount)
    }
}
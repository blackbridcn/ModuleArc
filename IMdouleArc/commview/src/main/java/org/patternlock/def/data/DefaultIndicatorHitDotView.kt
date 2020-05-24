package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import org.patternlock.def.data.DefaultConfig
import org.patternlock.def.data.DefaultStyleDecorator
import org.patternlock.view.data.CellData
import org.patternlock.view.iview.IHitDotView


/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorHitDotView(val styleDecorator: DefaultStyleDecorator) : IHitDotView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellData, isError: Boolean) {
        val saveCount = canvas.save()

        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}
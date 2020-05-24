package org.patternlock.view.iview.impl

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import org.patternlock.view.data.CellData
import org.patternlock.view.iview.IHitDotView


/**
 * File: RippleLockerHitCellView.java
 * Author: yuzhuzhang
 * Create: 2020/3/1 10:42 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/1 : Create RippleLockerHitCellView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
class RippleLockerHitCellView : IHitDotView {

    @ColorInt
    private var hitColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0

    private val paint: Paint = Paint()

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        this.paint.style = Paint.Style.FILL
    }

    @ColorInt
    fun getHitColor(): Int {
        return hitColor
    }

    fun setHitColor(@ColorInt hitColor: Int): RippleLockerHitCellView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): RippleLockerHitCellView {
        this.errorColor = errorColor
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellData, isError: Boolean) {
        val saveCount = canvas.save()

        this.paint.color = getColor(isError) and 0x14FFFFFF
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        this.paint.color = getColor(isError) and 0x43FFFFFF
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius * 2f / 3f, this.paint)

        this.paint.color = getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getHitColor()
    }
}

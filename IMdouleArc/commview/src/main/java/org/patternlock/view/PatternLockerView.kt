package org.patternlock.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.commview.R
import org.patternlock.def.data.*
import org.patternlock.view.data.CellData
import org.patternlock.view.data.CellFactory
import org.patternlock.view.iview.IDotView
import org.patternlock.view.iview.IHitDotView
import org.patternlock.view.iview.ILockerLinkedLineView
import org.patternlock.view.iview.OnPatternChangeListener
import kotlin.math.min


/**
 * File: PatternLockerView.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 12:31 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create PatternLockerView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
class PatternLockerView @JvmOverloads constructor(mContext: Context, attrs:AttributeSet?=null, defStyleAttr:Int=0): View(mContext,attrs,defStyleAttr) {
    //Context context, AttributeSet attrs
    companion object {
        private const val TAG = "PatternLockerView"
    }

    /**
     * 绘制完后是否自动清除标志位，如果开启了该标志位，延时@freezeDuration毫秒后自动清除已绘制图案
     */
    var enableAutoClean: Boolean = false

    /**
     * 能否跳过中间点标志位，如果开启了该标志，则可以不用连续
     */
    var enableSkip: Boolean = false

    /**
     * 是否开启触碰反馈，如果开启了该标志，则每连接一个cell则会震动
     */
    var enableHapticFeedback: Boolean = false

    /**
     * 绘制完成后多久可以清除（单位ms），只有在@enableAutoClean = true 时有效
     */
    var freezeDuration: Int = 0

    /**
     * 绘制连接线
     */
    var linkedLineView: ILockerLinkedLineView? = null

    /**
     * 绘制未操作时的cell样式
     */
    var dotView: IDotView? = null

    /**
     * 绘制操作时的cell样式
     */
    var hitDotView: IHitDotView? = null

    /**
     * 是否是错误的图案
     */
    private var isError: Boolean = false

    /**
     * 终点x坐标
     */
    private var endX: Float = 0f

    /**
     * 终点y坐标
     */
    private var endY: Float = 0f

    /**
     * 记录绘制多少个cell，用于判断是否调用OnPatternChangeListener
     */
    private var hitSize: Int = 0

    /**
     * 真正的cell数组
     */
    private lateinit var cellDataList: List<CellData>

    /**
     * 记录已绘制的cell的id
     */
    private val hitIndexList: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }

    /**
     * 监听器
     */
    private var listener: OnPatternChangeListener? = null

    init {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }


    fun enableDebug() {
       // Logger.enable = true
    }

    fun setOnPatternChangedListener(listener: OnPatternChangeListener?) {
        this.listener = listener
    }

    /**
     * 更改状态
     */
    fun updateStatus(isError: Boolean) {
        this.isError = isError
        invalidate()
    }

    /* */
    fun clearHitState() {
        this.clearHitData()
        this.isError = false
        if (this.listener != null) {
            this.listener!!.onClear(this)
        }
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        this.initCellBeanList()
        this.drawLinkedLine(canvas)
        this.drawCells(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return super.onTouchEvent(event)
        }

        var isHandle = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.handleActionDown(event)
                isHandle = true
            }
            MotionEvent.ACTION_MOVE -> {
                this.handleActionMove(event)
                isHandle = true
            }
            MotionEvent.ACTION_UP -> {
                this.handleActionUp(event)
                isHandle = true
            }
            else -> {
            }
        }
        invalidate()
        return if (isHandle) true else super.onTouchEvent(event)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0)

        val normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, DefaultConfig.defaultNormalColor)
        val hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, DefaultConfig.defaultHitColor)
        val errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, DefaultConfig.defaultErrorColor)
        val fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, DefaultConfig.defaultFillColor)
        val lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, DefaultConfig.getDefaultLineWidth(resources))

        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_plv_freezeDuration, DefaultConfig.defaultFreezeDuration)
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, DefaultConfig.defaultEnableAutoClean)
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, DefaultConfig.defaultEnableHapticFeedback)
        this.enableSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_enableSkip, DefaultConfig.defaultEnableSkip)

        ta.recycle()

        // style
        val styleDecorator = DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth)
        this.dotView = DefaultLockerDotView(styleDecorator)
        this.hitDotView = DefaultLockerHitDotView(styleDecorator)
        this.linkedLineView = DefaultLockerLinkedLineView(styleDecorator)
    }

    private fun initData() {
       // Logger.enable = DefaultConfig.defaultEnableLogger
        this.hitIndexList.clear()
    }

    private fun initCellBeanList() {
        if (!this::cellDataList.isInitialized) {
            val w = this.width - this.paddingLeft - this.paddingRight
            val h = this.height - this.paddingTop - this.paddingBottom
            this.cellDataList = CellFactory(w, h).cellBeanList
        }
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (this.hitIndexList.isNotEmpty()) {
            this.linkedLineView?.drawLine(canvas,
                    this.hitIndexList,
                    this.cellDataList,
                    this.endX,
                    this.endY,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        this.cellDataList.forEach {
            if (it.isHit && this.hitDotView != null) {
                this.hitDotView?.draw(canvas, it, this.isError)
            } else {
                this.dotView?.drawDop(canvas, it)
            }
        }
    }

    private fun handleActionDown(event: MotionEvent) {
        //1. reset to default state
        this.clearHitData()

        //2. update hit state
        this.updateHitState(event)

        //3. notify listener
        this.listener?.onStart(this)
    }

    private fun handleActionMove(event: MotionEvent) {
        printLogger()

        //1. update hit state
        this.updateHitState(event)

        //2. update end point
        this.endX = event.x
        this.endY = event.y

        //3. notify listener if needed
        val size = this.hitIndexList.size
        if (this.hitSize != size) {
            this.hitSize = size
            this.listener?.onChange(this, this.hitIndexList)
        }
    }

    private fun handleActionUp(event: MotionEvent) {
        this.printLogger()

        //1. update hit state
        this.updateHitState(event)

        //2. update end point
        this.endX = 0f
        this.endY = 0f

        //3. notify listener
        this.listener?.onComplete(this, this.hitIndexList)


        //4. startTimer if needed
        if (this.enableAutoClean && this.hitIndexList.size > 0) {
            this.startTimer()
        }
    }

    private fun updateHitState(event: MotionEvent) {
        this.cellDataList.forEach {
            if (!it.isHit && it.hitCell(event.x, event.y, this.enableSkip)) {
                it.isHit = true
                this.hitIndexList.add(it.id)
                this.hapticFeedback()
            }
        }
    }

    //清理缓存【清楚之前的点数据】
    private fun clearHitData() {
        if (this.hitIndexList.isNotEmpty()) {
            this.hitIndexList.clear()
            this.hitSize = 0
            this.cellDataList.forEach { it.isHit = false }
        }
    }

    override fun onDetachedFromWindow() {
        this.setOnPatternChangedListener(null)
        this.removeCallbacks(this.action)
        super.onDetachedFromWindow()
    }

    private val action = Runnable {
        this.isEnabled = true
        this.clearHitState()
    }

    private fun startTimer() {
        this.isEnabled = false
        this.postDelayed(this.action, this.freezeDuration.toLong())
    }

    private fun hapticFeedback() {
        if (this.enableHapticFeedback) {
            this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
        }
    }

    private fun printLogger() {
      //  if (Logger.enable) {
           // Logger.d(TAG, "cellBeanList = ${this.cellBeanList}, hitIndexList = ${this.hitIndexList}")
       // }
    }

}
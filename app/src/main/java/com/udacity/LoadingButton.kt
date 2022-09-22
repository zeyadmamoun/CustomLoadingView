package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var defaultBackgroundColor = 0
    private var defaultTextColor = 0

    private var progress: Int = 0
    private var circleProgress: Int = 0

    private var rectValueAnimator = ValueAnimator()
    private var circleValueAnimator = ValueAnimator.ofInt(0,360)

    private var buttonText = "Download file"
    private lateinit var  textPaint: Paint

    private val backgroundRectPaint = Paint().apply {
        color = context.getColor(R.color.colorPrimaryDark)
        style = Paint.Style.FILL
    }

    private val circularProgressPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when(new){
            ButtonState.Clicked ->{
                startLoadingAnimation()
                buttonText = "Downloading"
                invalidate()
            }
            ButtonState.Loading ->{

            }
            ButtonState.Completed ->{
                buttonText = "Download File"
                progress = 0
                invalidate()
            }
        }
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs,R.styleable.LoadingButton,defStyleAttr,0)
        try {
            defaultTextColor = typedArray.getColor(R.styleable.LoadingButton_textColor,Color.WHITE)
            defaultBackgroundColor = typedArray.getColor(R.styleable.LoadingButton_backgroundColor,context.getColor(R.color.colorPrimary))
        }finally {
            typedArray.recycle()
        }

        textPaint = Paint().apply {
            color = defaultTextColor
            textSize = 40f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create("", Typeface.BOLD)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(defaultBackgroundColor)
        drawProgressRect(canvas)
        drawDownloadWord(canvas)
        drawProgressCircle(canvas)
    }

    private fun drawProgressRect(canvas: Canvas?) {
        canvas?.drawRect(0f,0f,progress.toFloat(),heightSize.toFloat(),backgroundRectPaint)
    }

    private fun drawProgressCircle(canvas: Canvas?){
        canvas?.drawArc(
            (width * 0.65).toFloat(),
            (height * 0.3).toFloat(),
            (width * 0.7).toFloat(),
            (height * 0.63).toFloat(),0f, circleProgress.toFloat(),true,circularProgressPaint)
    }

    private fun drawDownloadWord(canvas: Canvas?) {
        canvas?.drawText( buttonText,
            (widthSize /2).toFloat(),
            (heightSize/2 + 10).toFloat(),
            textPaint
        )
    }

    private fun startLoadingAnimation() {
        rectValueAnimator = ValueAnimator.ofInt(0,width).setDuration(1000).apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            interpolator = DecelerateInterpolator()
            disableButton(this@LoadingButton)
            start()
        }

        circleValueAnimator.setDuration(1000).apply {
            addUpdateListener {
                circleProgress = it.animatedValue as Int
                invalidate()
            }
            interpolator = DecelerateInterpolator()
            disableButton(this@LoadingButton)
            start()
        }
    }

    private fun ValueAnimator.disableButton(view: View){
        addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
                buttonState = ButtonState.Completed
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}

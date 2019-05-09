package com.example.customviewtask

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * Class that implements custom RadioButton.
 *
 * @author Alexander Gorin
 */
class ColorfulRadioButton(context: Context?, attrs: AttributeSet?) :
    AppCompatRadioButton(context, attrs) {

    var circleColor: Int = Color.BLACK
        set(value) {
            field = value
            generateStrokeColor(value)
        }
    var strokeColor = Color.argb(
        (Color.alpha(Color.BLACK) * 0.4).toInt(),
        Color.red(Color.BLACK),
        Color.green(Color.BLACK),
        Color.blue(Color.BLACK)
    )
        private set

    private val paintCircle = Paint().apply {
        style = Paint.Style.FILL
    }

    init {
        val typedArrayAttrs =
            context?.obtainStyledAttributes(attrs, R.styleable.ColorfulRadioButton)
        circleColor = typedArrayAttrs?.getColor(
            R.styleable.ColorfulRadioButton_color,
            getThemeAccentColor(context)
        ) ?: Color.BLACK
        typedArrayAttrs?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        paintCircle.color = strokeColor
        if (isChecked) {
            canvas?.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                (width / 2).toFloat(),
                paintCircle
            )
        }
        paintCircle.color = circleColor
        canvas?.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 3).toFloat(),
            paintCircle
        )
    }

    private fun generateStrokeColor(value: Int) {
        strokeColor = Color.argb(
            (Color.alpha(value) * 0.4).toInt(),
            Color.red(value),
            Color.green(value),
            Color.blue(value)
        )
    }
}
package com.example.customviewtask

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.core.view.get
import androidx.core.view.size
import kotlinx.android.synthetic.main.paint_widget.view.*

/**
 * Class that implements custom widget. It consists of [SeekBar] with custom drawable background,
 * TextViews to show width value and text, [RadioGroup] with ColorfulRadiobuttons for picking color.
 *
 * @author Alexander Gorin
 */
class PaintWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var maxWidth = 0
        private set

    var defaultColorPosition = 0
        private set

    var firstItemColor = 0
        private set

    var onPaintWidgetChangeListener: OnPaintWidgetChangeListener =
        context as OnPaintWidgetChangeListener

    private val layerDrawable by lazy { seekBarWidget.progressDrawable as LayerDrawable }
    private val rightPart by lazy { layerDrawable.findDrawableByLayerId(R.id.seekbar_right_part) as GradientDrawable }
    private val leftPart by lazy { layerDrawable.findDrawableByLayerId(R.id.seekbar_left_part) as ScaleDrawable }

    init {
        inflate(context, R.layout.paint_widget, this)

        val typedArrayAttrs = context.obtainStyledAttributes(attrs, R.styleable.PaintWidget)
        maxWidth = typedArrayAttrs.getInteger(R.styleable.PaintWidget_maxWidth, 10)
        defaultColorPosition =
            typedArrayAttrs.getInteger(R.styleable.PaintWidget_defaultColorPosition, 0)
        firstItemColor =
            typedArrayAttrs.getColor(R.styleable.PaintWidget_firstItemColor, Color.BLACK)
        typedArrayAttrs?.recycle()

        setDefaultColorPosition(defaultColorPosition)
        setFirstItemColor(firstItemColor)
        setMaxWidth(maxWidth)

        seekBarWidget.setOnSeekBarChangeListener(SeekBarChangeListener())
        radioGroup.setOnCheckedChangeListener(RadioGroupCheckedListener())

        /*setDefaultColorPosition(DEFAULT_COLOR_POSITION)
        setMaxWidth(MAX_WIDTH)*/
    }


    private inner class SeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            widthTextView.text = progress.toString()
            onPaintWidgetChangeListener.onWidthChanged(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    private inner class RadioGroupCheckedListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            val checkedButton = findViewById<ColorfulRadioButton>(checkedId)
            val leftColor = checkedButton?.circleColor
            val rightColor = checkedButton?.strokeColor
            changeSeekBarColor(leftColor, rightColor)
            leftColor?.let {
                onPaintWidgetChangeListener.onColorChanged(it)
            }
        }
    }

    private fun changeSeekBarColor(circleColor: Int?, strokeColor: Int?) {
        circleColor?.let { leftPart.setTint(it) }
        strokeColor?.let { rightPart.setColor(it) }
    }

    private fun setRadioButtonChecked(num: Int) {
        val radioButton = radioGroup[num] as ColorfulRadioButton
        radioButton.isChecked = true
        changeSeekBarColor(radioButton.circleColor, radioButton.strokeColor)
    }

    fun setMaxWidth(value: Int) {
        maxWidth = value
        seekBarWidget.max = value
    }

    fun setDefaultColorPosition(value: Int) {
        if (radioGroup.size > value) {
            defaultColorPosition = value
            setRadioButtonChecked(value)
        } else {
            defaultColorPosition = 0
            setRadioButtonChecked(0)
        }
    }

    fun setFirstItemColor(value: Int) {
        firstItemColor = value
        val radioButton = radioGroup[0] as ColorfulRadioButton
        radioButton.setButtonColors(value)
        if (radioButton.isChecked) {
            changeSeekBarColor(
                radioButton.circleColor,
                radioButton.strokeColor
            )
        }
    }

    interface OnPaintWidgetChangeListener {
        fun onWidthChanged(widthValue: Int)
        fun onColorChanged(colorValue: Int)
    }

    companion object {
        const val DEFAULT_COLOR_POSITION = 3
        const val MAX_WIDTH = 5
    }
}
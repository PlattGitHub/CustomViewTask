package com.example.customviewtask

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import android.view.View
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
        set(value) {
            field = value
            seekBarWidget.max = value
        }

    var defaultColorPosition = 0
        set(value) {
            if (radioGroup.size > value) {
                field = value
                setRadioButtonChecked(value)
            } else {
                field = 0
                setRadioButtonChecked(0)
            }
        }

    var firstItemColor = 0
        set(value) {
            field = value
            val radioButton = radioGroup[0] as ColorfulRadioButton
            radioButton.circleColor = value
            if (radioButton.isChecked) {
                changeSeekBarColor(
                    radioButton.circleColor,
                    radioButton.strokeColor
                )
            }
        }

    var onPaintWidgetChangeListener: OnPaintWidgetChangeListener? = null
    var view: View? = null

    private val layerDrawable by lazy { seekBarWidget.progressDrawable as LayerDrawable }
    private val rightPart by lazy { layerDrawable.findDrawableByLayerId(R.id.seekbar_right_part) as GradientDrawable }
    private val leftPart by lazy { layerDrawable.findDrawableByLayerId(R.id.seekbar_left_part) as ScaleDrawable }

    init {
        view = View.inflate(context, R.layout.paint_widget, this)

        seekBarWidget.setOnSeekBarChangeListener(SeekBarChangeListener())
        radioGroup.setOnCheckedChangeListener(RadioGroupCheckedListener())

        val typedArrayAttrs = context.obtainStyledAttributes(attrs, R.styleable.PaintWidget)
        maxWidth = typedArrayAttrs.getInteger(R.styleable.PaintWidget_maxWidth, 10)
        defaultColorPosition =
            typedArrayAttrs.getInteger(R.styleable.PaintWidget_defaultColorPosition, 0)
        firstItemColor =
            typedArrayAttrs.getColor(R.styleable.PaintWidget_firstItemColor, Color.BLACK)
        typedArrayAttrs?.recycle()
    }


    private inner class SeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            widthTextView.text = progress.toString()
            onPaintWidgetChangeListener?.onWidthChange(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    private inner class RadioGroupCheckedListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            val checkedButton = view?.findViewById<ColorfulRadioButton>(checkedId)
            val leftColor = checkedButton?.circleColor
            val rightColor = checkedButton?.strokeColor
            changeSeekBarColor(leftColor, rightColor)
            leftColor?.let {
                onPaintWidgetChangeListener?.onColorChange(it)
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

    interface OnPaintWidgetChangeListener {
        fun onWidthChange(widthValue: Int)
        fun onColorChange(colorValue: Int)
    }
}
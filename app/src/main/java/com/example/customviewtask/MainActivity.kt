package com.example.customviewtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Simple Activity that displays [PaintWidget] with SwitchButton which is responsible
 * for showing that custom view.
 * [MainActivity] also implements [PaintWidget.OnPaintWidgetChangeListener] callbacks.
 *
 * @author Alexander Gorin
 */
class MainActivity : AppCompatActivity(), PaintWidget.OnPaintWidgetChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showPaintWidgetSwitch.setOnCheckedChangeListener { _, isChecked ->
            paintWidget.isVisible = isChecked
        }

        //0..3
        paintWidget.defaultColorPosition = 3
        paintWidget.maxWidth = 5
        paintWidget.onPaintWidgetChangeListener = this

    }

    override fun onWidthChange(widthValue: Int) {
        toast("Width has changed. Value is $widthValue")
    }

    override fun onColorChange(colorValue: Int) {
        toast("Color has changed. Value is ${String.format("#%06X", (colorValue))}")
    }
}

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
        paintWidget.defaultColorPosition = PaintWidget.DEFAULT_COLOR_POSITION
        paintWidget.maxWidth = PaintWidget.MAX_WIDTH
    }

    override fun onWidthChanged(widthValue: Int) {
        toast(String.format(getString(R.string.width_changed), widthValue))
    }

    override fun onColorChanged(colorValue: Int) {
        toast(
            String.format(
                getString(R.string.color_changed),
                String.format(getString(R.string.color_format), colorValue)
            )
        )
    }
}

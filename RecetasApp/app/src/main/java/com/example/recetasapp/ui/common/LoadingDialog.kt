package com.example.recetasapp.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import com.example.recetasapp.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
    }

    fun show(message: String? = null) {
        // Actualizar mensaje si se proporciona
        super.show()
    }

    companion object {
        private var instance: LoadingDialog? = null

        fun show(context: Context) {
            instance?.dismiss()
            instance = LoadingDialog(context)
            instance?.show()
        }

        fun hide() {
            instance?.dismiss()
            instance = null
        }
    }
}
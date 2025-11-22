package com.example.recetasapp.ui.common

import androidx.fragment.app.Fragment
import com.example.recetasapp.utils.showToast

abstract class BaseFragment : Fragment() {

    protected fun showMessage(message: String) {
        requireContext().showToast(message)
    }

    protected fun showError(error: String) {
        requireContext().showToast("Error: $error")
    }

    protected fun showLoading(show: Boolean) {
        // TODO: Implementar loading dialog
    }
}
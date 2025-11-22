package com.example.recetasapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.recetasapp.R
import com.example.recetasapp.utils.PreferencesHelper
import com.example.recetasapp.utils.showToast

class LanguageFragment : Fragment() {

    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var languageRadioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())
        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        languageRadioGroup = view.findViewById(R.id.language_radio_group)
    }

    private fun setupListeners() {
        languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.spanish_radio -> {
                    requireContext().showToast("Idioma: Español")
                }
                R.id.english_radio -> {
                    requireContext().showToast("Language: English (Coming soon)")
                }
                R.id.portuguese_radio -> {
                    requireContext().showToast("Idioma: Português (Em breve)")
                }
            }
        }
    }
}
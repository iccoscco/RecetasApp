package com.example.recetasapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recetasapp.R
import com.example.recetasapp.utils.showToast

class UploadRecipeFragment : Fragment() {

    private lateinit var recipeImage: ImageView
    private lateinit var recipeTitleEdit: EditText
    private lateinit var recipeDescriptionEdit: EditText
    private lateinit var uploadPhotoButton: Button
    private lateinit var publishButton: Button
    private lateinit var saveDraftButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        recipeImage = view.findViewById(R.id.recipe_image)
        recipeTitleEdit = view.findViewById(R.id.recipe_title_edit)
        recipeDescriptionEdit = view.findViewById(R.id.recipe_description_edit)
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button)
        publishButton = view.findViewById(R.id.publish_button)
        saveDraftButton = view.findViewById(R.id.save_draft_button)
    }

    private fun setupListeners() {
        uploadPhotoButton.setOnClickListener {
            requireContext().showToast("Subir foto próximamente")
        }

        publishButton.setOnClickListener {
            if (validateForm()) {
                requireContext().showToast("Receta publicada!")
                findNavController().navigateUp()
            }
        }

        saveDraftButton.setOnClickListener {
            requireContext().showToast("Borrador guardado")
            findNavController().navigateUp()
        }
    }

    private fun validateForm(): Boolean {
        val title = recipeTitleEdit.text.toString().trim()
        val description = recipeDescriptionEdit.text.toString().trim()

        if (title.isEmpty()) {
            recipeTitleEdit.error = "Ingresa un título"
            return false
        }

        if (description.isEmpty()) {
            recipeDescriptionEdit.error = "Ingresa una descripción"
            return false
        }

        return true
    }
}
package com.example.recetasapp.ui.cookingmode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recetasapp.R

// Activity del modo de cocina
class CookingModeActivity : AppCompatActivity() {

    private lateinit var viewModel: CookingModeViewModel
    private var recipeId: String = ""

    private lateinit var stepTitle: TextView
    private lateinit var stepDescription: TextView
    private lateinit var stepCounter: TextView
    private lateinit var timerText: TextView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooking_mode)

        // Obtiene el ID de la receta
        recipeId = intent.getStringExtra("recipeId") ?: ""

        initViewModel()
        initViews()
        setupListeners()
        setupObservers()
        startForegroundService()

        // Cargar receta
        if (recipeId.isNotEmpty()) {
            viewModel.loadRecipe(recipeId)
        }
    }

    private fun initViewModel() {
        val factory = CookingModeViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[CookingModeViewModel::class.java]
    }

    private fun initViews() {
        stepTitle = findViewById(R.id.step_title)
        stepDescription = findViewById(R.id.step_description)
        stepCounter = findViewById(R.id.step_counter)
        timerText = findViewById(R.id.timer_text)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        finishButton = findViewById(R.id.finish_button)
    }

    // Actualiza la UI automáticamente cuando:
    private fun setupListeners() {
        previousButton.setOnClickListener {
            viewModel.previousStep()
        }

        nextButton.setOnClickListener {
            viewModel.nextStep()
        }

        finishButton.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.currentStep.observe(this) { step ->
            stepCounter.text = "Paso ${viewModel.currentStepIndex.value} de ${viewModel.totalSteps.value}"
            stepTitle.text = "Paso ${viewModel.currentStepIndex.value}"
            stepDescription.text = step
        }

        viewModel.currentStepIndex.observe(this) { index ->
            previousButton.isEnabled = index > 1
            nextButton.isEnabled = index < (viewModel.totalSteps.value ?: 0)
        }

        viewModel.timerSeconds.observe(this) { seconds ->
            val minutes = seconds / 60
            val secs = seconds % 60
            timerText.text = String.format("%02d:%02d", minutes, secs)
        }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, CookingModeService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, CookingModeService::class.java)
        stopService(serviceIntent)
    }
}
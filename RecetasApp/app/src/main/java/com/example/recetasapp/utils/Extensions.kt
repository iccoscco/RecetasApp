package com.example.recetasapp.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recetasapp.R

// Funciones para strings
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    return this.matches(emailRegex.toRegex())
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}

// Funciones para vistas
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

// Funciones para Context
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Funciones para ImageView
fun ImageView.loadUrl(
    url: String,
    placeholder: Int = R.drawable.ic_placeholder,
    error: Int = R.drawable.ic_error
) {
    Glide.with(this.context)
        .load(url)
        .placeholder(placeholder)
        .error(error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadCircular(url: String) {
    Glide.with(this.context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_error)
        .into(this)
}

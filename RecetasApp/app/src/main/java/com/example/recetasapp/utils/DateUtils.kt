package com.example.recetasapp.utils

import java.text.SimpleDateFormat
import java.util.*

// Objeto para manejar fechas
object DateUtils {
    private const val DATE_FORMAT_DEFAULT = "dd/MM/yyyy"
    private const val DATE_FORMAT_FULL = "dd 'de' MMMM 'de' yyyy"
    private const val TIME_FORMAT_DEFAULT = "HH:mm"
    private const val DATETIME_FORMAT_DEFAULT = "dd/MM/yyyy HH:mm"

    fun getCurrentDate(): Date {
        return Date()
    }

    fun formatDate(date: Date, pattern: String = DATE_FORMAT_DEFAULT): String {
        val formatter = SimpleDateFormat(pattern, Locale("es", "ES"))
        return formatter.format(date)
    }

    fun formatTime(date: Date, pattern: String = TIME_FORMAT_DEFAULT): String {
        val formatter = SimpleDateFormat(pattern, Locale("es", "ES"))
        return formatter.format(date)
    }

    fun formatDateTime(date: Date, pattern: String = DATETIME_FORMAT_DEFAULT): String {
        val formatter = SimpleDateFormat(pattern, Locale("es", "ES"))
        return formatter.format(date)
    }

    fun parseDate(dateString: String, pattern: String = DATE_FORMAT_DEFAULT): Date? {
        return try {
            val formatter = SimpleDateFormat(pattern, Locale("es", "ES"))
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun getDayOfWeek(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> ""
        }
    }

    fun getMonthName(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> "Enero"
            Calendar.FEBRUARY -> "Febrero"
            Calendar.MARCH -> "Marzo"
            Calendar.APRIL -> "Abril"
            Calendar.MAY -> "Mayo"
            Calendar.JUNE -> "Junio"
            Calendar.JULY -> "Julio"
            Calendar.AUGUST -> "Agosto"
            Calendar.SEPTEMBER -> "Septiembre"
            Calendar.OCTOBER -> "Octubre"
            Calendar.NOVEMBER -> "Noviembre"
            Calendar.DECEMBER -> "Diciembre"
            else -> ""
        }
    }

    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    fun addHours(date: Date, hours: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }

    fun getTimeAgo(date: Date): String {
        val now = Date()
        val diff = now.time - date.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days día${if (days > 1) "s" else ""}"
            hours > 0 -> "$hours hora${if (hours > 1) "s" else ""}"
            minutes > 0 -> "$minutes minuto${if (minutes > 1) "s" else ""}"
            else -> "Ahora"
        }
    }

    fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.time = date
        val dateDay = calendar.get(Calendar.DAY_OF_YEAR)

        return today == dateDay
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.time = date1

        val calendar2 = Calendar.getInstance()
        calendar2.time = date2

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }
}
package com.rafaelduransaez.core.common.formatter

import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class DefaultNumberFormatter @Inject constructor() : NumberFormatter {
    private val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

    override fun format(value: Number): String = formatter.format(value)
}
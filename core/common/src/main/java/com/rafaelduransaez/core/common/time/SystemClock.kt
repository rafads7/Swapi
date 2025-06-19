package com.rafaelduransaez.core.common.time

import javax.inject.Inject

class SystemClock @Inject constructor() : Clock {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
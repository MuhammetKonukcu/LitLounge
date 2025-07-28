package com.muhammetkonukcu.litlounge.utils

import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.filter_all
import litlounge.composeapp.generated.resources.filter_finished
import litlounge.composeapp.generated.resources.filter_reading
import org.jetbrains.compose.resources.StringResource

enum class ReadingFilter(val labelRes: StringResource) {
    ALL(Res.string.filter_all),
    READING(Res.string.filter_reading),
    FINISHED(Res.string.filter_finished)
}
package com.rahul.cliproject.utils

fun String?.filterEmpty(): String {
    return this ?: ""
}
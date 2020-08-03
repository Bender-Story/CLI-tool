package com.rahul.cliproject.utils

fun Int?.filterEmpty() :Int{
    return this ?: 0
}
package com.github.itsempa.cyclabox.errors

class CommandError(message: String, cause: Throwable) : Error(message, cause)
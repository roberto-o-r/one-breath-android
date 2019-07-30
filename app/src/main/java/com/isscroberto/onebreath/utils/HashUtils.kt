package com.isscroberto.onebreath.utils

import java.security.MessageDigest

enum class Algorithm(val code: String) {
    SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512")
}

fun String.hashStringExtension (algorithm: Algorithm): String {
    val hexArray = "0123456789ABCDEF"
    val bytes = MessageDigest.getInstance(algorithm.code).digest(this.toByteArray())

    val hash = StringBuilder(bytes.size * 2)
    bytes.forEach {
        val i = it.toInt()
        hash.append(hexArray[i shr 4 and 0x0f])
        hash.append(hexArray[i and 0x0f])
    }

    return hash.toString()
}
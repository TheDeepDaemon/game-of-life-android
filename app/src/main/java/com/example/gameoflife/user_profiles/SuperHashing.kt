package com.example.gameoflife.user_profiles
import kotlin.random.Random
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter
import kotlin.experimental.xor


fun hash(bytes: ByteArray, type: String): ByteArray {
    return MessageDigest.getInstance(type).digest(bytes)
}

fun md5Hash(bytes: ByteArray): ByteArray {
    return hash(bytes, "MD5")
}

fun sha256Hash(bytes: ByteArray): ByteArray {
    return hash(bytes, "SHA-256")
}

fun sha512Hash(bytes: ByteArray): ByteArray {
    return hash(bytes, "SHA-512")
}

fun hashStep(bytes: ByteArray): ByteArray {
    val hash256 = sha256Hash(bytes)
    val hash512 = sha512Hash(bytes)

    val hex = DatatypeConverter.printHexBinary(md5Hash(bytes))
    val seed1: Long = hex.slice(0..8).toLong(16)
    val seed2: Long = hex.slice(8..16).toLong(16)

    val hash = hash512 + hash256

    val random1 = Random(seed1)
    val halfSize = hash.size / 2
    for (i in 0 until halfSize) {
        val b = random1.nextInt(256).toByte()
        hash[i] = hash[i] xor b
    }

    val random2 = Random(seed2)
    for (i in halfSize until hash.size) {
        val b = random2.nextInt(256).toByte()
        hash[i] = hash[i] xor b
    }

    hash.shuffle(Random(seed1))
    hash.shuffle(Random(seed2))
    return hash
}

fun randomString(length: Int, seed: Int) : String {
    val chars = (0 until 128).map { it.toChar() }
    val r = Random(seed)
    return (0 until length)
        .map { chars.random(r) }
        .joinToString("")
}

/*
* I am aware you are not normally supposed to write
* your own password hashing functions for security reasons.
* To clarify, I did this for fun.
 */
fun superHash(username: String, password: String): ByteArray {
    val str = username + password + randomString(16, 42)
    var bytes: ByteArray = str.toByteArray()
    for (i in 0 until 20000) {
        bytes = hashStep(bytes)
    }
    return bytes
}

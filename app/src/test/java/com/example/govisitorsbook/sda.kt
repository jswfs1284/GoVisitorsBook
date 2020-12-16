package com.example.govisitorsbook

import com.google.common.hash.Hashing
import org.junit.Test


class sda() {
    @Test
    fun testHashingSha256() {
        val hashFunction = Hashing.sha256()
        val hc = hashFunction
            .newHasher()
            .putString("dsdsdadadasfaafafafa", Charsets.UTF_8)
            .hash()
        val sha256 = hc.toString()
        println("sha256 = $sha256")
    }


}
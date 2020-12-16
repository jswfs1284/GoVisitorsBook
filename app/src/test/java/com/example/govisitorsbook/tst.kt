package com.example.govisitorsbook

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.squareup.okhttp.*
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.net.URISyntaxException


class tst{

    @Test
    fun BitmapToString(){
        lateinit var mSocket : Socket

        try{
            mSocket = IO.socket("http://150.95.198.45:5000")
            //Log.d("chatActivity socket", "connected")
        }catch(e : URISyntaxException){
            //Log.d("chatActivity socket", "failed")
        }

        mSocket.connect()

        mSocket.emit("stlog", "${"윤효진"} ${"01046051787"} ${"맘스터치"} ${"경기 하남시 미사강변대로 95"} ${"1608132659"} ${"14206fb54e01637cc095c317a72b5acd7a1f903aceee0a4e1a93904d9665b13b"} ${"1"}")

        mSocket.disconnect()
    }
}
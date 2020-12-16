package com.example.govisitorsbook

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.govisitorsbook.VisitHistory.VisitHistoryEntity
import com.example.govisitorsbook.database.DatabaseMod
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.common.hash.Hashing
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.android.synthetic.main.fragment_sign.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 효진님 소스 들어갈 Fragment
 * 캔버스로 서명 - 블록체인에 있는 서명과 비교
 */


class SignFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback

    // 엔터티 초기화
    private var visitHistoryEntity = VisitHistoryEntity(userId = "", address = "", visitDate = "")

    //noteDao 참조
    private val visitHistorydao by lazy { DatabaseMod.getDatabase(requireContext()).visitHistoryDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = arguments?.getString("Name").toString()
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        val imgPath = arguments?.getString("ImgPath").toString()
        val address = arguments?.getString("Address").toString()
        val shopName = arguments?.getString("Mall").toString()


        // 서명 인증 -> 방문기록 저장
        btn_confirm.setOnClickListener {
            if(true) { // 서명 인증 성공
                // 현재 일시
                val date = SimpleDateFormat("yyyy-MM-dd hh:mm")

                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    visitHistoryEntity = VisitHistoryEntity(
                        userId = userName,
                        address = address,
                        visitDate = date.format(Date())
                    )
                    visitHistorydao.insertVisitHistory(visitHistoryEntity)
                    findNavController().navigate(R.id.action_global_homeFragment)
                }
            } else {    // 서명 인증 실패
                Toast.makeText(requireContext(), "등록된 서명이 없습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_homeFragment)
            }

            view.signaturePad.buildDrawingCache()
            val sign = signaturePad.getDrawingCache()
            val fos: FileOutputStream // FileOutputStream 이용 파일 쓰기 한다
            val strFolderPath = Environment.getExternalStorageDirectory()
                .absolutePath
            val folder = File(strFolderPath)
            if (!folder.exists()) {  // 해당 폴더 없으면 만들어라
                folder.mkdirs()
            }
            val strFilePath =
                strFolderPath + "/" + System.currentTimeMillis() + ".png"
            val fileCacheItem = File(strFilePath)
            try {
                fos = FileOutputStream(fileCacheItem)
                sign.compress(Bitmap.CompressFormat.PNG, 100, fos)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                Toast.makeText(requireContext(), "사인을 캡쳐했습니다", Toast.LENGTH_SHORT).show()
            }

            val imgString = BitmapToString(sign)
            val imghash = HashingSha256(imgString)

            val time = System.currentTimeMillis() /1000         // 타임 스템프

            val client: OkHttpClient =
                OkHttpClient()

            lateinit var mSocket : Socket

            try{
                mSocket = IO.socket("http://150.95.198.45:5000")
                //Log.d("chatActivity socket", "connected")
            }catch(e : URISyntaxException){
                //Log.d("chatActivity socket", "failed")
            }

            mSocket.connect()

            mSocket.emit("stlog", "${userName} ${phoneNumber} ${shopName} ${address} ${time} ${imghash} ${"1"}")

            mSocket.disconnect()
        }
    }

    // 뒤로가기 누를 시 다시 QRFragment로 넘어가도록. (fragment_qr의 빈 레이아웃 나타나서 수동으로 해줌ㅠㅠ)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val bytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun HashingSha256(content : String?) : String {
        val hashFunction = Hashing.sha256()
        val hc = hashFunction
            .newHasher()
            .putString(content, Charsets.UTF_8)
            .hash()
        val sha256 = hc.toString()

        return sha256
    }

}
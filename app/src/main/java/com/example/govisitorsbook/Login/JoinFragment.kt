package com.example.govisitorsbook.Login

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.govisitorsbook.R
import com.example.govisitorsbook.database.DatabaseMod
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_join.*
import kotlinx.android.synthetic.main.fragment_join.view.*
import kotlinx.android.synthetic.main.fragment_join.view.btn_register
import kotlinx.android.synthetic.main.fragment_join.view.signaturePad
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * A simple [Fragment] subclass.
 */
class JoinFragment : Fragment() {



    val dao by lazy { DatabaseMod.getDatabase(requireContext()).userDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_join, container, false)

        root.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onClear() {

            }

            override fun onSigned() {

            }


        })


        root.btn_clear.setOnClickListener {
            signaturePad.clear()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btn_register.setOnClickListener {

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

            var userName = view.user_name.text.toString()
            var phoneNumber = view.phone_number.text.toString()

                if (userName.isEmpty() || phoneNumber.isEmpty())
                {
                    Toast.makeText(requireContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show()
                }
                else {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                        val user = User(
                            userName = userName,
                            phoneNumber = phoneNumber,
                            signPath = strFilePath
                        )
                        dao.insert(user)
                    }
                    findNavController().navigate(R.id.action_global_loginFragment)
                }

        }
    }


}

package com.example.govisitorsbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val userName = arguments?.getString("Name").toString()
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        val imgPath = arguments?.getString("ImgPath").toString()

        text_name.text = userName + "님"

        btn_QR.setOnClickListener {
            findNavController().navigate(R.id.QRFragment,Bundle().apply {
                putString("Name", userName)
                putString("phoneNumber", phoneNumber)
                putString("ImgPath", imgPath)
            })
        }
        btn_history.setOnClickListener {
            findNavController().navigate(R.id.visitHistoryFragment,Bundle().apply {
                putString("Name", userName)
            })
        }
    }

}

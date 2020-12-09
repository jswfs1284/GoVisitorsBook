package com.example.govisitorsbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign.*

/**
 * 효진님 소스 들어갈 Fragment
 * 캔버스로 서명 - 블록체인에 있는 서명과 비교
 */


class SignFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false)
    }

    // 뒤로가기 누를 시 다시 QRFragment로 넘어가도록. (fragment_qr의 빈 레이아웃 나타나서 수동으로 해줌ㅠㅠ)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                txt_temp.text = "occur back pressed event!!"

                findNavController().navigate(R.id.QRFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}

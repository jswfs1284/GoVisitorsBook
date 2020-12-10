package com.example.govisitorsbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.govisitorsbook.VisitHistory.VisitHistoryEntity
import com.example.govisitorsbook.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
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
    private val visitHistorydao by lazy { DatabaseModule.getDatabase(requireContext()).visitHistoryDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서명 인증 -> 방문기록 저장
        btn_confirm.setOnClickListener {
            if(false) { // 서명 인증 성공
                // 현재 일시
                val date = SimpleDateFormat("yyyy-MM-dd hh:mm")

                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    /*val visitHistoryEntity = VisitHistoryEntity(
                        userId = 유저아이디,
                        address = 가게주소,
                        visitDate = date.format(Date())
                    )
                    visitHistoryDao.insertVisitHistory(visitHistoryEntity)*/
                    findNavController().navigate(R.id.action_global_homeFragment)
                }
            } else {    // 서명 인증 실패
                Toast.makeText(requireContext(), "등록된 서명이 없습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_homeFragment)
            }

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

}
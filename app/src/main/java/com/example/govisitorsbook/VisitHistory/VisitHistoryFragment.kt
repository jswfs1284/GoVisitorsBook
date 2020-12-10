package com.example.govisitorsbook.VisitHistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.govisitorsbook.R
import com.example.govisitorsbook.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_visit_history.*
import kotlinx.android.synthetic.main.fragment_visit_history.view.*
import kotlinx.android.synthetic.main.fragment_visit_history.view.txt_empty
import java.text.SimpleDateFormat
import java.util.*

/**
 * 방문 기록 (장소, 일시)
 */
class VisitHistoryFragment : Fragment() {

    val dao by lazy { DatabaseModule.getDatabase(requireContext()).visitHistoryDao() }

    //어댑터 생성
    val adapter = VisitHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_visit_history, container, false)

        /* 어댑터 초기화*/
        rootView.list_visit_history.adapter = adapter
        rootView.list_visit_history.layoutManager = LinearLayoutManager(requireContext())

        return rootView
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 임시 id : test001 ====================> 여기 수정하기 !!!!!!!!!
        dao.selectVisitHistory(userId = "test001").observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            
            adapter.entities = it   //어댑터에 변경된 값 전달
            
            if(adapter.entities.isEmpty()) {    // 리스트에 값 없으면 "기록이 없습니다"
                view.txt_empty.visibility = View.VISIBLE
                view.list_visit_history.visibility = View.INVISIBLE
            } else {
                view.txt_empty.visibility = View.INVISIBLE
                view.list_visit_history.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()//어댑터에 변경 공지
        })
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

package com.example.govisitorsbook.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.govisitorsbook.R
import com.example.subsmanager2.util.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_join.*
import kotlinx.android.synthetic.main.fragment_join.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class JoinFragment : Fragment() {

    //FirebaseAuth 객체의 공유 인스턴스를 가져오기
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_join, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //회원 가입 버튼을 클릭한 경우
        view.btn_register.setOnClickListener {
            //id, pw, pw_firm 값 가져오기
            val id = view.edit_userid.text.toString()
            val pw = view.edit_password.text.toString()
            val name = view.edit_name.text.toString()
            val tel = view.edit_tel.text.toString()

            //id, pw, pw_firm 입력 여부 확인
            when {
                id.isEmpty() || pw.isEmpty() || name.isEmpty() || tel.isEmpty() -> Toast.makeText(requireContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show()
                else -> {
                    //id, pw, pw_firm 입력값이 정상이면
                    view.register_loader.visibility = View.VISIBLE

                    firebaseAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener { task ->
                            /* 성공한 경우*/
                            task.addOnSuccessListener {
                                // 수정필요!!!  RoomDB에 id, name, tel 저장해주기. (Firebase-Auth에는 id, pw만 저장됨)



                                //입력 필드 초기화
                                view.edit_userid.text = null
                                view.edit_password.text = null
                                view.edit_name.text = null
                                view.edit_tel.text = null

                                view.register_loader.visibility = View.GONE

                                hideKeyboard()
                                findNavController().navigate(R.id.action_global_homeFragment)
                            }
                            /* 실패한 경우*/
                            task.addOnFailureListener {
                                view.register_loader.visibility = View.GONE
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }//end of firebaseAuth.createUserWithEmailAndPassword.addOnCompleteListener
                }//end of when-else
            }//end of when
        }//end of view.btn_register.setOnClickListener
    }//end of onViewCreated

}

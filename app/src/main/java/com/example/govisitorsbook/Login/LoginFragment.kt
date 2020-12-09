package com.example.govisitorsbook.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.govisitorsbook.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 임시 버튼
        view.btn_temp.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }

        /* 회원가입 버튼 클릭했을 때 이벤트 */
        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }

        /* 로그인 버튼 클릭했을 때 이벤트 */
        btn_login.setOnClickListener {
            //입력한 id와 password 가져오기
            val id = view.edit_userid.text.toString()
            val password = view.edit_password.text.toString()

            //id와 password 입력여부 확인
            if (id.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "아이디/패스워드 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                //progress_loader VISIBLE
                view.progress_loader.visibility = View.VISIBLE

                /* 이메일 주소와 비밀번호로 사용자 로그인 처리하기
                   - FirebaseAuth.getInstance(): Initialize Firebase Auth
                   - id(이메일 주소)와 password를 signInWithEmailAndPassword()에 전달하여
                     유효성을 검사한 후, 등록된 user인경우 searchFragment로 이동
                 */

                FirebaseAuth.getInstance().signInWithEmailAndPassword(id, password)
                    .addOnCompleteListener { task ->
                        /* 등록된 user인 경우*/
                        task.addOnSuccessListener {
                            //입력 필드 초기화
                            view.edit_userid.text = null
                            view.edit_password.text = null
                            view.progress_loader.visibility = View.GONE

                            //hideKeyboard()
                            findNavController().navigate(R.id.action_global_homeFragment)
                        }
                        /* 등록한 user가 아닌 경우*/
                        task.addOnFailureListener {
                            view.progress_loader.visibility = View.GONE
                            /* Exception을 받아 에러메세지 출력 */
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }//end of FirebaseAuth.getInstance().signInWithEmailAndPassword.addOnCompleteListener
            }
        }
    }

}


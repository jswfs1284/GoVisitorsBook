package com.example.govisitorsbook.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.govisitorsbook.R
import com.example.govisitorsbook.database.DatabaseMod
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val dao by lazy { DatabaseMod.getDatabase(requireContext()).userDao() }

    var userList:List<User> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.recyc.adapter = UserAdapter(requireContext())
        view.recyc.layoutManager = LinearLayoutManager(requireContext())

        view.btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }

        dao.getUser().observe(viewLifecycleOwner, Observer {
            (view.recyc.adapter as UserAdapter).users = it
            (view.recyc.adapter as UserAdapter).notifyDataSetChanged()
        })




    }


}



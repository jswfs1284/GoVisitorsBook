package com.example.govisitorsbook.Login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.govisitorsbook.R
import com.example.govisitorsbook.database.DatabaseMod
import kotlinx.android.synthetic.main.list_uer.view.*

class UserAdapter(val context: Context, var users:List<User> = emptyList()):
    RecyclerView.Adapter<UserAdapter.Holder>(){

    val dao by lazy { DatabaseMod.getDatabase(context).userDao() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_uer,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(users[position],context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User,context: Context) {


            itemView.name.text = user.userName
            itemView.phone_number.text = user.phoneNumber

            /*if(user.autoLogin == true){
                Navigation.findNavController(itemView).navigate(R.id.action_global_homeFragment,
                    Bundle().apply {
                        putString("Name", user.userName)
                        putString("phoneNumber", user.phoneNumber)
                        putString("ImgPath", user.signPath)
                    }
                )
            }*/

            itemView.setOnClickListener {

                user.phoneNumber?.let { it1 -> dao.updateAuto(it1) }
                Navigation.findNavController(itemView).navigate(R.id.action_global_homeFragment,
                Bundle().apply {
                    putString("Name", user.userName)
                    putString("phoneNumber", user.phoneNumber)
                    putString("ImgPath", user.signPath)
                }
                )
            }

        }

    }
}
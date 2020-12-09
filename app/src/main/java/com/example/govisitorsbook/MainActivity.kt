package com.example.govisitorsbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = findNavController(R.id.navigation_host)

        NavigationUI.setupWithNavController(
            bottom_navigation, controller
        )

        controller.addOnDestinationChangedListener { _, destination, _ ->
            /* 최종 destination에서 네비바 없애기*/
            if (arrayListOf(
                    R.id.homeFragment, R.id.homeFragment
                ).contains(destination.id)
            ) {
                bottom_navigation.visibility = View.VISIBLE
            } else {
                bottom_navigation.visibility = View.GONE
            }
        }

        // Top Level Destination 설정. (뒤로가기 안보이게 하기)
        NavigationUI.setupActionBarWithNavController(
            this,
            controller,
            AppBarConfiguration(
                setOf(
                    R.id.loginFragment,
                    R.id.homeFragment
                )
            )
        )
    }

    override fun onActivityResult(  // QR코드 결과값 activity->fragment로 전달
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    // 뒤로가기 버튼 눌렀을 때, 안꺼지고 뒤로가기
    override fun onSupportNavigateUp() = findNavController(R.id.navigation_host).navigateUp()
}

package com.example.govisitorsbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator


/**
 * A simple [Fragment] subclass.
 * Use the [QRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Zxing lib 사용한 QR 인식
        val integrator = IntentIntegrator.forSupportFragment(this@QRFragment)

        integrator.setOrientationLocked(false)
        integrator.setPrompt("Scan QR code")
        integrator.setBeepEnabled(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)

        integrator.initiateScan()

        return inflater.inflate(R.layout.fragment_qr, container, false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_SHORT).show()
                // 테스트용 QR코드 발급 : https://the-qrcode-generator.com/ 테스트값 입력 (ex. 성남시 수정구)
                // 경재님 소스 들어갈 부분 (QR에서 가져온 값 - GPS값 비교)
                
                // 위치 인증 완료시 서명 Fragment 이동
                findNavController().navigate(R.id.action_QRFragment_to_signFragment)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}

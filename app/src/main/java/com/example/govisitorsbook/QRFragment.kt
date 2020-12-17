package com.example.govisitorsbook

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import java.io.IOException



/**
 * A simple [Fragment] subclass.
 * Use the [QRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRFragment : Fragment() {

    lateinit var userName : String
    lateinit var phoneNumber : String
    lateinit var imgPath : String

    lateinit var qrInfo : List<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        userName = arguments?.getString("Name").toString()
        phoneNumber = arguments?.getString("phoneNumber").toString()
        imgPath = arguments?.getString("ImgPath").toString()

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
        super.onActivityResult(requestCode, resultCode, data)

        var mLat: Double? = null
        var mLng: Double? = null


        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_SHORT).show()
                // 테스트용 QR코드 발급 : https://the-qrcode-generator.com/ 테스트값 입력 (ex. 성남시 수정구)
                // 경재님 소스 들어갈 부분 (QR에서 가져온 값 - GPS값 비교)

                qrInfo = result.contents.split(',')     // qrInfo[0] = 주소, qrInfo[1] = 매장명

                val mGeoCoder = Geocoder(requireContext())

                try {
                    val mResultLocation: List<Address> = mGeoCoder.getFromLocationName(
                        qrInfo[0], 1
                    )
                    mLat = mResultLocation.get(0).latitude
                    mLng = mResultLocation.get(0).longitude


                } catch (e: IOException) {
                    e.printStackTrace()
                }


                val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                var latitude: Double? = null
                var longtitude: Double? = null
                var distance: Double? = null
                //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
                if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(
                        requireContext().applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                } else {
                    when { //프로바이더 제공자 활성화 여부 체크
                        isNetworkEnabled -> {
                            val location =
                                lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음

                            longtitude = location?.longitude!!
                            latitude = location.latitude

                        }
                        isGPSEnabled -> {
                            val location =
                                lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음

                            longtitude = location?.longitude!!
                            latitude = location.latitude
                        }
                        else -> {

                        }
                    }

                }


                val locationA = Location("point A")

                locationA.latitude = latitude!!
                locationA.longitude = longtitude!!

                val locationB = Location("point B")
                locationB.latitude = mLat!!
                locationB.longitude = mLng!!

                /*locationB.latitude = 37.45075
                locationB.longitude = 127.12886*/


                distance = locationA.distanceTo(locationB).toDouble()





                lm.removeUpdates(gpsLocationListener)



                if (distance<1000){
                    findNavController().navigate(R.id.action_QRFragment_to_signFragment,Bundle().apply {
                        putString("Name", userName)
                        putString("phoneNumber", phoneNumber)
                        putString("ImgPath", imgPath)
                        putString("Address", qrInfo[0])
                        putString("Mall", qrInfo[1])
                    })
                    }
                else {
                    Toast.makeText(requireContext(), "현재 가게위치와 1km이내에 안계십니다", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                //뒤로가기 넣기


                // 위치 인증 완료시 서명 Fragment 이동

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude
        }

        //아래 3개함수는 형식상 필수부분
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


}
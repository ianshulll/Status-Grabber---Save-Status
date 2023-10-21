package com.anshul.statusgrabber.views.activities


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.anshul.statusgrabber.R
import com.anshul.statusgrabber.databinding.ActivityMainBinding
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.utils.SharedPrefKeys
import com.anshul.statusgrabber.utils.SharedPrefUtils
import com.anshul.statusgrabber.utils.replaceFragment
import com.anshul.statusgrabber.utils.slideFromStart
import com.anshul.statusgrabber.utils.slideToEndWithFadeOut
import com.anshul.statusgrabber.views.fragments.FragmentSettings
import com.anshul.statusgrabber.views.fragments.FragmentStatus
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    lateinit var mAdView : AdView
    var counter = 0

    private val activity = this
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        loadAds()
        val adRequest = AdRequest.Builder().build()

        binding.apply {

            // Admob
            MobileAds.initialize(activity) {
            }
            mAdView = findViewById(R.id.adView_banner)
            mAdView.loadAd(adRequest)



            SharedPrefUtils.init(activity)
            splashLogic()
            requestPermission()

            val fragmentWhatsappStatus = FragmentStatus()
            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
            replaceFragment(fragmentWhatsappStatus, bundle)

            bottomNavigationView.setOnItemSelectedListener {

                when(it.itemId){

                    R.id.menu_status->{
                        //Whatsapp status
                        counter++
                        val fragmentWhatsappStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
                        replaceFragment(fragmentWhatsappStatus, bundle)

                        if (counter == 5) {
                            showAd()
                            // Display the ad.
                            // Reset the counter after showing the ad.
                            counter = 0
                        }
                    }

                    R.id.menu_business_status->{
                        //Whatsapp  Business status
                        val fragmentWhatsappStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_BUSINESS)
                        replaceFragment(fragmentWhatsappStatus, bundle)

                    }

                    R.id.menu_settings->{
                        //Settings
                        replaceFragment(FragmentSettings())

                    }

                    
                }

                return@setOnItemSelectedListener true
            }

        }




//         Banner AD Functions
        mAdView.adListener = object: AdListener()
        {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked()
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                mAdView.loadAd(adRequest)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened()
            }
        }

    }


    private val PERMISSION_REQUEST_CODE = 50

    private fun requestPermission () {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val isPermissionsGranted = SharedPrefUtils.getPrefBoolean(
                SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                false
            )
            if (!isPermissionsGranted) {
                ActivityCompat.requestPermissions(
                    /* activity = */ activity,
                    /* permissions = */ arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    /* requestCode = */ PERMISSION_REQUEST_CODE
                )
                Toast.makeText(activity, "Please Grant Permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isGranted) {
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED, true)
            } else {
                SharedPrefUtils.putPrefBoolean(
                    SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                    false
                )

            }
        }
    }


    private fun splashLogic() {
            binding.apply {
                splashLayout.cardView.slideFromStart()
                Handler(Looper.myLooper()!!).postDelayed({
                    splashScreenHolder.slideToEndWithFadeOut()
                    splashScreenHolder.visibility = View.GONE
                }, 2000)

                splashLayout.splashTitle.slideFromStart()
                Handler(Looper.myLooper()!!).postDelayed({
                    splashScreenHolder.slideToEndWithFadeOut()
                    splashScreenHolder.visibility = View.GONE
                }, 2000)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager?.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }


    private fun loadAds(){

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-5200686692366657/4245840144", adRequest, object :
            InterstitialAdLoadCallback() {

            override fun onAdFailedToLoad(p0: LoadAdError) {

                mInterstitialAd = null

            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd

            }
        })

    }

    private fun showAd() {

        loadAds()
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        }
        else {
            Toast.makeText(activity,"", Toast.LENGTH_SHORT).show()
        }

    }

}

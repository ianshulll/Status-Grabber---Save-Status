package com.anshul.statusgrabber.views.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.anshul.statusgrabber.databinding.ActivityVideosPreviewBinding
import com.anshul.statusgrabber.models.MediaModel
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.views.adapters.VideoPreviewAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosPreview : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var counter = 0
    private val PREFS_KEY = "video_player_prefs"

    private val activity = this
    private val binding by lazy {
        ActivityVideosPreviewBinding.inflate(layoutInflater)
    }
    lateinit var adapter: VideoPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        loadAds()

        binding.apply {

            val list =
                intent.getSerializableExtra(Constants.MEDIA_LIST_KEY) as ArrayList<MediaModel>
            val scrollTo = intent.getIntExtra(Constants.MEDIA_SCROLL_KEY, 0)
            adapter = VideoPreviewAdapter(list, activity)
            videoRecyclerView.adapter = adapter

            val pageSnapHelper = PagerSnapHelper()
            pageSnapHelper.attachToRecyclerView(videoRecyclerView)
            videoRecyclerView.scrollToPosition(scrollTo)

            videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        stopAllPlayers()
                    }
                }


            })

            val prefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            counter = prefs.getInt("counter", 0)
        }


    }

    private fun stopAllPlayers() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.apply {
                    for (i in 0 until videoRecyclerView.childCount) {
                        val child = videoRecyclerView.getChildAt(i)
                        val viewHolder = videoRecyclerView.getChildViewHolder(child)
                        if (viewHolder is VideoPreviewAdapter.ViewHolder) {
                            viewHolder.stopPlayer()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopAllPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        counter++
        stopAllPlayers()

        if(counter % 3 == 0){
            showAd()
        }

        val prefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("counter", counter)
        editor.apply()
    }

    private fun loadAds(){

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-5200686692366657/5966115900", adRequest, object :
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
        if (mInterstitialAd != null ) {
            mInterstitialAd?.show(activity)
        }
        else {
            Toast.makeText(activity,"", Toast.LENGTH_SHORT).show()
        }

    }

}

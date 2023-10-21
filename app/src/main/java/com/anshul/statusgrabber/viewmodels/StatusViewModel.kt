package com.anshul.statusgrabber.viewmodels.factories

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshul.statusgrabber.data.StatusRepo
import com.anshul.statusgrabber.models.MEDIA_TYPE_IMAGE
import com.anshul.statusgrabber.models.MEDIA_TYPE_VIDEO
import com.anshul.statusgrabber.models.MediaModel
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.utils.SharedPrefKeys
import com.anshul.statusgrabber.utils.SharedPrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel (val repo: StatusRepo):ViewModel(){

    private val wpStatusLiveData get() = repo.whatsAppStatusesLiveData

    private val wpBusinessStatusLiveData get() = repo.whatsAppBusinessStatusesLiveData

    //wp main
    val whatsAppImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    //wp business
    val whatsAppBusinessImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    private var isPermissionsGranted = false

    init {
        SharedPrefUtils.init(repo.context)

        val wpPermissions = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,false)
        val wpBusinessPermissions = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,false)

        isPermissionsGranted = wpPermissions && wpBusinessPermissions

        if (isPermissionsGranted){
            CoroutineScope(Dispatchers.IO).launch {
                repo.getAllStatuses()
            }
            CoroutineScope(Dispatchers.IO).launch {
              repo.getAllStatuses(Constants.TYPE_WHATSAPP_BUSINESS)
            }
        }

    }

    fun getWhatsAppStatuses(){
        CoroutineScope(Dispatchers.IO).launch {
            if (!isPermissionsGranted){
                repo.getAllStatuses()
            }
            withContext(Dispatchers.Main){
                getWhatsAppImages()
                getWhatsAppVideos()
            }
        }
    }

    fun getWhatsAppImages(){
        wpStatusLiveData.observe(repo.activity as LifecycleOwner){
            val tempList = ArrayList<MediaModel>()
            it.forEach{ mediaModel ->
            if (mediaModel.type == MEDIA_TYPE_IMAGE){
                tempList.add(mediaModel)
               }
            }
            whatsAppImagesLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppVideos(){
        wpStatusLiveData.observe(repo.activity as LifecycleOwner){
            val tempList = ArrayList<MediaModel>()
            it.forEach{ mediaModel ->
            if (mediaModel.type == MEDIA_TYPE_VIDEO){
                tempList.add(mediaModel)
               }
            }
            whatsAppVideosLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppBusinessStatuses(){
        CoroutineScope(Dispatchers.IO).launch {
            if (!isPermissionsGranted){
                repo.getAllStatuses(Constants.TYPE_WHATSAPP_BUSINESS)
            }
            withContext(Dispatchers.Main){
                getWhatsAppBusinessImages()
                getWhatsAppBusinessVideos()
            }
        }
    }

    fun getWhatsAppBusinessImages(){
        wpBusinessStatusLiveData.observe(repo.activity as LifecycleOwner){
            val tempList = ArrayList<MediaModel>()
            it.forEach{ mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_IMAGE){
                    tempList.add(mediaModel)
                }
            }
            whatsAppBusinessImagesLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppBusinessVideos(){
        wpBusinessStatusLiveData.observe(repo.activity as LifecycleOwner){
            val tempList = ArrayList<MediaModel>()
            it.forEach{ mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_VIDEO){
                    tempList.add(mediaModel)
                }
            }
            whatsAppBusinessVideosLiveData.postValue(tempList)
        }
    }

}
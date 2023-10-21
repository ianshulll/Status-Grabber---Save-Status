package com.anshul.statusgrabber.data

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.anshul.statusgrabber.models.MEDIA_TYPE_IMAGE
import com.anshul.statusgrabber.models.MEDIA_TYPE_VIDEO
import com.anshul.statusgrabber.models.MediaModel
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.utils.SharedPrefKeys
import com.anshul.statusgrabber.utils.SharedPrefUtils
import com.anshul.statusgrabber.utils.getFileExtension
import com.anshul.statusgrabber.utils.isStatusExist


class StatusRepo(val context: Context) {

    val whatsAppStatusesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessStatusesLiveData = MutableLiveData<ArrayList<MediaModel>>()

    val activity = context as Activity

    private val wpStatusesList = ArrayList<MediaModel>()
    private val wpBusinessStatusesList = ArrayList<MediaModel>()

    fun getAllStatuses(whatsAppType:String = Constants.TYPE_WHATSAPP_MAIN){

        val treeUri = when(whatsAppType){
            Constants.TYPE_WHATSAPP_MAIN->{
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_TREE_URI,"")?.toUri()!!
            }
            else->{
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,"")?.toUri()!!
            }
        }

        activity.contentResolver.takePersistableUriPermission(
            treeUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        val fileDocument = DocumentFile.fromTreeUri(activity, treeUri)

        fileDocument?.let {
            it.listFiles().forEach { file->
                if (file.name != ".nomedia" && file.isFile){
                    val isDownloaded = context.isStatusExist(file.name!!)
                    Log.d("Status REPO", "getAllStatuses: Extension: ${getFileExtension(file.name!!)} || ${file.name}")
                    val type = if (getFileExtension(file.name!!) == "mp4"){
                        MEDIA_TYPE_VIDEO
                    }
                    else{
                        MEDIA_TYPE_IMAGE
                    }

                    val model = MediaModel(
                        pathUri = file.uri.toString(),
                        fileName = file.name!!,
                        type = type,
                        isDownloaded = isDownloaded
                    )
                    when(whatsAppType){
                        Constants.TYPE_WHATSAPP_MAIN->{
                            wpStatusesList.add(model)
                        }
                        else->{
                            wpBusinessStatusesList.add(model)
                        }
                    }
                }
            }
            when(whatsAppType){
                Constants.TYPE_WHATSAPP_MAIN->{
                    whatsAppStatusesLiveData.postValue(wpStatusesList)
                }
                else->{
                    whatsAppBusinessStatusesLiveData.postValue(wpBusinessStatusesList)
                }
            }
        }
    }
}
package com.anshul.statusgrabber.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.anshul.statusgrabber.R
import com.anshul.statusgrabber.databinding.ItemMediaBinding
import com.anshul.statusgrabber.models.MEDIA_TYPE_IMAGE
import com.anshul.statusgrabber.models.MediaModel
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.utils.saveStatus
import com.anshul.statusgrabber.views.activities.ImagesPreview
import com.anshul.statusgrabber.views.activities.VideosPreview
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdView

class MediaAdapter(val list:ArrayList<MediaModel>, val context: Context):
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {


    inner class  ViewHolder(val binding:ItemMediaBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(mediaModel: MediaModel){
            binding.apply {



                Glide.with(context)
                    .load(mediaModel.pathUri.toUri())
                    .into(statusImage)


                if (mediaModel.type == MEDIA_TYPE_IMAGE)
                    statusPlay.visibility = View.GONE
                val downloadImage = if (mediaModel.isDownloaded){
                    R.drawable.ic_downloaded
                }
                else{
                    R.drawable.ic_download
                }
                statusDownload.setImageResource(downloadImage)

                cardStatus.setOnClickListener{
                    if (mediaModel.type == MEDIA_TYPE_IMAGE){
                        // goto image preview activity
                        Intent().apply {
                            putExtra(Constants.MEDIA_LIST_KEY, list)
                            putExtra(Constants.MEDIA_SCROLL_KEY, layoutPosition)
                            setClass(context, ImagesPreview::class.java)
                            context.startActivity(this)
                        }
                    }
                    else{
                            // goto video preview activity
                            Intent().apply {
                                putExtra(Constants.MEDIA_LIST_KEY,list)
                                putExtra(Constants.MEDIA_SCROLL_KEY,layoutPosition)
                                setClass(context, VideosPreview::class.java)
                                context.startActivity(this)
                         }

                    }
                }
                statusDownload.setOnClickListener {
                    val isDownload = context.saveStatus(mediaModel)
                    if (isDownload){
                        //status is downloaded
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        statusDownload.setImageResource(R.drawable.ic_downloaded)
                    }
                    else{
                        //unable to download status
                        Toast.makeText(context, "Unable to Save", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMediaBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

}
package com.anshul.statusgrabber.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anshul.statusgrabber.data.StatusRepo
import com.anshul.statusgrabber.databinding.FragmentMediaBinding
import com.anshul.statusgrabber.models.MediaModel
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.viewmodels.factories.StatusViewModel
import com.anshul.statusgrabber.viewmodels.factories.StatusViewModelFactory
import com.anshul.statusgrabber.views.adapters.MediaAdapter


class FragmentMedia : Fragment() {
    private val binding by lazy{
        FragmentMediaBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: StatusViewModel
    lateinit var adapter: MediaAdapter

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            arguments?.let {

                val repo = StatusRepo(requireActivity())
                viewModel = ViewModelProvider(requireActivity(),
                    StatusViewModelFactory(repo))[StatusViewModel::class.java]

                val mediaType = it.getString(Constants.MEDIA_TYPE_KEY,"")


                when(mediaType){
                    Constants.MEDIA_TYPE_WHATSAPP_IMAGES->{
                        viewModel.whatsAppImagesLiveData.observe(requireActivity()) { unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach{model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0){
                                tempMediaText.visibility = View.VISIBLE
                            }
                            else{
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }
                    Constants.MEDIA_TYPE_WHATSAPP_VIDEOS->{
                        viewModel.whatsAppVideosLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach{model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter

                            if (list.size == 0){
                                tempMediaText.visibility = View.VISIBLE
                            }
                            else{
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }
                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES->{
                        viewModel.whatsAppBusinessImagesLiveData.observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach{model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter

                            if (list.size == 0){
                                tempMediaText.visibility = View.VISIBLE
                            }
                            else{
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }
                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS->{
                        viewModel.whatsAppBusinessVideosLiveData
                            .observe(requireActivity()){unFilteredList->
                            val filteredList = unFilteredList.distinctBy { model->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach{model->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                              if (list.size == 0){
                                  tempMediaText.visibility = View.VISIBLE
                               }
                              else{
                                  tempMediaText.visibility = View.GONE
                               }
                        }
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}
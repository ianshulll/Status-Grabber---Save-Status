package com.anshul.statusgrabber.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anshul.statusgrabber.data.StatusRepo
import com.anshul.statusgrabber.databinding.FragmentStatusBinding
import com.anshul.statusgrabber.utils.Constants
import com.anshul.statusgrabber.utils.SharedPrefKeys
import com.anshul.statusgrabber.utils.SharedPrefUtils
import com.anshul.statusgrabber.utils.getFolderPermissions
import com.anshul.statusgrabber.viewmodels.factories.StatusViewModel
import com.anshul.statusgrabber.viewmodels.factories.StatusViewModelFactory
import com.anshul.statusgrabber.views.adapters.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentStatus : Fragment() {

    private val binding by lazy{
        FragmentStatusBinding.inflate(layoutInflater)
    }

    private  lateinit var type:String
    private val  WHATSAPP_REQUEST_CODE = 101
    private val  WHATSAPP_BUSINESS_REQUEST_CODE = 102


    private val viewPagerTitles = arrayListOf("Images","Videos")
    lateinit var viewModel:StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            arguments?.let {

                val repo = StatusRepo(requireActivity())
                viewModel = ViewModelProvider(requireActivity(),
                    StatusViewModelFactory(repo))[StatusViewModel::class.java]

                type = it.getString((Constants.FRAGMENT_TYPE_KEY),"")


                when(type){
                    Constants.TYPE_WHATSAPP_MAIN->{

                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,false
                        )
                        if (isPermissionGranted){
                            getWhatsAppStatuses()

                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatuses()
                            }
                        }
                        permissionLayout.btnPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_REQUEST_CODE,
                                initialUri = Constants.getWhatsappUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity())
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager){tab,pos->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                        binding.swipeRefreshLayout.setOnRefreshListener {
                            refreshStatuses()
                        }

                    }
                    Constants.TYPE_WHATSAPP_BUSINESS->{

                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,
                            false
                        )
                        if (isPermissionGranted){
                            getWhatsAppBusinessStatuses()

                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatuses()
                            }
                        }

                        permissionLayout.btnPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_BUSINESS_REQUEST_CODE,
                                initialUri = Constants.getWhatsappBusinessUri()
                            )
                        }
                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES,
                            videosType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS)

                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager){tab,pos->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    fun refreshStatuses() {
        when (type) {
            Constants.TYPE_WHATSAPP_MAIN -> {
                Toast.makeText(requireActivity(), "Refreshing Status", Toast.LENGTH_SHORT)
                    .show()
                getWhatsAppStatuses()
            }

            else -> {
                Toast.makeText(
                    requireActivity(),
                    "Refreshing Status",
                    Toast.LENGTH_SHORT
                ).show()
                getWhatsAppBusinessStatuses()
            }
        }

        Handler(Looper.myLooper()!!).postDelayed({
            binding.swipeRefreshLayout.isRefreshing = false
        }, 2000)
    }



    fun getWhatsAppStatuses(){

        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppStatuses()
    }

    fun getWhatsAppBusinessStatuses(){
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppBusinessStatuses()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == AppCompatActivity.RESULT_OK){
            val treeUri = data?.data!!

            requireActivity().contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (requestCode == WHATSAPP_REQUEST_CODE){

                SharedPrefUtils.putPrefString(SharedPrefKeys.PREF_KEY_WP_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, true)
                getWhatsAppStatuses()
            }
            else if (requestCode == WHATSAPP_BUSINESS_REQUEST_CODE){

                SharedPrefUtils.putPrefString(SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, true)
                getWhatsAppBusinessStatuses()
            }
        }
    }

}


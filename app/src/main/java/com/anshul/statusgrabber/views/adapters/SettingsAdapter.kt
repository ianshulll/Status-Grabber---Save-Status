package com.anshul.statusgrabber.views.adapters

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshul.statusgrabber.R
import com.anshul.statusgrabber.databinding.DialogGuideBinding
import com.anshul.statusgrabber.databinding.ItemSettingsBinding
import com.anshul.statusgrabber.models.SettingsModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsAdapter(var list: ArrayList<SettingsModel>, var context: Context) :
    RecyclerView.Adapter<SettingsAdapter.viewHolder>() {

    inner class viewHolder(var binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val bullet: Char = '\u2022'
        fun bind(model: SettingsModel, position: Int) {
            binding.apply {
                settingsTitle.text = model.title
                settingsDesc.text = model.desc

                root.setOnClickListener {
                    when (position) {
                        0 -> {
                            // how to use 1st item
                            val dialog = Dialog(context)
                            val dialogBinding =
                                DialogGuideBinding.inflate((context as Activity).layoutInflater)
                            dialogBinding.okayBtn.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.setContentView(dialogBinding.root)

                            dialog.window?.setLayout(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.WRAP_CONTENT
                            )

                            dialog.show()


                        }

                        2 -> {
                            MaterialAlertDialogBuilder(context).apply {
                                setTitle("Disclaimer")
                                setMessage("Disclaimer: WhatsApp Status Downloader App\n" +
                                        "\n" +
                                        "By using this app, you agree to:\n" +
                                        "\n" +
                                        bullet+"Respect copyrights and intellectual property.\n" +
                                        bullet+"Understand this app is not affiliated with WhatsApp.\n" +
                                        bullet+"Take responsibility for content you download.\n" +
                                        bullet+"Ensure your privacy and device security.\n" +
                                        bullet+"Keep the app updated for optimal performance.\n" +
                                        bullet+"Comply with local laws regarding content sharing and downloads.\n" )
                                setPositiveButton("Okay",null)
                                show()
                            }
                        }

                        3 -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://mohdanshul.netlify.app")).apply {
                                context.startActivity(this)
                            }

                        }

                        4 -> {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.app_name))
                                putExtra(Intent.EXTRA_TEXT,"Want To Download Status For Whatsapp ? : https://play.google.com/store/apps/details?id=${context.packageName}")
                                context.startActivity(this)
                            }
                        }

                        5 -> {
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                            ).apply {
                                context.startActivity(this)
                            }

                        }
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemSettingsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(model = list[position], position)
    }
}

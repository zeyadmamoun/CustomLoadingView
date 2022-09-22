package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var radioGroup: RadioGroup
    private lateinit var downloadButton: LoadingButton
    private var selectedFileName: String? = null
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(this,getString(R.string.notification_title),getString(R.string.notification_description))

        radioGroup = findViewById(R.id.radio_group)
        downloadButton = findViewById(R.id.custom_button)

        downloadButton.setOnClickListener {
            when(radioGroup.checkedRadioButtonId){
                R.id.glide_radio_button ->{
                    selectedFileName = "GlideLibrary.zip"
                    downloadButton.buttonState = ButtonState.Clicked
                    download(getString(R.string.glide_url))
                }
                R.id.project_radio_button ->{
                    selectedFileName = "StarterProject.zip"
                    downloadButton.buttonState = ButtonState.Clicked
                    download(getString(R.string.project_starter_code))
                }
                R.id.retrofit_radio_button ->{
                    selectedFileName = "RetrofitLibrary.zip"
                    downloadButton.buttonState = ButtonState.Clicked
                    download(getString(R.string.retrofit_url))
                }
                View.NO_ID ->{
                    Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID){
                val downloadQuery = DownloadManager.Query()
                val itemFile = downloadQuery.setFilterById(id)
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val itemFileQuery = downloadManager.query(itemFile)

                itemFileQuery.moveToFirst()
                val status = when (itemFileQuery.getInt(itemFileQuery.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_SUCCESSFUL -> "Success"
                    else -> "Failed"
                }

                if (context != null) {
                    createNotification(context,"Download Notification","your download is finished",fileName,status)
                }
            }
        }
    }

    private fun download(url: String) {
        fileName = url
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,selectedFileName)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request) // enqueue puts the download request in the queue.
    }

}

package com.example.androidthreadtest

import android.os.AsyncTask
import android.widget.Toast
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class DownloadTask : AsyncTask<Unit, Int, Boolean>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun doInBackground(vararg params: Unit?) = try {
        while (true) {
            val downloadPercent = doDownload()
            publishProgress(downloadPercent)
            if(downloadPercent >= 100) {
                break
            }
        }
        true
    } catch (e: Exception) {
        false
    }

    override fun onProgressUpdate(vararg values: Int?) {
        progressDialog.setMessage("Downloaded ${Pvalues[0]}%")
    }

    override fun onPostExecute(result: Boolean?) {
        progressDialog.dismiss()
        if(result) {
            Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
        }

    }
}
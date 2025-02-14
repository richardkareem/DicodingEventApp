package com.org.dicodingeventapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.org.dicodingeventapp.R
import com.org.dicodingeventapp.data.remote.response.EventResponse
import com.org.dicodingeventapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWorker(context:Context, workerParams:WorkerParameters): Worker(context,workerParams) {
    companion object{
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getUpcomingEvent()
    }

    private fun getUpcomingEvent(): Result{
        Log.d(TAG, "get upcoming worker dimulai...")
        val client = ApiConfig.getApiService().getEventByWorkerManager()
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if(response.isSuccessful){
                    val listEvent = response.body()?.listEvents
                    if (listEvent != null) {
                        if(listEvent.isEmpty()){
                            Log.d(TAG, "onSuccess: data kosong!!")
                            resultStatus = Result.success()

                        }else{
                            val firstUpcoming =  response.body()?.listEvents?.first()
                            if(firstUpcoming != null){
                                val title = firstUpcoming.name
                                val message = firstUpcoming.beginTime
                                showNotification(title, message)
                                Log.d(TAG, "onSuccess: selesai")
                                resultStatus = Result.success()
                            }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showNotification("Get upcoming event gagal", t.message)
                Log.d(TAG, "onFailure: gagal")
                resultStatus = Result.failure()
            }

        })
        return  resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifiaction_active_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}
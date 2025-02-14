package com.org.dicodingeventapp.ui.detailEvent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.org.dicodingeventapp.R
import com.org.dicodingeventapp.data.local.entity.EventEntity
import com.org.dicodingeventapp.databinding.ActivityDetailEventBinding
import com.org.dicodingeventapp.data.remote.response.Event
import com.org.dicodingeventapp.data.repository.Result
import com.org.dicodingeventapp.utils.FormatTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Event"
        val factory = DetaiEventVIewModelFactory.getInstance(this)
         val detailEventViewModel : DetailEventViewModel by viewModels {
            factory
        }

        val id = DetailEventActivityArgs.fromBundle(intent.extras as Bundle).queryId
        var isFavorite = DetailEventActivityArgs.fromBundle(intent.extras as Bundle).isFavorite
        Log.d("DETAIL_EVENT", "id: $id, isFavorite: $isFavorite")

        detailEventViewModel.getDetailEvent(id).observe(this){result ->
            if(result!= null){
                when(result){
                    is Result.Error -> {
                        onError(true)
                        onLoading(false)
                    }
                    is Result.Success -> {
                        setDetailEventData(result.data)
                        onError(false)
                        onLoading(false)
                        detailEventViewModel.getFavorite(id).observe(this){ isFav ->
                            Log.d("DetailEvent", isFav.toString())
                            if(isFav){
                                binding.fbFavorite.setImageResource(R.drawable.ic_favorite)
                                binding.fbFavorite.setOnClickListener {
                                    detailEventViewModel.deleteFavoriteEvent(id)
                                }
                            }else{
                                binding.fbFavorite.setImageResource(R.drawable.ic_favorite_outline)
                                binding.fbFavorite.setOnClickListener{
                                   detailEventViewModel.insertFavorite(result.data)
                                }
                            }
                        }


                    }
                    Result.isEmpty -> {
                        onLoading(false)
                    }
                    Result.loading -> {
                        onLoading(true)
                    }
                }
            }
        }







    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkAlreadyExpired(dateString: String, dateFormat: String = "yyyy-MM-dd") : Boolean {
        return try {
            val sdf = SimpleDateFormat(dateFormat,Locale("ID","id"))
            sdf.isLenient = false
            val inputDate: Date = sdf.parse(dateString)!!
            val currentDate = Date()
            inputDate.before(currentDate)
        } catch (e: Exception) {
            false
        }
    }

    private fun onLoading(isLoading: Boolean){
        if(isLoading) binding.loadingDetail.visibility = View.VISIBLE else binding.loadingDetail.visibility = View.GONE
    }

    private fun onIsFavoriteOrNot(isFavorite: Int){
        if(isFavorite == 1){
            binding.fbFavorite.setImageResource(R.drawable.ic_favorite)
        }else{
            binding.fbFavorite.setImageResource(R.drawable.ic_favorite_outline)
        }
    }

    private fun onError(isErr: Boolean){
        if(isErr) {
            binding.detail503.card503.visibility = View.VISIBLE
            binding.ctDetail.visibility = View.GONE
        }else {
            binding.detail503.card503.visibility = View.GONE
            binding.ctDetail.visibility = View.VISIBLE
        }
    }



    private  fun setDetailEventData(data: Event){
        val isEventExpired = checkAlreadyExpired(data.endTime)
        val schedule = FormatTime.formatTimeRange(data.beginTime, data.endTime)
        val resultQuota = data.quota - data.registrants
        val textQuota = "Sisa Kuota: ${if(resultQuota <= 0) "- (Penuh)" else "$resultQuota"}"
        val textOrganizer = "Penyelenggara: ${data.ownerName}"
        binding.tvCategory.text = data.category
        binding.tvOrganizer.text = textOrganizer
        binding.tvTitle.text = data.name
        binding.tvStatus.text = if (isEventExpired) "Event Telah Selesai" else "Event Belum Selesai"
        binding.tvDescriptionSection.text = (HtmlCompat.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_LEGACY))
        binding.location.text = data.cityName
        binding.tvQuota.text = textQuota
        binding.tvSchedule.text = schedule
        binding.btnToBrowser.text = if (isEventExpired) "Go to official website" else "Register Now"
        Glide.with(this)
            .load(data.mediaCover)
            .into(binding.ivDescription)
        binding.btnToBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(data.link))
            startActivity(intent)

        }
    }
}
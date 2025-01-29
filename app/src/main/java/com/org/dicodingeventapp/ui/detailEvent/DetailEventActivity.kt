package com.org.dicodingeventapp.ui.detailEvent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.org.dicodingeventapp.databinding.ActivityDetailEventBinding
import com.org.dicodingeventapp.service.data.response.Event
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEventBinding
    private val detailEventViewModel : DetailEventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Event"

        val id = DetailEventActivityArgs.fromBundle(intent.extras as Bundle).queryId

        detailEventViewModel.getDetailEvent(id)



        detailEventViewModel.detailEvent.observe(this){ detailEvent ->
            setDetailEventData(detailEvent)
        }

        detailEventViewModel.isLoading.observe(this){isLoading ->
            binding.loadingDetail.visibility = if(isLoading) View.VISIBLE else View.GONE
        }

        detailEventViewModel.isError.observe(this){isErr ->
            if(isErr) {
                binding.detail503.card503.visibility = View.VISIBLE
                binding.ctDetail.visibility = View.GONE
            }else {
                binding.detail503.card503.visibility = View.GONE
                binding.ctDetail.visibility = View.VISIBLE
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

    private fun formatTimeRange(beginTime: String, endTime: String): String {
        val local = Locale("ID", "id")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", local)
        val outputDateFormat = SimpleDateFormat("d MMMM yyyy", local)
        val outputTimeFormat = SimpleDateFormat("h a", local)

        val startDate = inputFormat.parse(beginTime)!!
        val endDate = inputFormat.parse(endTime)!!

        return "${outputDateFormat.format(startDate)} jam ${outputTimeFormat.format(startDate)} - ${outputDateFormat.format(endDate)} jam ${outputTimeFormat.format(endDate)}"
    }

    private  fun setDetailEventData(data: Event){
        val isEventExpired = checkAlreadyExpired(data.endTime)
        val schedule = formatTimeRange(data.beginTime, data.endTime)
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
package com.org.dicodingeventapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.databinding.ActivitySearchBinding
import com.org.dicodingeventapp.ui.EventAdapter
import com.org.dicodingeventapp.ui.detailEvent.DetailEventActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Search"

        val query = SearchActivityArgs.fromBundle(intent.extras as Bundle).query
        val teksSearchQ  = "You Search: $query"
        binding.tvSearchQuery.text = teksSearchQ

        val layoutManager = LinearLayoutManager(this)
        binding.rvEventSearch.layoutManager = layoutManager

        val searchFactory = SearchViewModelFactory.getInstance(this)
        val searchViewModel : SearchViewModel by viewModels{
            searchFactory
        }

        if(query != ""){
            searchViewModel.fetchFindEventyQuery(query!!)
        }
        searchViewModel.listEventsItem.observe(this){ listEvents ->
            setEventData(listEvents)
            val teks = "Event Found: ${listEvents.size}"
            binding.tvSearchFound.text = teks
        }

        searchViewModel.isLoading.observe(this){isLoading ->
            setLoading(isLoading)
        }

        searchViewModel.isErr.observe(this){isErr ->
            if(isErr){
                binding.search503.card503.visibility = View.VISIBLE
                binding.rvEventSearch.visibility = View.GONE
            }else{
                binding.search503.card503.visibility = View.GONE
                binding.rvEventSearch.visibility = View.VISIBLE
            }
        }

    }


    private fun setLoading(isLoading: Boolean){
        if(isLoading)binding.loadingSearchActivity.visibility = View.VISIBLE
        else binding.loadingSearchActivity.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            else ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setEventData(data:List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(data)
        binding.rvEventSearch.adapter = adapter
        adapter.setItemOnClickCallback(object : EventAdapter.OnClickCallback{
            override fun onItemClicked(data: ListEventsItem) {
                val intent = Intent(this@SearchActivity, DetailEventActivity::class.java)
                intent.putExtras(Bundle().apply {
                    putString("queryId", data.id.toString())
                })
                startActivity(intent)
            }

        })
    }
}
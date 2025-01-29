package com.org.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.org.dicodingeventapp.databinding.FragmentHomeBinding
import com.org.dicodingeventapp.service.data.response.ListEventsItem

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel : HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fetch api get upcoming and finished
        homeViewModel.initUpcomingAndFinishedFetch()

        val layoutManagerHorizontal = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalHome.layoutManager = layoutManagerHorizontal

        val layoutManagerVertical = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvVerticalHome.layoutManager = layoutManagerVertical

        homeViewModel.upcomingEvent.observe(viewLifecycleOwner){ upcomingEvent ->
            setEventData(upcomingEvent)
        }
        homeViewModel.finishedEvent.observe(viewLifecycleOwner){ finishedEvent ->
            setEventVerticalData(finishedEvent)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            showLoading(isLoading)
        }

        // if data empty
        homeViewModel.isEmptyUpcoming.observe(viewLifecycleOwner){ isEmpty ->
            if(isEmpty) {
                binding.tvDataEmptyHome.visibility = View.VISIBLE
                binding.rvHorizontalHome.minimumHeight = 200
            } else {
                binding.tvDataEmptyHome.visibility = View.GONE
            }
        }
        homeViewModel.isEmptyFinished.observe(viewLifecycleOwner){ isEmpty->
            if(isEmpty) binding.tvDataEmptyHome.visibility = View.VISIBLE else binding.tvDataEmptyHome.visibility = View.GONE
        }
        binding.btnSearch.setOnClickListener {
            val searchQuery = binding.searchView.text.toString()
            if(searchQuery != ""){
                val toSearchFragment = HomeFragmentDirections.actionNavigationHomeToSearchActivity(searchQuery)
                view.findNavController().navigate(toSearchFragment)
            }
        }

    }

    private fun setEventData(data: List<ListEventsItem>){
        val homeAdapter = HomeAdapter(data)
        binding.rvHorizontalHome.adapter = homeAdapter
        homeAdapter.onItemClickCallback = object : HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(id: String) {
                val toDetailEvent = HomeFragmentDirections.actionNavigationHomeToDetailEventActivity()
                toDetailEvent.setQueryId(id)
                view?.findNavController()?.navigate(toDetailEvent)
            }

        }


    }
    private fun setEventVerticalData(data:List<ListEventsItem>){
        val homeAdapterFinished = HomeAdapter(data)
        binding.rvVerticalHome.adapter = homeAdapterFinished
        homeAdapterFinished.onItemClickCallback = object : HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(id: String) {
                val toDetailEvent = HomeFragmentDirections.actionNavigationHomeToDetailEventActivity()
                toDetailEvent.setQueryId(id)
                view?.findNavController()?.navigate(toDetailEvent)
            }

        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading) binding.loadingHome.visibility = View.VISIBLE else binding.loadingHome.visibility = View.GONE
    }
}
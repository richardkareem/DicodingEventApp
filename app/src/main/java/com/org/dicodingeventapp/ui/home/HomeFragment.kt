package com.org.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.data.repository.Result
import com.org.dicodingeventapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private  var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = HomeViewModelFactory.getInstance(requireContext())

        //rv upcoming
        val layoutManagerUpcoming = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.rvUpcomingHome?.layoutManager = layoutManagerUpcoming
        //rv finished
        val layoutManagerFinished = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding?.rvFinishedHome?.layoutManager = layoutManagerFinished
        val homeViewModel : HomeViewModel by viewModels{
            factory
        }

            /** UPCOMING */
            homeViewModel.getUpcomingEvent().observe(viewLifecycleOwner){res ->
                if(res != null){
                    when(res){
                        is Result.Error -> {
                            showLoading(false)
                            setEmptyDataUpcoming(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setEmptyDataUpcoming(false)
                            setEventUpcoming(res.data)
                        }
                        Result.isEmpty -> {
                            showLoading(false)
                            setEmptyDataUpcoming(true)
                        }
                        Result.loading -> {
                            showLoading(true)
                        }
                    }
                }
            }


            /** FINISHED */ //keknya penyebabnya tu result deh
            homeViewModel.getFinishedEvent().observe(viewLifecycleOwner){res ->
                if(res != null){
                    when(res){
                        is Result.Error -> {
                            showLoading(false)
                            setEmptyDataFinished(false)
                        }
                        is Result.Success -> {
                            setEventFinished(res.data)
                            showLoading(false)
                            setEmptyDataFinished(false)
                        }
                        Result.isEmpty -> {
                            setEmptyDataFinished(true)
                            showLoading(false)
                        }
                        Result.loading -> {

                            showLoading(true)
                        }
                    }
                }

            }

        binding?.btnSearch?.setOnClickListener {
            val searchQuery = binding?.searchView?.text.toString()
            if(searchQuery != ""){
                val toSearchFragment = HomeFragmentDirections.actionNavigationHomeToSearchActivity(searchQuery)
                view.findNavController().navigate(toSearchFragment)
            }
        }

    }

    private fun setEventFinished(data: List<ListEventsItem>){
        val homeAdapter = HomeAdapterFinished(data)
        binding?.rvFinishedHome?.adapter = homeAdapter
        homeAdapter.onItemClickCallback = object : HomeAdapterFinished.OnItemClickCallback{
            override fun onItemClicked(id: String) {
                val toDetailEvent = HomeFragmentDirections.actionNavigationHomeToDetailEventActivity()
                toDetailEvent.setQueryId(id)
                view?.findNavController()?.navigate(toDetailEvent)
            }


        }


    }
    private fun setEventUpcoming(data:List<ListEventsItem>){
        val homeAdapter = HomeAdapter(data)
        binding?.rvUpcomingHome?.adapter = homeAdapter
        homeAdapter.onItemClickCallback = object : HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(id: String) {
                val toDetailEvent = HomeFragmentDirections.actionNavigationHomeToDetailEventActivity()
                toDetailEvent.setQueryId(id)
                view?.findNavController()?.navigate(toDetailEvent)
            }
        }
    }

    private fun setEmptyDataUpcoming (isEmpty: Boolean){
        if(isEmpty) {
            binding?.tvDataEmptyHome?.visibility = View.VISIBLE
            binding?.rvFinishedHome?.minimumHeight = 200
        } else {
            binding?.tvDataEmptyHome?.visibility = View.GONE
        }
    }

    private fun setEmptyDataFinished(isEmpty: Boolean){
     if(isEmpty){
         binding?.tvDataEmptyHome?.visibility = View.VISIBLE
         binding?.rvFinishedHome?.minimumHeight = 200
     }else{
         binding?.tvDataEmptyHome?.visibility = View.GONE
     }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading) binding?.loadingHome?.visibility = View.VISIBLE else binding?.loadingHome?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
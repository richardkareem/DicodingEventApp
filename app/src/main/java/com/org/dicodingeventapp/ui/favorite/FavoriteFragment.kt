package com.org.dicodingeventapp.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.dicodingeventapp.data.local.entity.EventEntity
import com.org.dicodingeventapp.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    
    private var _binding : FragmentFavoriteBinding? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = FavoritesViewModelFactory.getInstance(requireContext())
        val favoriteViewModel : FavoriteViewModel by viewModels{
            factory
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvFavorite?.layoutManager = layoutManager

        favoriteViewModel.getAllFavorites().observe(viewLifecycleOwner){event ->
            if(event != null){
                Log.d("FavoriteFragment", event.toString())
                setEventData(event)
                if(event.isEmpty()){
                    binding?.ivNoData?.visibility = View.VISIBLE
                }
            }
        }

    }
// private fun setEventUpcoming(data:List<EventEntity>){
//        val homeAdapter = HomeAdapter(data)
//        binding?.rvUpcomingHome?.adapter = homeAdapter
//        homeAdapter.onItemClickCallback = object : HomeAdapter.OnItemClickCallback{
//            override fun onItemClicked(id: String, currentIsFavorite: Int) {
//                val toDetailEvent = HomeFragmentDirections.actionNavigationHomeToDetailEventActivity()
//                toDetailEvent.setQueryId(id)
//                toDetailEvent.setIsFavorite(currentIsFavorite)
//                view?.findNavController()?.navigate(toDetailEvent)
//            }
//
//        }
//    }

    private fun setEventData(listEvent : List<EventEntity>){
        val adapter = FavoriteAdapter(listEvent)
        binding?.rvFavorite?.adapter = adapter
        //onclick card
        adapter.onItemClickCallback = object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(id: String) {
                val toDetailEvent = FavoriteFragmentDirections.actionFavoriteFragmentToDetailEventActivity()
                toDetailEvent.queryId = id.toString()
                view?.findNavController()?.navigate(toDetailEvent)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
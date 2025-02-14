package com.org.dicodingeventapp.ui.upcoming
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.dicodingeventapp.data.remote.response.ListEventsItem
import com.org.dicodingeventapp.data.repository.Result
import com.org.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.org.dicodingeventapp.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding ? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = UpcomingViewModelFactory.getInstance(requireContext())
        val upcomingViewModel : UpcomingViewModel by viewModels{
            factory
        }

        //set recycle view
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.layoutManager = layoutManager

        upcomingViewModel.getUpcomingEvent().observe(viewLifecycleOwner){res ->
            if(res != null){
                when(res){
                    is Result.Error -> {
                        onLoading(false)
                        onError(true)
                    }
                    is Result.Success -> {
                        onLoading(false)
//                        setEventData(res.data)
                    }
                    Result.isEmpty -> {
                        onLoading(false)
                        onDataEmpty(true)
                    }
                    Result.loading -> {
                        onLoading(true)
                    }
                }
            }

        }
    }

    //function set data recycle view
    private fun setEventData(listEvent : List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(listEvent)
        binding.rvUpcoming.adapter = adapter
        //onclick card
        adapter.setItemOnClickCallback(object : EventAdapter.OnClickCallback{
            override fun onItemClicked(data: ListEventsItem) {
                val toDetailEvent = UpcomingFragmentDirections.actionNavigationUpcomingToDetailEventActivity()
                toDetailEvent.queryId = data.id.toString()
                view?.findNavController()?.navigate(toDetailEvent)
            }


        })
    }

    private fun onError(isError: Boolean){
        if(isError){
                binding.upcoming503.card503.visibility = View.VISIBLE
            }else{
                binding.upcoming503.card503.visibility = View.GONE
            }
    }

    private fun onDataEmpty(isEmpty: Boolean){
        if(isEmpty)binding.constraintFinishedDataEmpty.visibility = View.VISIBLE
            else binding.constraintFinishedDataEmpty.visibility = View.GONE
    }

    private fun onLoading(isLoading: Boolean){
        binding.loadingUpcoming.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
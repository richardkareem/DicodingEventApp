package com.org.dicodingeventapp.ui.upcoming
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding ? = null
    private val binding get()= _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val upcomingViewModel : UpcomingViewModel by viewModels()

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
        //set recycle view
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.layoutManager = layoutManager
        //call view model listEvent
        upcomingViewModel.upcomingEvent.observe(viewLifecycleOwner){ listEventData ->
            setEventData(listEventData)

        }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            binding.loadingUpcoming.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
        }

        upcomingViewModel.isError.observe(viewLifecycleOwner){isError ->
            if(isError){
                binding.upcoming503.card503.visibility = View.VISIBLE
            }else{
                binding.upcoming503.card503.visibility = View.GONE
            }


        }

        upcomingViewModel.isDataEmpty.observe(viewLifecycleOwner){isDataEmpty ->
            if(isDataEmpty)binding.constraintFinishedDataEmpty.visibility = View.VISIBLE
            else binding.constraintFinishedDataEmpty.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
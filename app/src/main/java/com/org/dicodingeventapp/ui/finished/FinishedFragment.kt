package com.org.dicodingeventapp.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.dicodingeventapp.databinding.FragmentFinishedBinding
import com.org.dicodingeventapp.service.data.response.ListEventsItem
import com.org.dicodingeventapp.ui.EventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel : FinishedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFinished.layoutManager= layoutManager

        finishedViewModel.listItem.observe(viewLifecycleOwner){ listEvent ->
            setEventData(listEvent)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            setLoading(isLoading)
        }
        finishedViewModel.isEmpty.observe(viewLifecycleOwner){isEmpty ->
            if(isEmpty)binding.constraintUpcomingDataEmpty.visibility = View.VISIBLE
            else binding.constraintUpcomingDataEmpty.visibility = View.GONE
        }
        finishedViewModel.isError.observe(viewLifecycleOwner){isError ->
            if(isError){
                binding.card503Finished.card503.visibility = View.VISIBLE
            }else
                binding.card503Finished.card503.visibility = View.GONE
        }

    }

    private fun setEventData(datas: List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(datas)
        binding.recyclerViewFinished.adapter = adapter
        adapter.setItemOnClickCallback(object : EventAdapter.OnClickCallback{
            override fun onItemClicked(data: ListEventsItem) {
               val toDetailEvent = FinishedFragmentDirections.actionNavigationFinishedToDetailEventActivity()
                toDetailEvent.queryId = data.id.toString()
                view?.findNavController()?.navigate(toDetailEvent)
            }

        })

    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading)binding.pbFragment.visibility = View.VISIBLE
        else binding.pbFragment.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
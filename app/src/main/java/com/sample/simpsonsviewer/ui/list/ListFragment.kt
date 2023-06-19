package com.sample.simpsonsviewer.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.main.MainViewModel
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            this.recyclerView = view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { value ->
                    onStateReceived(state = value)
                }
            }
        }
    }

    private fun onStateReceived(state: MainViewModelStore.State) {
        when(state) {
            is MainViewModelStore.State.Error -> { }
            MainViewModelStore.State.Idle -> { }
            MainViewModelStore.State.InProgress -> { }
            is MainViewModelStore.State.OriginalSearchQuery -> { }
            is MainViewModelStore.State.Response -> { onResponse(state) }
        }
    }

    private fun onResponse(state: MainViewModelStore.State.Response) {
        recyclerView.adapter = SummaryRecyclerViewAdapter(state.data.list)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
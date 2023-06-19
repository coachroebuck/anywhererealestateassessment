package com.sample.simpsonsviewer.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.main.MainViewModel
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import com.sample.simpsonsviewer.model.ServiceResponseSummary
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ServiceResponseSummaryKey = "ServiceResponseSummary"


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private var serviceResponseSummary: ServiceResponseSummary? = null
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var icon: ImageView
    private lateinit var rootView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceResponseSummary =
                it.getSerializable(ServiceResponseSummaryKey) as ServiceResponseSummary?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
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
        rootView = view.findViewById(R.id.rootView)
        title = view.findViewById(R.id.title)
        details = view.findViewById(R.id.details)
        icon = view.findViewById(R.id.imageView)

        loadDetails()
    }

    private fun loadDetails() {
        serviceResponseSummary?.let {
            rootView.isVisible = true
            title.text = serviceResponseSummary?.title
            details.text = serviceResponseSummary?.details
            Glide.with(icon)
                .load(serviceResponseSummary?.icon)
                .error(R.drawable.photo_not_available)
                .into(icon)
        } ?: run {
            rootView.isVisible = false
        }
    }

    private fun onStateReceived(state: MainViewModelStore.State) {
        when (state) {
            is MainViewModelStore.State.Error -> {}
            MainViewModelStore.State.Idle -> {}
            MainViewModelStore.State.InProgress -> {}
            is MainViewModelStore.State.OriginalSearchQuery -> {}
            is MainViewModelStore.State.Response -> {}
            is MainViewModelStore.State.ItemSelected -> {
                onItemSelected(state)
            }
        }
    }

    private fun onItemSelected(state: MainViewModelStore.State.ItemSelected) {
        serviceResponseSummary = state.selectedItem
        requireActivity().runOnUiThread { loadDetails() }
    }
}
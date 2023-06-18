package com.sample.simpsonsviewer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.sample.simpsonsviewer.databinding.ActivityMainBinding
import com.sample.simpsonsviewer.main.MainViewModel
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import com.sample.simpsonsviewer.ui.details.DetailsFragment
import com.sample.simpsonsviewer.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)

        viewModel.onCreate(savedInstanceState)

        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (isTablet) {
            setContentView(R.layout.activity_main_tablet)

            // Tablet layout: Add both fragments to the container
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.list_container, ListFragment())
                .add(R.id.detail_container, DetailsFragment())
                .commit()
        } else {
            setContentView(R.layout.activity_main)

            // Phone layout: Add only the list fragment to the container
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.container, ListFragment())
                .commit()
        }

        binding.appBarMain.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    submitQueryText(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    queryTextChange(newText)
                    return true
                }
            })
        }

        lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                onStateReceived(state = value)
            }
        }
    }

    private fun queryTextChange(text: String?) {
        viewModel.emit(MainViewModelStore.Intent.OnSearchQueryChanged(text))
    }

    private fun onStateReceived(state: MainViewModelStore.State) {
        when (state) {
            MainViewModelStore.State.Idle -> {}
            is MainViewModelStore.State.OriginalSearchQuery -> onOriginalSearchQuery(state)
            is MainViewModelStore.State.Error -> onError(state)
            MainViewModelStore.State.InProgress -> onInProgress(state)
            is MainViewModelStore.State.Response -> onResponse(state)
        }
    }

    private fun onError(state: MainViewModelStore.State.Error) {
        runOnUiThread { Toast.makeText(this, state.throwable.message, Toast.LENGTH_LONG).show() }
    }

    private fun onInProgress(state: MainViewModelStore.State) {
        println(state)
    }

    private fun onResponse(state: MainViewModelStore.State.Response) {
        println(state)
    }

    private fun onOriginalSearchQuery(state: MainViewModelStore.State.OriginalSearchQuery) {
        binding.appBarMain.searchView.setQuery(state.query, false)
    }

    private fun submitQueryText(query: String?) {
        viewModel.emit(MainViewModelStore.Intent.SubmitQueryText(query))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }
}
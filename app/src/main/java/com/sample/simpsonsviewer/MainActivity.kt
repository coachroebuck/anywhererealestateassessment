package com.sample.simpsonsviewer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    private var isTablet: Boolean = true

    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel.onCreate(savedInstanceState)

        isTablet = resources.getBoolean(R.bool.isTablet)

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
        setSupportActionBar(binding.appBarMain.toolbar)

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
            retrieveInformation()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { value ->
                    onStateReceived(state = value)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    private fun retrieveInformation() {
        viewModel.emit(MainViewModelStore.Intent.RetrieveInformation)
    }

    private fun queryTextChange(text: String?) {
        emit(MainViewModelStore.Intent.OnSearchQueryChanged(text))
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
        enableUserInteraction()
    }

    private fun onInProgress(state: MainViewModelStore.State) {
        disableUserInteraction()
    }

    private fun onResponse(state: MainViewModelStore.State.Response) {
        enableUserInteraction()
    }

    private fun disableUserInteraction() {
        runOnUiThread {
            binding.appBarMain.searchView.isEnabled = false
            binding.progressIndicator.isVisible = true
            findViewById<View>(R.id.progressIndicator).isVisible = true

            if (isTablet) {
                findViewById<View>(R.id.list_container).isVisible = false
                findViewById<View>(R.id.detail_container).isVisible = false
            } else {
                findViewById<View>(R.id.container).isVisible = false
            }
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    private fun enableUserInteraction() {
        runOnUiThread {
            binding.appBarMain.searchView.isEnabled = true
            binding.progressIndicator.isVisible = false
            findViewById<View>(R.id.progressIndicator).isVisible = false

            if (isTablet) {
                findViewById<View>(R.id.list_container).isVisible = true
                findViewById<View>(R.id.detail_container).isVisible = true
            } else {
                findViewById<View>(R.id.container).isVisible = true
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun onOriginalSearchQuery(state: MainViewModelStore.State.OriginalSearchQuery) {
        binding.appBarMain.searchView.setQuery(state.query, false)
    }

    private fun submitQueryText(query: String?) {
        emit(MainViewModelStore.Intent.SubmitQueryText(query))
    }

    private fun emit(intent: MainViewModelStore.Intent) {
        viewModel.emit(intent)
    }
}
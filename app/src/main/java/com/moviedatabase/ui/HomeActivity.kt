package com.moviedatabase.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moviedatabase.data.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
private class HomeActivity : AppCompatActivity() {

    private val vm: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    private fun observeViewModels() {


        lifecycleScope.launch {
            // repeatOnLifecycle is a suspend function that waits until the state is reached
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.trending.collect {
                    // Update RecyclerView safely here
                }
            }
        }
    }
}
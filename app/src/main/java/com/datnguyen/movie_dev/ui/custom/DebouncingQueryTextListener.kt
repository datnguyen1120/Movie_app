package com.datnguyen.movie_dev.ui.custom

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


internal class DebouncingQueryTextListener(
    lifecycle: Lifecycle,
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : TextWatcher {
    var debouncePeriod: Long = 500

    private val coroutineScope: CoroutineScope = lifecycle.coroutineScope

    private var searchJob: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            s?.toString()?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(it)
            }
        }
    }
}
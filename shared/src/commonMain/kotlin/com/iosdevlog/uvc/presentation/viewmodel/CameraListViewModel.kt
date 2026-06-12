package com.iosdevlog.uvc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iosdevlog.uvc.domain.model.UVCDevice
import com.iosdevlog.uvc.domain.repository.UVCRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CameraListViewModel(
    private val repository: UVCRepository
) : ViewModel() {

    private val _devices = MutableStateFlow<List<UVCDevice>>(emptyList())
    val devices: StateFlow<List<UVCDevice>> = _devices.asStateFlow()

    init {
        observeDevices()
    }

    private fun observeDevices() {
        viewModelScope.launch {
            repository.getDevices().collect { deviceList ->
                _devices.value = deviceList
            }
        }
    }

    fun refresh() {
        observeDevices()
    }
}

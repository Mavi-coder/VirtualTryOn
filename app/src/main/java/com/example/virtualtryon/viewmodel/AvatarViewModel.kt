package com.example.virtualtryon.viewmodel

import androidx.lifecycle.ViewModel
import com.example.virtualtryon.data.model.Avatar
import com.example.virtualtryon.data.repository.AvatarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AvatarViewModel : ViewModel() {

    private val repository = AvatarRepository()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentAvatar = MutableStateFlow<Avatar?>(null)
    val currentAvatar: StateFlow<Avatar?> = _currentAvatar

    fun saveAvatar(
        name: String,
        gender: String,
        height: String,
        weight: String,
        size: String
    ) {
        _isLoading.value = true

        val avatar = Avatar(
            name = name,
            gender = gender,
            height = height,
            weight = weight,
            size = size
        )

        repository.saveAvatar(
            avatar = avatar,
            onSuccess = {
                _isLoading.value = false
                _isSaved.value = true
                _currentAvatar.value = avatar
            },
            onFailure = {
                _isLoading.value = false
                _isSaved.value = false
            }
        )
    }
}
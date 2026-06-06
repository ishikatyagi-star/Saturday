package com.studysnap.ui.addnotes

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNotesViewModel @Inject constructor() : ViewModel() {
    var cameraUri: Uri? = null
    var imageUri: Uri? = null
        private set
    var textInput: String? = null
        private set

    fun setImageUri(uri: Uri) {
        imageUri = uri
        textInput = null
    }

    fun setTextInput(text: String) {
        textInput = text
        imageUri = null
    }
}
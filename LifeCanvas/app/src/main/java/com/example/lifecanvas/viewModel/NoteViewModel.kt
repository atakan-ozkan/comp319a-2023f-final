package com.example.lifecanvas.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifecanvas.model.NoteModel
import com.example.lifecanvas.repository.NoteRepository
import kotlinx.coroutines.launch
import java.util.Date

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    fun insert(note: NoteModel) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun get(noteId: Int): LiveData<NoteModel> {
        val liveData = MutableLiveData<NoteModel>()
        viewModelScope.launch {
            liveData.value = repository.get(noteId)
        }
        return liveData
    }

    fun update(note: NoteModel) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun delete(note: NoteModel) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAllNotes()
        }
    }

    fun deletePublicNotes() {
        viewModelScope.launch {
            repository.deletePublicNotes()
        }
    }

    fun deletePrivateNotes() {
        viewModelScope.launch {
            repository.deletePrivateNotes()
        }
    }

    fun getAllNotes(): LiveData<List<NoteModel>> {
        val liveData = MutableLiveData<List<NoteModel>>()
        viewModelScope.launch {
            liveData.value = repository.getAllNotes()
        }
        return liveData
    }

    fun getPublicNotes(): LiveData<List<NoteModel>> {
        val liveData = MutableLiveData<List<NoteModel>>()
        viewModelScope.launch {
            liveData.value = repository.getPublicNotes()
        }
        return liveData
    }

    fun getPrivateNotes(): LiveData<List<NoteModel>> {
        val liveData = MutableLiveData<List<NoteModel>>()
        viewModelScope.launch {
            liveData.value = repository.getPrivateNotes()
        }
        return liveData
    }

    fun searchNotesWithFilters(
        searchText: String,
        isPublic: Boolean? = null,
        noteType: String? = null,
        createdDateStart: Date? = null,
        createdDateEnd: Date? = null,
        modifiedDateStart: Date? = null,
        modifiedDateEnd: Date? = null
    ) : LiveData<List<NoteModel>>{
        val liveData = MutableLiveData<List<NoteModel>>()
        val searchQuery = if (searchText.isBlank()) "%" else "%$searchText%"
        viewModelScope.launch {
            liveData.value = repository.searchNotes(searchQuery, isPublic, noteType,
                createdDateStart,createdDateEnd,modifiedDateStart, modifiedDateEnd)
        }
        return liveData
    }

}
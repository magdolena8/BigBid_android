package com.begdev.bigbid.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.network.data.ItemRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val itemRepo: ItemRepo

) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = MutableStateFlow(HomeState())

//    init {
//        fetchBooks()
//    }

//    private val _state = MutableStateFlow(emptyList<Item>())
//    val state: StateFlow<List<Item>>
//        get() = _state

    init {
        viewModelScope.launch {
            val items = itemRepo.getItems()
//            _uiState.value.items = items
            _uiState.value.copy(
                items = items
            )
        }
    }


    //    fun handleEvent(homeEvent: HomeEvent)

}
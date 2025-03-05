package com.example.thmanyah.base.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thmanyah.base.presentation.uimodel.BaseEvent
import com.example.thmanyah.base.presentation.uimodel.BaseIntent
import com.example.thmanyah.base.presentation.uimodel.BaseState
import com.example.thmanyah.base.presentation.uimodel.UiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



/**
 * BaseViewModel that implements the MVI architecture pattern
 * with automatic mapping from domain entities to UI state and UI models
 *
 * @param I The type of Intent that this ViewModel will process
 * @param S The type of State that this ViewModel will maintain
 * @param M The type of UI Model that will be derived from the State
 * @param V The type of Event that this ViewModel will emit
 * @param initialState The initial state of the UI
 */
abstract class BaseViewModel<I : BaseIntent, S : BaseState, M : UiModel, V : BaseEvent>(initialState: S) : ViewModel() {

    // State handling
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    // UI Model derived from state
    private val _uiModel = MutableStateFlow<M?>(null)
    val uiModel: StateFlow<M?> = _uiModel.asStateFlow()

    // Event handling for one-time events
    private val _event = MutableSharedFlow<V>()
    val event: SharedFlow<V> = _event.asSharedFlow()

    /**
     * Process a new user intent
     */
    fun processIntent(intent: I) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    /**
     * Handle the intent and update state or emit events accordingly
     * This method should be implemented by subclasses to handle specific intents
     */
    protected abstract suspend fun handleIntent(intent: I)

    /**
     * Update the current state
     */
    protected fun updateState(update: (S) -> S) {
        _state.update { currentState ->
            val newState = update(currentState)
            // When state is updated, automatically map to UI model
            mapStateToUiModel(newState)?.let { newUiModel ->
                _uiModel.value = newUiModel
            }
            newState
        }
    }

    /**
     * Map the current state to a UI model
     * This method should be implemented by subclasses to define how state is mapped to UI model
     */
    protected abstract fun mapStateToUiModel(state: S): M?

    /**
     * Emit a one-time event
     */
    protected suspend fun emitEvent(event: V) {
        _event.emit(event)
    }

}



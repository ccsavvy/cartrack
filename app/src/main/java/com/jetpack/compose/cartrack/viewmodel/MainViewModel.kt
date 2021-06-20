package com.jetpack.compose.cartrack.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jetpack.compose.cartrack.dao.UserDB
import com.jetpack.compose.cartrack.entities.ApiService
import com.jetpack.compose.cartrack.model.User
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    private val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
    private val String.hasSpecialCharacters: Boolean
        get() = regex.matcher(this).find()

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _username = MutableLiveData("")
    private val _password = MutableLiveData("")
    private val _userNameState = MutableLiveData(false)
    private val _passwordState = MutableLiveData(false)

    val username: LiveData<String> = _username
    val password: LiveData<String> = _password

    var usernameState: LiveData<Boolean> = _userNameState
    var passwordState: LiveData<Boolean> = _passwordState

    // onUserNameChange is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onUserNameChange(newUsername: String) {
        _username.value = newUsername
    }

    // onPasswordChange event
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    private fun storeRepositoriesLocally(data: User) {
        launch {
            val dao = UserDB(getApplication()).userRepositoryDao()
            dao.deleteAllUsers()
            val result = dao.saveUser(data)
        }
    }

    fun validateUsername(username: String): LoginUsernameException? {
        if (username.isBlank()) return LoginUsernameException.EmptyUsernameException(username = username)
        if (username.hasSpecialCharacters) return LoginUsernameException.InvalidUsernameException()
        return null
    }

    fun validatePassword(password: String): LoginPasswordException? {
        if (password.isBlank()) return LoginPasswordException.EmptyPasswordException(password)
        return null
    }

    sealed class LoginUsernameException(message: String? = null) : Exception(message) {
        class EmptyUsernameException(val username: String) : LoginUsernameException()
        class InvalidUsernameException : LoginUsernameException()
    }

    sealed class LoginPasswordException(message: String? = null) : Exception(message) {
        class EmptyPasswordException(val password: String) : LoginPasswordException()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
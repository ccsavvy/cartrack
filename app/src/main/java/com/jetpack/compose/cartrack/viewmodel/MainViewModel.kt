package com.jetpack.compose.cartrack.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jetpack.compose.cartrack.dao.UserDB
import com.jetpack.compose.cartrack.entities.ApiService
import com.jetpack.compose.cartrack.entities.CarTrackUsers
import com.jetpack.compose.cartrack.model.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.async
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
    private val _isRememberMeChecked = MutableLiveData(false)

    val username: LiveData<String> = _username
    val password: LiveData<String> = _password

    val usernameState: LiveData<Boolean> = _userNameState
    val passwordState: LiveData<Boolean> = _passwordState

    val isRememberMeChecked: LiveData<Boolean> = _isRememberMeChecked

    // onUserNameChange is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onUserNameChange(newUsername: String) {
        _username.value = newUsername
    }

    // onPasswordChange event
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onUserNameStateChange(newState: Boolean) {
        _userNameState.value = newState
    }

    fun onPasswordStateChange(newState: Boolean) {
        _passwordState.value = newState
    }

    fun onRememberMeIsChecked(newState: Boolean) {
        _isRememberMeChecked.value = newState
    }

    fun storeUserLocally(data: Repository) {
        launch {
            val dao = UserDB(getApplication()).userRepositoryDao()
            val result = dao.saveUser(
                Repository(
                    username = "cartrack",
                    password = "cartrack",
                    isRememberMeTicked = isRememberMeChecked.value ?: false
                )
            )
        }
    }

    fun validateUserLogin(): Boolean {
        var valid = true
        launch {
            valid = async {
                var result: List<Repository> = emptyList()
                if (_userNameState.value == false && _passwordState.value == false && _isRememberMeChecked.value == true) {
                    val dao = UserDB(getApplication()).userRepositoryDao()
                    result = dao.findUsernameByUsername(
                        username = _username.value ?: "cartrack",
                        password = _password.value ?: "cartrack",
                    )
                }

                return@async result.isNotEmpty()

            }.await()
        }
        return valid
    }

    fun fetchFromRemote() {
        disposable.add(
            apiService.getCarTrackUsers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CarTrackUsers>>() {

                    override fun onSuccess(data: List<CarTrackUsers>) {
                        Log.d("COMPOSE", data.toString())
                        Toast.makeText(
                            getApplication(),
                            "Repositories retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
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
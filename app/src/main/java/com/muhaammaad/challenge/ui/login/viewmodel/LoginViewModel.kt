package  com.muhaammaad.challenge.ui.login.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.muhaammaad.challenge.model.accountUser
import com.muhaammaad.challenge.util.SingleLiveEvent
import com.muhaammaad.challenge.util.Util
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class LoginViewModel(application: Application) : AndroidViewModel(application) {


    var btnSelected: ObservableBoolean? = null
    var email: ObservableField<String>? = null
    var password: ObservableField<String>? = null
    var progressDialog: SingleLiveEvent<Boolean>? = null
    var userLogin: MutableLiveData<accountUser>? = null

    init {
        btnSelected = ObservableBoolean(false)
        progressDialog = SingleLiveEvent<Boolean>()
        email = ObservableField("")
        password = ObservableField("")
        userLogin = MutableLiveData<accountUser>()
    }

    fun onEmailChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(s.toString().trim()) && password?.get()!!.length >= 6)
    }

    fun onPasswordChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        btnSelected?.set(Util.isEmailValid(email?.get()?.trim()!!) && s.toString().length >= 6)
    }

    fun signup() {
        login()
    }

    fun login() {
        progressDialog?.value = true
        val compositeDisposable =  CompositeDisposable()
        val disposable = Single.just(true)
                .subscribeOn(Schedulers.io())
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {value->
                    if (value)
                        onResponse()
                    else onFailure()
                    compositeDisposable.dispose()
                }

        compositeDisposable.add(disposable) //IDE is satisfied that the Disposable is being managed.
    }

    fun onResponse() {
        progressDialog?.value = false
        userLogin?.value = accountUser("Muhammad", "Ali", "moh.butt1@gmail.com")

    }

    fun onFailure() {
        progressDialog?.value = false
    }

}
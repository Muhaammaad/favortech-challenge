package  com.muhaammaad.challenge.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.ActivityLoginBinding
import com.muhaammaad.challenge.ui.home.HomeActivity
import com.muhaammaad.challenge.ui.login.viewmodel.LoginViewModel
import com.muhaammaad.challenge.util.CustomeProgressDialog
import com.muhaammaad.challenge.util.Prefs

/**
 * Registration Login view
 */
class LoginActivity : AppCompatActivity() {

    //region Members
    lateinit var customProgressDialog: CustomeProgressDialog
    lateinit var binding: ActivityLoginBinding
    lateinit var viewmodel: LoginViewModel
    lateinit var sharedPref: Prefs
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = Prefs(this)
        /**
         * if Logged in land to HomeScreen otherwise show registration
         */
        if (!sharedPref.token.isEmpty()) {
            openHomeActivity()
            return
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
            viewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
            binding.viewmodel = viewmodel
            customProgressDialog = CustomeProgressDialog(this)
            initObservables()
        }
    }

    private fun initObservables() {
        viewmodel.progressDialog?.observe(this, Observer {
            if (it!!) customProgressDialog.show() else customProgressDialog.dismiss()
        })

        viewmodel.userLogin?.observe(this, Observer { user ->
            if (user == null) {
                binding.etEmail.error = getString(R.string.wrong_credentials)
            } else user.let {
                //TODO: Should use Account Manager for account purposes rather than sharedPrefs, Not using currently because of time limit/demo
                sharedPref.email = it.email
                sharedPref.token = it.token
                Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_LONG).show()
                openHomeActivity()
            }
        })
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}

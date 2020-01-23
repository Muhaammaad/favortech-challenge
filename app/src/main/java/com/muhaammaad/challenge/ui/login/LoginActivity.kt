package  com.muhaammaad.challenge.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import  com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.ActivityLoginBinding
import  com.muhaammaad.challenge.ui.home.HomeActivity
import  com.muhaammaad.challenge.ui.login.viewmodel.LoginViewModel
import com.muhaammaad.challenge.util.CustomeProgressDialog

class LoginActivity : AppCompatActivity() {

    var binding: ActivityLoginBinding? = null
    var viewmodel: LoginViewModel? = null
    var customeProgressDialog: CustomeProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding?.viewmodel = viewmodel
        customeProgressDialog = CustomeProgressDialog(this)
        initObservables()
    }

    private fun initObservables() {
        viewmodel?.progressDialog?.observe(this, Observer {
            if (it!!) customeProgressDialog?.show() else customeProgressDialog?.dismiss()
        })

        viewmodel?.userLogin?.observe(this, Observer { user ->
            Toast.makeText(this, "welcome, ${user?.last_name}", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })
    }

}

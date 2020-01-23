package  com.muhaammaad.challenge.ui.home

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.muhaammaad.challenge.R


class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val ft = supportFragmentManager.beginTransaction()
        val fragment = PictureListFragment()
        ft.add(R.id.container, fragment).commit()
    }
}

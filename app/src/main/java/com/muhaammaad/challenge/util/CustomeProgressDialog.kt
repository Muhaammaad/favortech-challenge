package  com.muhaammaad.challenge.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import  com.muhaammaad.challenge.R


class CustomeProgressDialog(context: Context?) : Dialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}
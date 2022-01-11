package com.kunpark.resource.view.login

import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.kunpark.resource.R
import com.kunpark.resource.base.BaseActivity
import com.kunpark.resource.utils.*
import com.kunpark.resource.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : BaseActivity() {
    private val viewModel: LoginViewModel by viewModels ()
    override fun initEvent() {

    }

    override fun initView() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                // 2
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // 3
                val token = task.result?.token
            })
    }

    override fun initViewModel() {
        viewModel.loginSuccess.observe(this, Observer {
            if (it != null && it) {
                DazoneApplication.getInstance().mPref?.setString(Constants.USER_ID, etId.text.toString())
                DazoneApplication.getInstance().mPref?.setString(Constants.PASSWORD, etPass.text.toString())
                dismissProgressDialog()
                callActivity(MainActivity::class.java)
                finish()
            }
        })

        viewModel.hasSSL.observe(this, Observer {
            it?.let {
                login(etPass.text.toString(), etId.text.toString())
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            it?.let {
                dismissProgressDialog()
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin?.setOnClickListener {
            if (checkValidationLogin() == null) {
                Utils.setServerSite(etDomain.text.toString())
                checkSSL()
            } else {
                Toast.makeText(this, checkValidationLogin(), Toast.LENGTH_LONG).show()
            }
        }

        etDomain?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.COMPANY_NAME, ""))
        etPass?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.PASSWORD, ""))
        etId?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.USER_ID, ""))

        csMain?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            csMain.getWindowVisibleDisplayFrame(r)
            val screenHeight = csMain.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                imgLogo.visibility = View.GONE
            } else {
                imgLogo.visibility = View.VISIBLE
            }
        }
    }

    private fun login(password: String, userName: String) {
        val params = JsonObject()
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("companyDomain", DazoneApplication.getInstance().mPref?.getString(Constants.COMPANY_NAME, "") ?: "")
        params.addProperty("password", password)
        params.addProperty("userID", userName)
        params.addProperty("mobileOSVersion", "Android" + android.os.Build.VERSION.RELEASE)

        showProgressDialog()
        viewModel.login(params)
    }


    private fun checkSSL() {
        val params = JsonObject()
        params.addProperty("Domain", Locale.getDefault().language)
        params.addProperty("Applications", "CrewResource")

        showProgressDialog()
        viewModel.checkSSL(params)
    }

    private fun checkValidationLogin(): String? {
        return when {
            etDomain?.text?.isEmpty() == true -> {
                resources.getString(R.string.require_domain)
            }
            etId?.text?.isEmpty() == true -> {
                resources.getString(R.string.require_id)
            }
            etPass?.text?.isEmpty() == true -> {
                resources.getString(R.string.require_pass)
            }
            else -> null
        }
    }

    fun showHidePassword(view: View) {
        if (etPass.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            etPass.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imgShow.setImageResource(R.drawable.ic_show_password)
        } else {
            etPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imgShow.setImageResource(R.drawable.ic_hide_password)
        }
    }
}
package com.base.library.login.facebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.base.library.login.common.bean.LoginAuth
import com.base.library.login.common.constants.LoginConstants.Companion.FACEBOOK
import com.base.library.login.common.listener.OnLoginListener
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

/**
 * Description:
 * Facebook登录管理类
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2018/12/4
 */
class FacebookLoginManager(private val context: Context, private val onLoginListener: OnLoginListener) : FacebookCallback<LoginResult>,
        GraphRequest.Callback {

    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val loginButton by lazy { LoginButton(context) }
    val auth: LoginAuth = LoginAuth()

    /**
     * Facebook登录
     * 如果当前账号未退出，先退出当前账号。每次都向用户请求最新的Facebook账户登录
     * 之前使用的逻辑是如果当前账号未退出，则使用当前账户直接登录。导致的问题是切换了Facebook账号后，无法切换用户
     */
    fun login() {
        if (null != AccessToken.getCurrentAccessToken()) {
            LoginManager.getInstance().logOut()
        }
        loginButton.setReadPermissions(arrayListOf("email"))
        loginButton.registerCallback(callbackManager, this)
        loginButton.callOnClick()
    }

    override fun onSuccess(result: LoginResult?) {
        if (result == null || result.accessToken == null || TextUtils.isEmpty(result.accessToken.token)) {
            onLoginListener.onLoginFail(FACEBOOK, "Facebook Login fail, token is null")
            return
        }
        auth.token = result.accessToken.token
        getUserInfo(result.accessToken)
    }

    /**
     * 获取登录的用户公开信息
     */
    private fun getUserInfo(accessToken: AccessToken) {
        val params = Bundle().apply { putString("fields", "picture,name,id,email,permissions") }
        GraphRequest(accessToken, "/me", params, HttpMethod.GET, this).executeAsync()
    }

    override fun onCompleted(response: GraphResponse?) {
        val email = response?.jsonObject?.getString("email") ?: ""
        auth.email = email
        onLoginListener.onLoginSuccess(FACEBOOK, auth)
    }


    override fun onCancel() {
        onLoginListener.onLoginFail(FACEBOOK, "Facebook login cancel")
    }

    override fun onError(error: FacebookException?) {
        onLoginListener.onLoginFail(FACEBOOK, "Facebook login fail:$error")
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun release() {
        loginButton.unregisterCallback(callbackManager)
    }
}
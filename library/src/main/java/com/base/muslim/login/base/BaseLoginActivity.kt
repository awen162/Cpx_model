package com.base.muslim.login.base

import android.content.Intent
import com.base.muslim.base.activity.BaseActivity
import com.base.muslim.login.LoginManager
import com.base.muslim.login.common.listener.OnLoginListener

/**
 * Description:
 * 登录基类
 *
 * Facebook登录，需要用浏览器打开 Facebook开发者平台：https://developers.facebook.com/apps/
 * 1.创建应用，将应用编号赋值给 gradle.properties 的 facebook_app_id 变量
 * 2.在Facebook开发者平台控制台的 设置->基本 中添加Android平台，填入Google Play包名、类名、密钥散列、隐私权政策
 * 3.在Facebook开发者平台控制台 添加产品->Facebook登录， 按照步骤操作，在第五步中启用单点登录并保存（其他步骤此sdk中已做，无需更改）
 * 4.在Facebook开发者平台控制台公开发布应用
 *
 * Google登录，需要在firebase控制台创建项目：firebase.com
 * 1.身份验证中，启用Google登录
 * 2.项目设置中，填入SHA1和包名
 * 3.下载google_service.json，放入app模块下
 * 4.在身份验证 -> 登录方法 -> Google -> 网页SDK配置中找到网页客户端ID，将其赋值给 gradle.properties 的 google_web_client_id 变量
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2018/9/18
 */
abstract class BaseLoginActivity : BaseActivity(), OnLoginListener {

    private val loginManager by lazy { LoginManager(this, this) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginManager.handleActivityResult(requestCode, resultCode, data)
    }

    fun loginBy(type: String) {
        loginManager.loginBy(type)
    }
}
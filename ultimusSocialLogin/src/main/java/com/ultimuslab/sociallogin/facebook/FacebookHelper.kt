package com.ultimuslab.sociallogin.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ultimuslab.sociallogin.R
import org.jetbrains.annotations.NotNull
import java.util.*

class FacebookHelper(private val mListener: FacebookListener) {
    var callbackManager: CallbackManager = CallbackManager.Factory.create()

    fun performSignIn(activity: Activity?) {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                activity,
                Arrays.asList("public_profile", "user_friends", "email")
            )
    }

    fun performSignIn(fragment: Fragment?) {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                fragment,
                Arrays.asList("public_profile", "user_friends", "email")
            )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun performSignOut() {
        LoginManager.getInstance().logOut()
        mListener.onFBSignOut()
    }

    init {
        var lManager = LoginManager.getInstance()

        val mCallBack: FacebookCallback<LoginResult> = object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                var accessToken: AccessToken? = loginResult.accessToken

                var token = accessToken?.token

                val request = GraphRequest.newMeRequest(accessToken) { data, _ ->

                    //here is the data that you want
                    if (data.getString("id").isNotEmpty()) {
                        val name = data.getString("name")
                        val email = data.getString("email")
                        val id = data.getString("id")
                        val avatar = data.getJSONObject("picture").getJSONObject("data")
                            .getString("url")
//                                var image_url = "https://graph.facebook.com/$id/picture?type=normal"

                        mListener.onFbSignInSuccess(
                            token.toString(),
//                            loginResult.accessToken.userId,
                            id,
                            name,
                            email,
                            avatar

                        )


                    } else {
                        mListener.onFbSignInFail("User id not found")
                        lManager.logOut()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "name,email,id,picture.type(large)")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                mListener.onFbSignInFail("User cancelled operation")
                lManager.logOut()

            }

            override fun onError(e: FacebookException) {
                mListener.onFbSignInFail(e.message)
                lManager.logOut()

            }
        }
        lManager.registerCallback(callbackManager, mCallBack)
    }
}
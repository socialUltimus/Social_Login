package com.ultimuslab.sociallogin.twitter

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient


class TwitterHelper(
    @field:NonNull @param:NonNull private val mListener: TwitterListener,
    @field:NonNull @param:NonNull private val mActivity: Activity,
    @field:NonNull @param:NonNull private val mTwitterApiKey: String,
    @field:NonNull @param:NonNull private val mTwitterSecreteKey: String
) {
    private val mAuthClient: TwitterAuthClient?

    private val mCallback: Callback<TwitterSession> = object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession?>) {
            val session: TwitterSession? = result.data
            mListener.onTwitterSignIn(
                session?.getAuthToken()?.token, session?.getAuthToken()?.secret,
                session?.getUserId() ?: 0
            )

            //call fetch email only when permission is granted
            session?.let {

                fetchTwitterEmail(it)
            }

        }

        override fun failure(exception: TwitterException) {
            mListener.onTwitterError(exception.message)
        }
    }

    fun performSignIn() {
        //check if user is already authenticated or not

        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            mAuthClient?.authorize(
                mActivity,
                mCallback
            )
        } else {
            //if user is already authenticated direct call fetch twitter email api
//            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show()
            getTwitterSession()?.let {
                fetchTwitterEmail(it)
            }
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mAuthClient?.onActivityResult(requestCode, resultCode, data)
    }

    init {
        val config = TwitterConfig.Builder(mActivity)
            .logger(DefaultLogger(Log.DEBUG)) //enable logging when app is in debug mode
            .twitterAuthConfig(
                TwitterAuthConfig(
                    mTwitterApiKey,
                    mTwitterSecreteKey
                )
            ) //pass the created app Consumer KEY and Secret also called API Key and Secret
            .debug(true) //enable debug mode
            .build()

        //finally initialize twitter with created configs

        //finally initialize twitter with created configs
        Twitter.initialize(config)

        mAuthClient = TwitterAuthClient()
    }


    private fun getTwitterSession(): TwitterSession? {

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/
        return TwitterCore.getInstance().sessionManager.activeSession
    }

    fun fetchTwitterEmail(twitterSession: TwitterSession) {
        mAuthClient?.requestEmail(
            twitterSession,
            object : Callback<String>() {
                override fun success(result: Result<String>) {
                    //here it will give u only email and rest of other information u can get from TwitterSession
//                    userDetailsLabel.setText(
//                        """
//                            User Id : ${twitterSession.userId}
//                            Screen Name : ${twitterSession.userName}
//                            Email Id : ${result.data}
//                            """.trimIndent()
//                    )
                }

                override fun failure(exception: TwitterException) {
//                    Toast.makeText(
//                        this@TwitterHelper,
//                        "Failed to authenticate. Please try again.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })
    }

}
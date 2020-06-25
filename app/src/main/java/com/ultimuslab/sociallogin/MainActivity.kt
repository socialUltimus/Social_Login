package com.ultimuslab.sociallogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ultimuslab.sociallogin.facebook.FacebookHelper
import com.ultimuslab.sociallogin.facebook.FacebookListener
import com.ultimuslab.sociallogin.instagram.InstagramHelper
import com.ultimuslab.sociallogin.instagram.InstagramListener
import com.ultimuslab.sociallogin.twitter.TwitterHelper
import com.ultimuslab.sociallogin.twitter.TwitterListener
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity(), FacebookListener, TwitterListener, InstagramListener {

    private var mFacebook: FacebookHelper? = null
    private var mTwitter: TwitterHelper? = null
    private var mInstagram: InstagramHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFacebook = FacebookHelper(this)
        mTwitter = TwitterHelper(this, this, "Your Twitter Api Key", "Your Twitter Api Secret");
        mInstagram = InstagramHelper(
            this,
            this,
            "3003774029706274",
            "c280ca824060783da320667c5b018bf7",
            "https://instagram.com/"
        )

        facebook_button.setOnClickListener {
            mFacebook?.performSignIn(this)
        }

        twitter_button.setOnClickListener {
            mTwitter?.performSignIn()
        }

        instagram_button.setOnClickListener {
            mInstagram?.performSignIn()
        }


//        val info: PackageInfo
//        try {
//            info = packageManager.getPackageInfo("com.ultimuslab.sociallogin", PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                var md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val something = String(Base64.encode(md.digest(), 0))
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something)
//            }
//        } catch (e1: PackageManager.NameNotFoundException) {
//            Log.e("name not found", e1.toString())
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e("no such an algorithm", e.toString())
//        } catch (e: Exception) {
//            Log.e("exception", e.toString())
//        }

//        ObtjjPaRAneIbjBL83v/UkRk3q4=

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFacebook?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onFbSignInFail(errorMessage: String?) {
        data_received_text_view.setText(errorMessage)
    }

    override fun onFbSignInSuccess(
        accessToken: String,
        userId: String?,
        name: String,
        email: String,
        avatar: String
    ) {

        data_received_text_view.setText("NAME:- ${name} \n EMAIL:- ${email} \n ID:- ${userId} \n AVATAR:- ${avatar} \n TOKEN:- ${accessToken}")

    }

    override fun onFBSignOut() {
    }

    override fun onTwitterError(errorMessage: String?) {
        data_received_text_view.setText(errorMessage)
    }

    override fun onTwitterSignIn(authToken: String?, secret: String?, userId: Long) {
        data_received_text_view.setText(userId.toString())

    }

    override fun onInstagramSignInFail(errorMessage: String?) {
        data_received_text_view.setText(errorMessage)

    }

    override fun onInstagramSignInSuccess(
        authToken: String?,
        userId: String?,
        userName: String?,
        userEmail: String?,
        avatar: String?
    ) {

        data_received_text_view.setText("NAME:- ${userName} \n EMAIL:- ${userEmail} \n ID:- ${userId} \n AVATAR:- ${avatar} \n TOKEN:- ${authToken}")


    }
}
package com.ultimuslab.sociallogin.facebook


interface FacebookListener {
    fun onFbSignInFail(errorMessage: String?)
    fun onFbSignInSuccess(
        authToken: String,
        userId: String?,
        name: String,
        email: String,
        avatar: String
    )

    fun onFBSignOut()
}
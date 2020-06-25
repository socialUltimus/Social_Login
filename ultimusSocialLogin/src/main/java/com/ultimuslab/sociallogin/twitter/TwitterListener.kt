package com.ultimuslab.sociallogin.twitter

interface TwitterListener {
    fun onTwitterError(errorMessage: String?)
    fun onTwitterSignIn(
        authToken: String?,
        secret: String?,
        userId: Long
    )
}
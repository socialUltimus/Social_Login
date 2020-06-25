package com.ultimuslab.sociallogin.instagram

interface InstagramListener {
    fun onInstagramSignInFail(errorMessage: String?)
    fun onInstagramSignInSuccess(
        authToken: String?,
        userId: String?,
        userName: String?,
        userEmail: String?,
        avatar: String?
    )
}
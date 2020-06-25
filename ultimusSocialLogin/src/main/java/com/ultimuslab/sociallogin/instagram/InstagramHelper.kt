package com.ultimuslab.sociallogin.instagram

import android.R
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.ultimuslab.sociallogin.instagram.InstagramDialog.OAuthDialogListener
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class InstagramHelper(
    private val mListener: InstagramListener,
    val context: Context,
    private val mClientId: String,
    private val mClientSecret: String, callbackUrl: String
) {
    private val mDialog: InstagramDialog
    private var mProgress: ProgressDialog? = null
    private var mAccessToken: String? = null
    private var mUserId: String? = null

    fun performSignIn() {
        if (mAccessToken != null) {
            mAccessToken = null
        } else {
            mDialog.show()
        }
    }

    private fun getAccessToken(response: String) {
        mProgress?.setMessage("Logging in...")
        mProgress?.show()
        object : Thread() {
            override fun run() {
                Log.i(TAG, "Getting access token")
                var what = WHAT_FETCH_INFO
                try {
                    val url = URL(TOKEN_URL)
                    val urlConnection =
                        url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "POST"
                    urlConnection.doInput = true
                    urlConnection.doOutput = true
                    val writer =
                        OutputStreamWriter(urlConnection.outputStream)
                    writer.write(
                        "client_id="
                                + mClientId
                                + "&client_secret="
                                + mClientSecret
                                + "&grant_type=authorization_code"
                                + "&redirect_uri="
                                + mCallbackUrl
                                + "&code="
                                + response
                    )
                    writer.flush()
                    val response = streamToString(urlConnection.inputStream)
                    Log.i(TAG, "response $response")
                    val jsonObj = JSONTokener(response).nextValue() as JSONObject
                    mAccessToken = jsonObj.getString("access_token")
//                    mUserId = jsonObj.getJSONObject("user").getString("id")
                    mUserId = jsonObj.getString("user_id")
                } catch (ex: Exception) {
                    what = WHAT_ERROR
                    ex.printStackTrace()
                }
                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0))
            }
        }.start()
    }

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            mProgress?.dismiss()
            if (msg.what == WHAT_ERROR) {
                if (msg.arg1 == 1) {
                    mListener.onInstagramSignInFail("Failed to get access token")
                } else if (msg.arg1 == 2) {
                    mListener.onInstagramSignInFail("Failed to get user information")
                }
            } else {
//                mListener.onInstagramSignInSuccess(mAccessToken, mUserId)
                getUserInfoByAccessToken()
            }
        }
    }

    private fun streamToString(`is`: InputStream?): String {
        var str = ""
        if (`is` != null) {
            val sb = StringBuilder()
            var line: String?
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(InputStreamReader(`is`))
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    `is`.close()
                    reader?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            str = sb.toString()
        }
        return str
    }

    companion object {
        private const val WHAT_ERROR = 1
        private const val WHAT_FETCH_INFO = 2

        @JvmField
        var mCallbackUrl = ""
        private const val AUTH_URL = "https://api.instagram.com/oauth/authorize"
        private const val TOKEN_URL = "https://api.instagram.com/oauth/access_token"
        private const val API_URL = "https://graph.instagram.com/"
        private const val USER_PROFILE_URL = "https://graph.instagram.com/me"
        private const val TAG = "InstagramAPI"
    }

    init {
        mCallbackUrl = callbackUrl
        val authUrl = (AUTH_URL
                + "?client_id="
                + mClientId
                + "&redirect_uri="
                + mCallbackUrl
//                + "&response_type=code&display=touch&scope=likes+comments+relationships")
                + "&scope=user_profile,user_media"
                + "&response_type=code")
        mDialog = InstagramDialog(context, authUrl, object : OAuthDialogListener {
            override fun onComplete(code: String) {
                getAccessToken(code.removeSuffix("#_"))
            }

            override fun onError(error: String) {
                mListener.onInstagramSignInFail("Authorization failed")
            }
        })
        mProgress = ProgressDialog(context)
        mProgress?.setCancelable(false)
    }

    private fun getUserInfoByAccessToken() {
        RequestInstagramAPI().execute()
    }

    inner class RequestInstagramAPI : AsyncTask<Void?, String?, String>() {

        override fun onPostExecute(response: String?) {
            if (response != null) {
                try {
                    val jsonData = JSONObject(response)
//                    val jsonData = jsonObject.getJSONObject("data")
                    if (jsonData.has("id")) {

                        val userId = jsonData.getString("id")
                        val userName = jsonData.getString("username")
//                        val profilePicture = jsonData.getString("profile_picture")

                        mListener.onInstagramSignInSuccess(
                            mAccessToken,
                            userId,
                            userName,
                            "not",
                            "not"

                        )

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                val toast = Toast.makeText(context, "Login error!", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        override fun doInBackground(vararg p0: Void?): String? {
            val httpClient: HttpClient = DefaultHttpClient()
            val httpGet = HttpGet(
                USER_PROFILE_URL
                        + "?fields=id,username&access_token="
                        + mAccessToken
            )
            try {
                val response: HttpResponse = httpClient.execute(httpGet)
                val httpEntity: HttpEntity = response.getEntity()
                return EntityUtils.toString(httpEntity)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}
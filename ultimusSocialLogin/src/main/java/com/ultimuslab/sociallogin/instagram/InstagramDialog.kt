package com.ultimuslab.sociallogin.instagram

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class InstagramDialog
/**
 * @param context  instance of caller
 * @param url      requested url for instagram login
 * @param listener response to instagram helper if login fail or success.
 */(
    context: Context?,
    private val mUrl: String,
    private val mListener: OAuthDialogListener
) : Dialog(context!!) {
    private var mSpinner: ProgressDialog? = null
    private var mWebView: WebView? = null
    private var mContent: LinearLayout? = null
    private var mTitle: TextView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        mSpinner = ProgressDialog(context)
        mSpinner!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mSpinner!!.setMessage("Loading...")
        mContent = LinearLayout(context)
        mContent!!.orientation = LinearLayout.VERTICAL
        setUpTitle()
        setUpWebView()
        val display = window!!.windowManager.defaultDisplay
        val scale = context.resources.displayMetrics.density
        val dimensions =
            if (display.width < display.height) DIMENSIONS_PORTRAIT else DIMENSIONS_LANDSCAPE
        addContentView(
            mContent!!, FrameLayout.LayoutParams(
                (dimensions[0] * scale + 0.5f).toInt(),
                (dimensions[1] * scale + 0.5f).toInt()
            )
        )
        //      addContentView(mContent, FILL);
        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
    }

    /**
     * Set title of dialog
     */
    private fun setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mTitle = TextView(context)
        mTitle!!.text = "Instagram"
        mTitle!!.setTextColor(Color.WHITE)
        mTitle!!.typeface = Typeface.DEFAULT_BOLD
        mTitle!!.setBackgroundColor(Color.BLACK)
        mTitle!!.setPadding(
            MARGIN + PADDING,
            MARGIN,
            MARGIN,
            MARGIN
        )
        mContent!!.addView(mTitle)
    }

    /**
     * Creating webview for instagram dialog login
     */
    private fun setUpWebView() {
        mWebView = WebView(context)
        mWebView!!.isVerticalScrollBarEnabled = false
        mWebView!!.isHorizontalScrollBarEnabled = false
        mWebView!!.webViewClient = OAuthWebViewClient()
        //        mWebView.setNestedScrollingEnabled(true);
//        mWebView.setScrollContainer(true);
//        mWebView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setLoadWithOverviewMode(false);
//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.loadUrl(mUrl)
        mWebView!!.layoutParams = FILL
        mContent!!.addView(mWebView)
    }

    /**
     * Customize webview client
     */
    private inner class OAuthWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            Log.d(TAG, "Redirecting URL $url")
            if (url.startsWith(InstagramHelper.mCallbackUrl)) {
                val urls = url.split("=".toRegex()).toTypedArray()
                mListener.onComplete(urls[1])
                dismiss()
                return true
            }
            return false
        }

        override fun onReceivedError(
            view: WebView, errorCode: Int, description: String,
            failingUrl: String
        ) {
            Log.d(TAG, "Page error: $description")
            super.onReceivedError(view, errorCode, description, failingUrl)
            mListener.onError(description)
            dismiss()
        }

        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap
        ) {
            Log.d(TAG, "Loading URL: $url")
            super.onPageStarted(view, url, favicon)
            mSpinner!!.show()
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            val title = mWebView!!.title
            if (title != null && title.length > 0) {
                mTitle!!.text = title
            }
            Log.d(TAG, "onPageFinished URL: $url")
            mSpinner!!.dismiss()
        }
    }

    /**
     * Callback between [InstagramDialog] to [InstagramHelper].
     */
    interface OAuthDialogListener {
        //If user success fully sign in
        fun onComplete(accessToken: String?)

        //If error occurs while sign in
        fun onError(error: String?)
    }

    companion object {
        private val DIMENSIONS_LANDSCAPE = floatArrayOf(460f, 260f)
        private val DIMENSIONS_PORTRAIT = floatArrayOf(340f, 420f)
        private val FILL = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        const val MARGIN = 4
        const val PADDING = 2
        private const val TAG = "Instagram-WebView"
    }

}
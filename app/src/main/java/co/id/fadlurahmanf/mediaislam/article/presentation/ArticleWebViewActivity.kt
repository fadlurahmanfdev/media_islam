package co.id.fadlurahmanf.mediaislam.article.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.article.BaseArticleActivity
import co.id.fadlurahmanf.mediaislam.databinding.ActivityArticleWebViewBinding

class ArticleWebViewActivity :
    BaseArticleActivity<ActivityArticleWebViewBinding>(ActivityArticleWebViewBinding::inflate) {
    companion object {
        const val URL = "URL"
    }

    override fun onBaseQuranInjectActivity() {}

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(true)
        setOnApplyWindowInsetsListener(binding.main)

        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webview.canGoBack()) {
                    binding.webview.goBack()
                } else {
                    finish()
                }
            }
        })
        val url = intent.extras?.getString(URL)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE

                view?.loadUrl(
                    "javascript:(function() { " +
                            "document.getElementsByClassName('header-wrapper')[0].style.display='none'; })()"
                )
                view?.loadUrl(
                    "javascript:(function() { " +
                            "document.getElementsByClassName('sidebar')[0].style.display='none'; })()"
                )
                view?.loadUrl(
                    "javascript:(function() { " +
                            "document.getElementsByClassName('code-block code-block-1')[0].style.display='none'; })()"
                )
            }
        }

        if (url != null) {
            binding.webview.loadUrl(url)
        }
    }

}
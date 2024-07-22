package co.id.fadlurahmanf.mediaislam.article.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.article.BaseArticleActivity
import co.id.fadlurahmanf.mediaislam.databinding.ActivityArticleListBinding

class ArticleListActivity : BaseArticleActivity<ActivityArticleListBinding>(ActivityArticleListBinding::inflate) {
    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    private val articleFragment = ArticleFragment()
    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

        binding.toolbar.tvTitle.text = "Artikel"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl, articleFragment).commit()
    }

}
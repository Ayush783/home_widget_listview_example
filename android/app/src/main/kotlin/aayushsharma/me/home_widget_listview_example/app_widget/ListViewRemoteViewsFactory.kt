package aayushsharma.me.home_widget_listview_example.app_widget

import aayushsharma.me.home_widget_listview_example.R
import aayushsharma.me.home_widget_listview_example.app_widget.models.NewsArticle
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListViewRemoteViewsFactory(private val context: Context, private val intent: Intent?) : RemoteViewsService.RemoteViewsFactory{
    private var articles: List<NewsArticle>? = null
    override fun onCreate() {
        val articlesJson: String? = intent?.extras?.getString("ARTICLES_JSON","")

        if(articlesJson != null) {
            val articleListType = object : TypeToken<List<NewsArticle>>() {}.type
            val data: List<NewsArticle> = Gson().fromJson(articlesJson, articleListType)
            articles = data
        }
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return articles!!.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val article: NewsArticle = articles!![position]
        val views = RemoteViews(context.packageName, R.layout.list_tile)
        views.setTextViewText(R.id.title, article.title)
        views.setImageViewResource(R.id.header_image, R.drawable.ic_launcher)

        val fillInIntent = Intent()
        fillInIntent.data = Uri.parse("package://<Add required data you need to pass>")
        views.setOnClickFillInIntent(R.id.list_tile, fillInIntent)

        return views
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return  1
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
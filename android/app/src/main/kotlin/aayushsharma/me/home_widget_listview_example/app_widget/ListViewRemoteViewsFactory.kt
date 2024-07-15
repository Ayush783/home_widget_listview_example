package aayushsharma.me.home_widget_listview_example.app_widget

import aayushsharma.me.home_widget_listview_example.R
import aayushsharma.me.home_widget_listview_example.app_widget.models.NewsArticle
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListViewRemoteViewsFactory(private val context: Context, private val intent: Intent?) : RemoteViewsService.RemoteViewsFactory{
    private var articles: List<NewsArticle>? = null
    private var articleImages: List<Bitmap?> = ArrayList()
    override fun onCreate() {
        val articlesJson: String? = intent?.extras?.getString("ARTICLES_JSON","")

        if(articlesJson != null) {
            val articleListType = object : TypeToken<List<NewsArticle>>() {}.type
            val data: List<NewsArticle> = Gson().fromJson(articlesJson, articleListType)
            articles = data
            LoadImagesTask().execute(articles)
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

        if (position < articleImages.size && articleImages[position] != null) {
            views.setImageViewBitmap(R.id.header_image, articleImages[position])
        } else {
            views.setImageViewResource(R.id.header_image, R.drawable.ic_launcher) // Placeholder image
        }

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

    private inner class LoadImagesTask : AsyncTask<List<NewsArticle>, Void, List<Bitmap?>>() {

        override fun doInBackground(vararg params: List<NewsArticle>): List<Bitmap?> {
            return params[0].map { article ->
                try {
                    Glide.with(context)
                        .asBitmap()
                        .load(article.urlToImage)
                        .submit(65, 65)
                        .get()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_launcher)
                        .submit(65, 65)
                        .get()
                    null
                }
            }
        }

        override fun onPostExecute(result: List<Bitmap?>) {
            articleImages = result
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, ListViewAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview_app_widget)
        }
    }
}
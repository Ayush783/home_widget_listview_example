package aayushsharma.me.home_widget_listview_example.app_widget.worker

import aayushsharma.me.home_widget_listview_example.app_widget.ListViewAppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UpdateWidgetWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            fetchData()
        }
    }

    private suspend fun fetchData(): Result {
        val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=1a673055bd244c02b610c387e99b058e"
        val request = Request.Builder().url(url).build()
        return try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            if (response.isSuccessful && responseData != null) {
                val jsonObject = JSONObject(responseData)
                val data = jsonObject.getJSONArray("articles")
                saveDataToHomeWidgetPreferences(data.toString())
                updateWidget(data.toString())
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun updateWidget(data: String) {
        val widgetId = inputData.getInt("widgetId", 0)
        val views = ListViewAppWidgetProvider.createListView(applicationContext, widgetId, data)
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        appWidgetManager.updateAppWidget(widgetId, views)
    }

    private fun saveDataToHomeWidgetPreferences( data: String) {
        val preferences = applicationContext.getSharedPreferences("HomeWidgetPreferences",Context.MODE_PRIVATE).edit()
        preferences.putString("NEWS_ARTICLES_DATA", data)
        preferences.commit()
    }
}
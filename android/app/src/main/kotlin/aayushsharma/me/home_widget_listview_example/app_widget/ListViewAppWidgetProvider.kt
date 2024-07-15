package aayushsharma.me.home_widget_listview_example.app_widget

import aayushsharma.me.home_widget_listview_example.MainActivity
import aayushsharma.me.home_widget_listview_example.R
import aayushsharma.me.home_widget_listview_example.app_widget.service.ListViewWidgetService
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.RemoteViews
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import aayushsharma.me.home_widget_listview_example.app_widget.worker.UpdateWidgetWorker
import es.antonborri.home_widget.HomeWidgetPlugin
import es.antonborri.home_widget.HomeWidgetProvider

class ListViewAppWidgetProvider : HomeWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        widgetData: SharedPreferences
    ) {
        val data = HomeWidgetPlugin.getData(context).getString("NEWS_ARTICLES_DATA","")
        appWidgetIds.forEach { id ->
            var views: RemoteViews?
            if(data!!.isNotEmpty()) {
                views = createListView(context, id, data)
            } else {
                views = RemoteViews(context.packageName,R.layout.listview_widget_loader)
                initiateWorkRequest(context, id)
            }

            appWidgetManager.updateAppWidget(id, views)
        }
    }

    private fun initiateWorkRequest(context: Context, id: Int) {
        val workRequest: WorkRequest = OneTimeWorkRequest.Builder(UpdateWidgetWorker::class.java).setInputData(
            Data.Builder().putInt("widgetId", id).build()
        ).build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    companion object {
        fun createListView(context: Context, widgetId: Int, data: String): RemoteViews {
            val intent = Intent(
                context,
                ListViewWidgetService::class.java
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            intent.putExtra("ARTICLES_JSON", data)

            val views = RemoteViews(context.packageName, R.layout.listview_app_widget)
            views.setRemoteAdapter(R.id.listview_app_widget, intent)

            val pendingIntentWithData = getPendingIntent(
                context,
                MainActivity::class.java
            )
            views.setPendingIntentTemplate(R.id.listview_app_widget, pendingIntentWithData)

            return views
        }

        private fun getPendingIntent(context: Context, activityClass: Class<MainActivity>) : PendingIntent {
            val intent = Intent(context, activityClass)
            intent.action = "es.antonborri.home_widget.action.LAUNCH"

            var flags = PendingIntent.FLAG_UPDATE_CURRENT
            if (Build.VERSION.SDK_INT >= 23) {
                flags = flags or PendingIntent.FLAG_MUTABLE
            }

            return PendingIntent.getActivity(context, 0, intent, flags)
        }
    }
}
package aayushsharma.me.home_widget_listview_example.app_widget

import aayushsharma.me.home_widget_listview_example.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class ListViewRemoteViewsFactory(private val context: Context, private val intent: Intent?) : RemoteViewsService.RemoteViewsFactory{
    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.list_tile)
        views.setTextViewText(R.id.title, "Sample title. Position: $position")
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
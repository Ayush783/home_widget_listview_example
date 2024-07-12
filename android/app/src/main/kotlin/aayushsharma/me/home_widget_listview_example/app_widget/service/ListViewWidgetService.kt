package aayushsharma.me.home_widget_listview_example.app_widget.service

import aayushsharma.me.home_widget_listview_example.app_widget.ListViewRemoteViewsFactory
import android.content.Intent
import android.widget.RemoteViewsService

class ListViewWidgetService : RemoteViewsService(){
    override fun onGetViewFactory(intent: Intent?): RemoteViewsService.RemoteViewsFactory {
        return ListViewRemoteViewsFactory(this.applicationContext, intent)
    }
}
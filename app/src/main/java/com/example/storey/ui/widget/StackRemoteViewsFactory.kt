package com.example.storey.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.storey.R
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.helper.urlToBitmap

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()


    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val db = MainDatabase.getDatabase(mContext.applicationContext)
        db.storyWidgetDao().getAllStoryWidget().forEach {
            mWidgetItems.add(urlToBitmap(it.photoUrl)!!)
        }
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            ImageStoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
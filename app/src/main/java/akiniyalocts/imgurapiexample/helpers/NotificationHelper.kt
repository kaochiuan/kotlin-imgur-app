package akiniyalocts.imgurapiexample.helpers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat

import java.lang.ref.WeakReference

import akiniyalocts.imgurapiexample.R
import akiniyalocts.imgurapiexample.imgurmodel.ImageResponse
import android.support.v4.content.ContextCompat

/**
 * Created by AKiniyalocts on 1/15/15.
 *
 *
 * This class is just created to help with notifications, definitely not necessary.
 */
class NotificationHelper(context: Context) {

    private val mContext: WeakReference<Context>


    init {
        this.mContext = WeakReference(context)
    }

    fun createUploadingNotification() {
        val mBuilder = NotificationCompat.Builder(mContext.get()!!, "channelId")
        mBuilder.setSmallIcon(android.R.drawable.ic_menu_upload)
        mBuilder.setContentTitle(mContext.get()!!.getString(R.string.notification_progress))


        mBuilder.color = ContextCompat.getColor(mContext.get()!!, R.color.primary)

        mBuilder.setAutoCancel(true)

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), mBuilder.build())

    }

    fun createUploadedNotification(response: ImageResponse) {
        val mBuilder = NotificationCompat.Builder(mContext.get())
        mBuilder.setSmallIcon(android.R.drawable.ic_menu_gallery)
        mBuilder.setContentTitle(mContext.get()!!.getString(R.string.notifaction_success))

        mBuilder.setContentText(response.data!!.link)

        mBuilder.color = ContextCompat.getColor(mContext.get()!!, R.color.primary)


        val resultIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.data!!.link))
        val intent = PendingIntent.getActivity(mContext.get(), 0, resultIntent, 0)
        mBuilder.setContentIntent(intent)
        mBuilder.setAutoCancel(true)

        val shareIntent = Intent(Intent.ACTION_SEND, Uri.parse(response.data!!.link))
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, response.data!!.link)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pIntent = PendingIntent.getActivity(mContext.get(), 0, shareIntent, 0)
        mBuilder.addAction(NotificationCompat.Action(R.drawable.abc_ic_menu_share_mtrl_alpha,
                mContext.get()!!.getString(R.string.notification_share_link), pIntent))

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), mBuilder.build())
    }

    fun createFailedUploadNotification() {
        val mBuilder = NotificationCompat.Builder(mContext.get()!!, "channelId")
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert)
        mBuilder.setContentTitle(mContext.get()!!.getString(R.string.notification_fail))


        mBuilder.color = ContextCompat.getColor(mContext.get()!!, R.color.primary)

        mBuilder.setAutoCancel(true)

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), mBuilder.build())
    }

    companion object {
        val TAG = NotificationHelper::class.java.simpleName
    }
}

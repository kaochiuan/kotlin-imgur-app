package akiniyalocts.imgurapiexample.services

import android.content.Context

import java.lang.ref.WeakReference

import akiniyalocts.imgurapiexample.Constants
import akiniyalocts.imgurapiexample.helpers.NotificationHelper
import akiniyalocts.imgurapiexample.imgurmodel.ImageResponse
import akiniyalocts.imgurapiexample.imgurmodel.ImgurAPI
import akiniyalocts.imgurapiexample.imgurmodel.Upload
import akiniyalocts.imgurapiexample.utils.NetworkUtils
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import retrofit.mime.TypedFile

/**
 * Created by AKiniyalocts on 1/12/15.
 *
 *
 * Our upload service. This creates our restadapter, uploads our image, and notifies us of the response.
 */
class UploadService(context: Context) {

    private val mContext: WeakReference<Context>

    init {
        this.mContext = WeakReference(context)
    }

    fun Execute(upload: Upload, callback: Callback<ImageResponse>?) {
        val cb = callback

        if (!NetworkUtils.isConnected(mContext.get()!!)) {
            //Callback will be called, so we prevent a unnecessary notification
            cb!!.failure(null)
            return
        }

        val notificationHelper = NotificationHelper(mContext.get()!!)
        notificationHelper.createUploadingNotification()

        val restAdapter = buildRestAdapter()

        restAdapter.create(ImgurAPI::class.java).postImage(
                Constants.clientAuth,
                upload.title!!,
                upload.description!!,
                upload.albumId!!, "",
                TypedFile("image/*", upload.image!!),
                object : Callback<ImageResponse> {
                    override fun success(imageResponse: ImageResponse, response: Response?) {
                        cb?.success(imageResponse, response)
                        if (response == null) {
                            /*
                             Notify image was NOT uploaded successfully
                            */
                            notificationHelper.createFailedUploadNotification()
                            return
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (imageResponse.success) {
                            notificationHelper.createUploadedNotification(imageResponse)
                        }
                    }

                    override fun failure(error: RetrofitError) {
                        cb?.failure(error)
                        notificationHelper.createFailedUploadNotification()
                    }
                })
    }

    private fun buildRestAdapter(): RestAdapter {
        val imgurAdapter = RestAdapter.Builder()
                .setEndpoint(ImgurAPI.server)
                .build()

        /*
        Set rest adapter logging if we're already logging
        */
        if (Constants.LOGGING)
            imgurAdapter.logLevel = RestAdapter.LogLevel.FULL
        return imgurAdapter
    }

    companion object {
        val TAG = UploadService::class.java.simpleName
    }
}

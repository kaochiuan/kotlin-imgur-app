package akiniyalocts.imgurapiexample.imgurmodel

import retrofit.Callback
import retrofit.http.Body
import retrofit.http.Header
import retrofit.http.POST
import retrofit.http.Query
import retrofit.mime.TypedFile

/**
 * Created by AKiniyalocts on 2/23/15.
 *
 *
 * This is our imgur API. It generates a rest API via Retrofit from Square inc.
 *
 *
 * more here: http://square.github.io/retrofit/
 */
interface ImgurAPI {


    /****************************************
     * Upload
     * Image upload API
     */

    /**
     * @param auth        #Type of authorization for upload
     * @param title       #Title of image
     * @param description #Description of image
     * @param albumId     #ID for album (if the user is adding this image to an album)
     * @param username    username for upload
     * @param file        image
     * @param cb          Callback used for success/failures
     */
    @POST("/3/image")
    fun postImage(
            @Header("Authorization") auth: String,
            @Query("title") title: String,
            @Query("description") description: String,
            @Query("album") albumId: String,
            @Query("account_url") username: String,
            @Body file: TypedFile,
            cb: Callback<ImageResponse>
    )

    companion object {
        val server = "https://api.imgur.com"
    }
}

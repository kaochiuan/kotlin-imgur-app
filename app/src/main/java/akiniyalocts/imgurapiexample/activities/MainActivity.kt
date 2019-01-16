package akiniyalocts.imgurapiexample.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.squareup.picasso.Picasso

import java.io.File

import akiniyalocts.imgurapiexample.R
import akiniyalocts.imgurapiexample.helpers.DocumentHelper
import akiniyalocts.imgurapiexample.helpers.IntentHelper
import akiniyalocts.imgurapiexample.imgurmodel.ImageResponse
import akiniyalocts.imgurapiexample.imgurmodel.Upload
import akiniyalocts.imgurapiexample.services.UploadService
import android.app.Activity
import android.view.View
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MainActivity : AppCompatActivity() {

    /*
      These annotations are for ButterKnife by Jake Wharton
      https://github.com/JakeWharton/butterknife
     */
    @Bind(R.id.imageview)
    internal var uploadImage: ImageView? = null
    @Bind(R.id.editText_upload_title)
    internal var uploadTitle: EditText? = null
    @Bind(R.id.editText_upload_desc)
    internal var uploadDesc: EditText? = null
    @Bind(R.id.toolbar)
    internal var toolbar: Toolbar? = null

    private var upload: Upload? = null // Upload object containging image and meta data
    private var chosenFile: File? = null //chosen file from intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val returnUri: Uri?

        if (requestCode != IntentHelper.FILE_PICK) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        returnUri = data!!.data
        val filePath = DocumentHelper.getPath(this, returnUri)
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return
        chosenFile = File(filePath)

        /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso.with(baseContext)
                .load(chosenFile)
                .placeholder(R.drawable.ic_photo_library_black)
                .fit()
                .into(uploadImage)

    }


    @OnClick(R.id.imageview)
    fun onChooseImage() {
        uploadDesc!!.clearFocus()
        uploadTitle!!.clearFocus()
        IntentHelper.chooseFileIntent(this)
    }

    private fun clearInput() {
        uploadTitle!!.setText("")
        uploadDesc!!.clearFocus()
        uploadDesc!!.setText("")
        uploadTitle!!.clearFocus()
        uploadImage!!.setImageResource(R.drawable.ic_photo_library_black)
    }

    @OnClick(R.id.fab)
    fun uploadImage() {
        /*
      Create the @Upload object
     */
        if (chosenFile == null) return
        createUpload(chosenFile!!)

        /*
      Start upload
     */
        UploadService(this).Execute(upload!!, UiCallback())
    }

    private fun createUpload(image: File) {
        upload = Upload()

        upload!!.image = image
        upload!!.title = uploadTitle!!.text.toString()
        upload!!.description = uploadDesc!!.text.toString()
    }

    private inner class UiCallback : Callback<ImageResponse> {

        override fun success(imageResponse: ImageResponse, response: Response) {
            clearInput()
            val toast = Toast.makeText(applicationContext, imageResponse.data!!.link, Toast.LENGTH_LONG)
            toast.show()
        }

        override fun failure(error: RetrofitError?) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById<View>(R.id.rootView), "No internet connection", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }
}

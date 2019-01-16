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
import android.support.design.widget.FloatingActionButton
import android.view.View
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MainActivity : AppCompatActivity() {

    /*
      These annotations are for ButterKnife by Jake Wharton
      https://github.com/JakeWharton/butterknife
     */
    private var uploadImage: ImageView? = null
    private var uploadTitle: EditText? = null
    private var uploadDesc: EditText? = null
    private var toolbar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private var upload: Upload? = null // Upload object containging image and meta data
    private var chosenFile: File? = null //chosen file from intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        buildViews()
    }

    fun buildViews(){
        val bundle = this.intent.extras

        uploadImage = findViewById(R.id.imageview)
        uploadTitle = findViewById(R.id.editText_upload_title)
        uploadDesc = findViewById(R.id.editText_upload_desc)
        toolbar = findViewById(R.id.toolbar)
        fab = findViewById(R.id.fab)


        fab!!.setOnClickListener(onUploadImage)
        uploadImage!!.setOnClickListener(onChoseImage)
    }

    private val onChoseImage = View.OnClickListener{
        uploadDesc!!.clearFocus()
        uploadTitle!!.clearFocus()
        IntentHelper.chooseFileIntent(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != IntentHelper.FILE_PICK) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        val returnUri: Uri? = data!!.data
        val filePath = DocumentHelper.getPath(this, returnUri!!)
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return
        chosenFile = File(filePath)

        /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso
                .get()
                .load(chosenFile!!)
                .placeholder(R.drawable.ic_photo_library_black)
                .fit()
                .into(uploadImage)

    }

    private fun clearInput() {
        uploadTitle!!.setText("")
        uploadDesc!!.clearFocus()
        uploadDesc!!.setText("")
        uploadTitle!!.clearFocus()
        uploadImage!!.setImageResource(R.drawable.ic_photo_library_black)
    }


    private val onUploadImage = View.OnClickListener {
        /*
        Create the @Upload object
        */
        if (chosenFile != null) {
            createUpload(chosenFile!!)

            /*
            Start upload
            */
            UploadService(this).Execute(upload!!, UiCallback())
        }
    }

    private fun createUpload(image: File) {
        upload = Upload()

        upload!!.image = image
        upload!!.title = uploadTitle!!.text.toString()
        upload!!.description = uploadDesc!!.text.toString()
        //upload!!.albumId = ""
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

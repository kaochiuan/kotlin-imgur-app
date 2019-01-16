package akiniyalocts.imgurapiexample.helpers

import android.app.Activity
import android.content.Intent

/**
 * Created by AKiniyalocts on 2/23/15.
 *
 */
object IntentHelper {
    val FILE_PICK = 1001


    fun chooseFileIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        activity.startActivityForResult(intent, FILE_PICK)
    }
}

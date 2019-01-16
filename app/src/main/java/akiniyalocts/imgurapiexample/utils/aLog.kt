package akiniyalocts.imgurapiexample.utils

import android.util.Log

import akiniyalocts.imgurapiexample.Constants

/**
 * Created by AKiniyalocts on 1/16/15.
 *
 * Basic logger bound to a flag in Constants.java
 */
object aLog {
    fun w(TAG: String?, msg: String?) {
        if (Constants.LOGGING) {
            if (TAG != null && msg != null)
                Log.w(TAG, msg)
        }
    }

}

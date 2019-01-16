package akiniyalocts.imgurapiexample

/**
 * Created by AKiniyalocts on 2/23/15.
 */
object Constants {
    /*
      Logging flag
     */
    val LOGGING = false

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    val MY_IMGUR_CLIENT_ID = ""
    val MY_IMGUR_CLIENT_SECRET = ""

    /*
      Redirect URL for android.
     */
    val MY_IMGUR_REDIRECT_URL = "http://android"

    /*
      Client Auth
     */
    val clientAuth: String
        get() = "Client-ID $MY_IMGUR_CLIENT_ID"

}

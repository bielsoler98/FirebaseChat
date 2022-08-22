package com.senyor_o.firebasechat.utils

object Constants {

    const val EMAIL_METHOD = "email_method"
    const val GOOGLE_METHOD = "google"

    //Collection References
    const val USERS_REF = "users"

    //User fields
    const val DISPLAY_NAME = "displayName"
    const val EMAIL = "email"
    const val PHOTO_URL = "ppf"
    const val CREATED_AT = "createdAt"

    //Names
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    //Empty Value
    const val NO_DISPLAY_NAME = ""

    const val MESSAGES = "messages"
    const val USERS = "users"
    const val SENT_BY = "sent_by"
    const val SENT_DATE = "sentDate"
    const val SENT_ON_IMAGE = "sent_on_image"
    const val IS_CURRENT_USER = "is_current_user"
    const val CONTENT = "content"
    const val CONTENT_TYPE = "content_type"
    const val IMAGE_TYPE = "image_type"
    const val TEXT_TYPE = "text_type"

    //Messages
    const val SIGN_IN_ERROR_MESSAGE = "16: Cannot find a matching credential."
    const val REVOKE_ACCESS_MESSAGE = "You need to re-authenticate before revoking the access."
}

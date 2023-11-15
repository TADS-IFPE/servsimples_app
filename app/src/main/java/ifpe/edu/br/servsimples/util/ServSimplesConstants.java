/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import javax.net.ssl.HttpsURLConnection;

public interface ServSimplesConstants {

    // HTTP constants
    int HTTP_OK = HttpsURLConnection.HTTP_OK;

    String ACTION_EDIT_PROFILE = "servsimples.com.action_edit_profile";
    String ACTION_EDIT_SERVICE = "servsimples.com.action_edit_service";


    int USER_EXISTS = 422;
    int USER_NOT_EXISTS = 416;
    int USER_INFO_NOT_MATCH = 417;
    int USER_INVALID = 403;
    int OK = 200;
    int TOKEN_NOT_PRESENT = 301;
    int TOKEN_DECRYPT_FAILURE = 303;
    int USERNAME_INVALID = 409;
    int PASSWORD_INVALID = 206;
    int TOKEN_EXPIRED = 426;
    int FAILURE = 226;
    int USER_NOT_ALLOWED = 401;
    int SERVICE_INVALID = 425;

    int SERVER_ERROR = 500;

}

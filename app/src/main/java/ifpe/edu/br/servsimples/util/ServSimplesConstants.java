/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;

public interface ServSimplesConstants {

    // HTTP constants
    int HTTP_OK = HttpsURLConnection.HTTP_OK;

    String ACTION_EDIT_PROFILE = "servsimples.com.action_edit_profile";
    String ACTION_EDIT_SERVICE = "servsimples.com.action_edit_service";
    String ACTION_SEARCH_SERVICE = "servsimples.com.action_search_service";
    String ACTION_SHOW_PROFESSIONAL_AVAILABILITY = "servsimples.com.action_search_professional_availability";
    String ACTION_SHOW_AVAILABILITIES = "action-show-availabilities";
    String ACTION_DELETE_AVAILABILITIES = "action-delete-availabilities";
    String ACTION_ADD_AVAILABILITY = "action-add-availability";
    String ACTION_SHOW_APPOINTMENTS = "action-show-appointments";
    String CURRENT_SERVICE_NAME = "current-service-name";
    String CURRENT_SERVICE_DESCRIPTION = "current-service-description";
    String CURRENT_SERVICE_CATEGORY = "current-service-category";
    String CURRENT_SERVICE_COST_VALUE = "current-service-coast-value";
    String CURRENT_SERVICE_COST_TIME = "current-service-coast-time";
    String CURRENT_SERVICE_ID = "current-service-id";
    String USER_CPF = "user-cpf-12ewdv";
    String USER_ID = "user-id-12ewdv";
    String SERVICE_ID = "service_id343";
    String SERVICE_CATEGORY = "service_category343";
    String SERVICE_DESCRIPTION = "service_description23";
    String SERVICE_COST_VALUE = "service_cost_value324";
    String SERVICE_COST_TIME = "service_service_cost_timewerwerw";
    String SERVICE_NAME = "service_name_dfjskf";

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

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import android.content.Context;
import android.content.SharedPreferences;

import ifpe.edu.br.servsimples.model.User;

public class PersistHelper {

    private static final String TAG = PersistHelper.class.getSimpleName();
    private static final String SERVSIMPLES_PREFERENCES = "serversimples-prefernces";
    private static final String CPF = "user-cpf";
    private static final String TOKEN = "user-token";
    private static final String STATUS = "user-session-status";
    private static final String NAME = "user-name";
    private static final String USER_TYPE = "user-type";
    private static final String SESSION_FLAG = "session-flag";
    private static final int USER_TYPE_ADMIN = 1;
    private static final int USER_TYPE_PROFESSIONAL = 2;
    private static final int USER_TYPE_USER = 3;


    public static void saveUserInfo(User user, Context context) {
        if (user == null || context == null) {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "'user' or 'context' is null");
            return;
        }
        if (ServSimplesAppLogger.ISLOGABLE) {
            ServSimplesAppLogger.d(TAG, "saveUserInfo:");
            ServSimplesAppLogger.d(TAG, "user info: cpf:" + user.getCpf() + " name:"
                    + user.getName() + " token:" + user.getToken() + " username:"
                    + user.getUserName() + " userType:" + user.getUserType());
        }
        SharedPreferences sp =
                context.getSharedPreferences(SERVSIMPLES_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CPF, user.getCpf());
        editor.putString(NAME, user.getName());
        editor.putString(TOKEN, user.getToken());
        editor.putInt(USER_TYPE, user.getUserType() == User.UserType.USER ? USER_TYPE_USER :
                user.getUserType() == User.UserType.PROFESSIONAL ? USER_TYPE_PROFESSIONAL : USER_TYPE_ADMIN);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(SERVSIMPLES_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(SESSION_FLAG, false);
    }

    public static void setUserLogged(Context context, boolean isLoggedIn) {
        if (context == null) {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "'context' is null");
            return;
        }
        SharedPreferences sp =
                context.getSharedPreferences(SERVSIMPLES_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SESSION_FLAG, isLoggedIn);
        editor.apply();
    }

    public static User getUser(Context context) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "getUserInfo:");
        SharedPreferences sp =
                context.getSharedPreferences(SERVSIMPLES_PREFERENCES, Context.MODE_PRIVATE);
        String cpf = sp.getString(CPF, "");
        String token = sp.getString(TOKEN, "");
        String name = sp.getString(NAME, "");
        int userType = sp.getInt(USER_TYPE, -1);

        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "info: cpf:" + cpf + " token:" + token +" name:" + name + " userType:" + userType);

        User user = new User();
        user.setToken(token);
        user.setCpf(cpf);
        user.setName(name);
        user.setUserType(userType == USER_TYPE_ADMIN ? User.UserType.ADMIN
                : userType == USER_TYPE_PROFESSIONAL ? User.UserType.PROFESSIONAL : User.UserType.USER);
        return user;
    }
}

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ServSimplesAppLogger {

    private static final String TAG = "ServSimplesApp";
    public static final boolean ISLOGABLE = true;

    public static void d(String subtag, String message) {
        Log.d(TAG, " [" + subtag + "]: " + message);
    }

    public static void d(String subtag, Thread thread, String message) {
        Log.d(TAG, " [" + subtag + "] " + " [" + thread.getName() + "] " + message);
    }

    public static void e(String subtag, String message) {
        Log.e(TAG, " [" + subtag + "]: " + message);
    }

    public static void w(String subtag, String message) {
        Log.w(TAG, " [" + subtag + "]: " + message);
    }

    public static OkHttpClient.Builder getLoggerClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
        return httpClient;
    }
}

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionManager {

    private static final String SERVSIMPLES_SERVER_DOMAIN = "http://100.67.69.162:8080";
    private static ConnectionManager instance;
    private static Retrofit mServSimplesServerConnection;

    private ConnectionManager() {
        Gson gson = new GsonBuilder().create();
        mServSimplesServerConnection = new Retrofit.Builder()
                .baseUrl(SERVSIMPLES_SERVER_DOMAIN)
                .client(ServSimplesAppLogger.getLoggerClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Retrofit getServSimplesConnection() {
        return mServSimplesServerConnection;
    }
}
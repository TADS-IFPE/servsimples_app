/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.managers;

import ifpe.edu.br.servsimples.model.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public abstract class ServicesInterfaceWrapper {

    interface UserServices {
        @POST("api/register/user")
        Call<User> registerUser(@Body RequestBody requestBody);

        @POST("api/login")
        Call<User> loginUser(@Body RequestBody requestBody);

        @POST("api/get/user")
        Call<User> getUser(@Body RequestBody requestBody);

        @POST("/api/update/user")
        Call<User> updateUser(@Body RequestBody requestBody);

        @POST("/api/unregister/user")
        Call<User> removeUser(@Body RequestBody requestBody);
    }

    public interface IServSimplesServerManager {
        void registerUser(User user, RegistrationCallback callback);
        void loginUser(User user, RegistrationCallback callback);
        void getUser(User user, RegistrationCallback callback);
        void unregisterUser(User user, RegistrationCallback callback);
        void updateUser(User user, RegistrationCallback callback);
    }

    public interface RegistrationCallback {
        void onSuccess(User user);

        void onFailure(String message);
    }

}
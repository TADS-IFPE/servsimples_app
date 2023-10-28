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

    }

    public interface IServSimplesServerManager {
        void registerUser(User user, RegistrationCallback callback);

        void unregisterUser(User user, RegistrationCallback callback);
    }

    public interface RegistrationCallback {
        void onSuccess(User device);

        void onFailure(String message);
    }

}
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.managers;

import java.util.List;

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

        @POST("api/update/user")
        Call<User> updateUser(@Body RequestBody requestBody);

        @POST("api/unregister/user")
        Call<User> removeUser(@Body RequestBody requestBody);
    }

    interface ServiceServices {
        @POST("api/register/service")
        Call<User> registerService(@Body RequestBody requestBody);

        @POST("api/get/service")
        Call<User> getService(@Body RequestBody requestBody);

        @POST("api/get/service/categories")
        Call<List<String>> getServiceCategories(@Body RequestBody requestBody);

        @POST("api/update/service")
        Call<User> updateService(@Body RequestBody requestBody);

        @POST("api/unregister/service")
        Call<User> unregisterService(@Body RequestBody requestBody);
    }

    interface EventServices {

    }
}
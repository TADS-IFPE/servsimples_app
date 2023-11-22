/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.managers;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerManager implements IServerManagerInterfaceWrapper.IServerUserManager,
        IServerManagerInterfaceWrapper.IServerServiceManager {

    private static final String TAG = ServerManager.class.getSimpleName();
    private static ServerManager instance;
    private final ConnectionManager mConnectionManager;

    private ServerManager() {
        mConnectionManager = ConnectionManager.getInstance();
    }

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    @Override
    public void registerUser(User user,
                             IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "registerUser");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.UserServices.class)
                .registerUser(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "registerUser not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "registerUser: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void loginUser(User user, IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "loginUser");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.UserServices.class)
                .loginUser(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "loginUser not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "loginUser: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void getUser(User user, IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.UserServices.class)
                .getUser(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "getUser not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "getUser: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void unregisterUser(User user, IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "unregisterUser");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.UserServices.class)
                .removeUser(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "unregisterUser not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "unregisterUser: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void updateUser(User user, IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "updateUser");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.UserServices.class)
                .updateUser(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "updateUser not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "updateUser: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void registerService(User user, IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "registerService");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.ServiceServices.class)
                .registerService(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "registerService not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "registerService: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void updateService(User user,
                              IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "updateService");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.ServiceServices.class)
                .updateService(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "updateService not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "updateService: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void unregisterService(User user,
                                  IServerManagerInterfaceWrapper.ServerRequestCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "unregisterService");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.ServiceServices.class)
                .unregisterService(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "unregisterService not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "unregisterService: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void getServiceCategories(User user,
                                     IServerManagerInterfaceWrapper.ServerCategoriesCallback callback) {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "getServiceCategories");
        mConnectionManager
                .getServSimplesConnection().create(ServicesInterfaceWrapper.ServiceServices.class)
                .getServiceCategories(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(user)))
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call,
                                           @NonNull Response<List<String>> resp) {
                        if (resp.isSuccessful() && resp.code() == ServSimplesConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (ServSimplesAppLogger.ISLOGABLE)
                                ServSimplesAppLogger.w(TAG, "getServiceCategories not ok: status:"
                                        + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call,
                                          @NonNull Throwable t) {
                        if (ServSimplesAppLogger.ISLOGABLE)
                            ServSimplesAppLogger.e(TAG, "getServiceCategories: onFailure:"
                                    + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }
}
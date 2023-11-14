package ifpe.edu.br.servsimples.managers;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */

import java.util.List;

import ifpe.edu.br.servsimples.model.User;

public class IServerManagerInterfaceWrapper {

    public interface IServerUserManager {
        void registerUser(User user,
                          ServerRequestCallback callback);

        void loginUser(User user,
                       ServerRequestCallback callback);

        void getUser(User user,
                     ServerRequestCallback callback);

        void unregisterUser(User user,
                            ServerRequestCallback callback);

        void updateUser(User user,
                        ServerRequestCallback callback);

    }

    public interface IServerServiceManager {
        void registerService(User user,
                             ServerRequestCallback registrationCallback);

        void getServiceCategories(User user,
                                  ServerCategoriesCallback categoriesCallback);
    }

    public interface ServerCategoriesCallback {
        void onSuccess(List<String> categories);

        void onFailure(String message);
    }

    public interface ServerRequestCallback {
        void onSuccess(User user);

        void onFailure(String message);
    }
}
package ifpe.edu.br.servsimples.managers;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */

import ifpe.edu.br.servsimples.model.User;

public class IServerManagerInterfaceWrapper {

    public interface IServerUserManager {
        void registerUser(User user,
                          serverRequestCallback callback);

        void loginUser(User user,
                       serverRequestCallback callback);

        void getUser(User user,
                     serverRequestCallback callback);

        void unregisterUser(User user,
                            serverRequestCallback callback);

        void updateUser(User user,
                        serverRequestCallback callback);

    }

    public interface IServerServiceManager {
        void registerService(User user,
                             serverRequestCallback registrationCallback);
    }

    public interface serverRequestCallback {
        void onSuccess(User user);

        void onFailure(String message);
    }
}
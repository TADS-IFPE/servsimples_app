package ifpe.edu.br.servsimples.managers;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */

import java.util.List;

import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;

public class IServerManagerInterfaceWrapper {

    public interface IUserManager {
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

        void getProfessionalUserFromService(User user,
                                            ServerRequestCallback callback);
    }

    public interface IServiceManager {
        void registerService(User user,
                             ServerRequestCallback registrationCallback);

        void getServiceCategories(User user,
                                  ServerCategoriesCallback categoriesCallback);

        void updateService(User user, ServerRequestCallback serviceCallback);

        void unregisterService(User user, ServerRequestCallback serviceCallback);

        void getServicesByCategory(User currentUser,
                                   IServerManagerInterfaceWrapper.ServerServicesCallback callback);
    }

    public interface IAvailabilityManager {
        void registerAvailability(User user,
                                  IServerManagerInterfaceWrapper.RegisterAvailabilityCallback callback);
        void deleteAvailability(User user,
                                  IServerManagerInterfaceWrapper.RegisterAvailabilityCallback callback);
    }

    public interface ServerCategoriesCallback {
        void onSuccess(List<String> categories);

        void onFailure(String message);
    }

    public interface RegisterAvailabilityCallback {
        void onSuccess(int response);

        void onFailure();
    }

    public interface ServerRequestCallback {
        void onSuccess(User user);

        void onFailure(String message);
    }

    public interface ServerServicesCallback {
        void onSuccess(List<Service> services);

        void onFailure(String message);
    }
}
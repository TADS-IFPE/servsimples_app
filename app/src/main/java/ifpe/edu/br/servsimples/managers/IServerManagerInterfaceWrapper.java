package ifpe.edu.br.servsimples.managers;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */

import java.util.List;

import ifpe.edu.br.servsimples.model.Availability;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.agenda.AppointmentFragment;
import ifpe.edu.br.servsimples.ui.agenda.AppointmentWrapper;

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

        void getAvailabilitiesForProfessional(AppointmentWrapper appointmentWrapper,
                                              IServerManagerInterfaceWrapper.AvailabilityCallback callback);
    }

    public interface IAppointmentManager {
        void registerAppointment(AppointmentWrapper appointmentWrapper, AppointmentCallback callback);
    }

    public interface AppointmentCallback {
        void onSuccess();
        void onFailure();
    }

    public interface ServerCategoriesCallback {
        void onSuccess(List<String> categories);

        void onFailure(String message);
    }

    public interface RegisterAvailabilityCallback {
        void onSuccess(int response);

        void onFailure();
    }

    public interface AvailabilityCallback {
        void onSuccess(List<Availability> availabilities);

        void onFailure(String message);
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
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.LoginActivity;
import ifpe.edu.br.servsimples.ui.RegisterActivity;
import ifpe.edu.br.servsimples.ui.agenda.AgendaHolderActivity;
import ifpe.edu.br.servsimples.ui.services.MyServicesDropDownAdapter;
import ifpe.edu.br.servsimples.ui.services.ServicesHolderActivity;
import ifpe.edu.br.servsimples.util.DialogUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;
import ifpe.edu.br.servsimples.util.ServerResponseCodeParser;


public class ProfileFragment extends Fragment {


    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final int GET_USER_OK = 0;
    private static final int GET_USER_NOT_OK = 1;
    private TextView mLogout;
    private TextView mEditProfile;
    private TextView mDeleteUser;
    private TextView mTvUserName;
    private TextView mTvUserBio;
    private User mcurrentUser;

    //Services
    private TextView mTvServiceName;
    private TextView mTvServiceDescription;
    private TextView mTvServiceCategory;
    private TextView mTvServiceValue;
    private TextView mTvServiceTime;
    private TextView mTvCreateService;
    private TextView mTvEditService;
    private TextView mTvDeleteService;
    private Spinner mSpServices;
    private CardView mServicesCard;
    private CardView mServicesSettingsCard;
    private Service mCurrentService;

    // Agenda
    private TextView mTvAddAvailability;
    private TextView mTvShowAvailabilities;
    private CardView mAgendaSettingsCard;

    // Appointment
    private TextView mTvShowAppointment;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_USER_OK -> {
                    mTvUserName.setText(mcurrentUser.getName());
                    mTvUserBio.setText(mcurrentUser.getBio());
                    List<Service> services = mcurrentUser.getServices();
                    if (services.isEmpty()) {
                        enableDisableServicesFields(View.GONE);
                    } else {
                        enableDisableServicesFields(View.VISIBLE);
                        setNonProfessionalFieldsVisibility(isProfessionalUser() ?
                                View.VISIBLE : View.GONE);
                        MyServicesDropDownAdapter servicesAdapter =
                                new MyServicesDropDownAdapter(getContext(), services);
                        mSpServices.setAdapter(servicesAdapter);
                        mSpServices.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int position, long id) {
                                        mCurrentService = services.get(position);
                                        mTvServiceName.setText(mCurrentService.getName());
                                        mTvServiceDescription.setText(mCurrentService.getDescription());
                                        mTvServiceCategory.setText(mCurrentService.getCategory());
                                        mTvServiceValue.setText(mCurrentService.getCost().getValue());
                                        mTvServiceTime.setText(mCurrentService.getCost().getTime());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                    }
                }
                case GET_USER_NOT_OK -> performLogOut();
            }
        }
    };

    private void enableDisableServicesFields(int visible) {
        mServicesCard.setVisibility(visible);
        mTvEditService.setVisibility(visible);
        mTvDeleteService.setVisibility(visible);
    }

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewsById(view);
        setUpListeners();
        return view;
    }

    private void setNonProfessionalFieldsVisibility(int visibility) {
        mServicesCard.setVisibility(visibility);
        mServicesSettingsCard.setVisibility(visibility);
        mAgendaSettingsCard.setVisibility(visibility);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "onResume");
        setNonProfessionalFieldsVisibility(isProfessionalUser() ? View.VISIBLE : View.GONE);
        retrieveUserProfileInfo();
    }

    private boolean isProfessionalUser() {
        return User.UserType.PROFESSIONAL ==
                PersistHelper.getCurrentUser(getContext()).getUserType();
    }

    private void retrieveUserProfileInfo() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "retrieveUserProfileInfo");

        new Thread(() -> ServerManager.getsInstance()
                .getUser(PersistHelper.getCurrentUser(getContext()),
                        new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (user == null) {
                                    mHandler.sendEmptyMessage(GET_USER_NOT_OK);
                                    return;
                                }
                                mcurrentUser = user;
                                mHandler.sendEmptyMessage(GET_USER_OK);
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(getContext(),
                                        ServerResponseCodeParser.parseToString(message),
                                        Toast.LENGTH_SHORT).show();
                                mHandler.sendEmptyMessage(GET_USER_NOT_OK);
                            }
                        })).start();
    }

    private void setUpListeners() {
        mLogout.setOnClickListener(view -> logout());
        mDeleteUser.setOnClickListener(View -> deleteUser());
        mTvDeleteService.setOnClickListener(View -> deleteService());
        mEditProfile.setOnClickListener(view -> editProfile());
        mTvCreateService.setOnClickListener(View -> createService());
        mTvEditService.setOnClickListener(View -> editService());
        mTvAddAvailability.setOnClickListener(v -> addAvailability());
        mTvShowAvailabilities.setOnClickListener(View -> showAvailabilities());
        mTvShowAppointment.setOnClickListener(View -> showAppointments());
    }

    private void showAppointments() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "showAppointments");
        Intent intent = new Intent(getContext(), AgendaHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_SHOW_APPOINTMENTS);
        startActivity(intent);
    }

    private void deleteAvailability() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "deleteAvailability");
        Intent intent = new Intent(getContext(), AgendaHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_DELETE_AVAILABILITIES);
        startActivity(intent);
    }

    private void showAvailabilities() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "showAvailabilities");
        Intent intent = new Intent(getContext(), AgendaHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_SHOW_AVAILABILITIES);
        startActivity(intent);
    }

    private void logout() {
        DialogUtils.showDialog(
                getContext(),
                "Sair",
                "Deseja realmente sair da aplicação?",
                new DialogUtils.IDialogYesNoCallback() {
                    @Override
                    public void onOk() {
                        performLogOut();
                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void deleteUser() {
        DialogUtils.showDialog(
                getContext(),
                "Excluir conta",
                PersistHelper.getCurrentUser(getContext()).getName() +
                        ", deseja realmente excluir sua conta? Seus dados não poderão ser recuperados",
                new DialogUtils.IDialogYesNoCallback() {
                    @Override
                    public void onOk() {
                        performDeleteUser();
                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void deleteService() {
        DialogUtils.showDialog(
                getContext(),
                "Excluir Serviço",
                PersistHelper.getCurrentUser(getContext()).getName() +
                        ", deseja realmente excluir " + mCurrentService.getName() + " ?",
                new DialogUtils.IDialogYesNoCallback() {
                    @Override
                    public void onOk() {
                        performDeleteService();
                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void addAvailability() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "addAvailability");
        Intent intent = new Intent(getContext(), AgendaHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_ADD_AVAILABILITY);
        startActivity(intent);
    }

    private void performDeleteService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "deleteService");
        User currentUser = PersistHelper.getCurrentUser(getContext());
        currentUser.addService(mCurrentService);
        ServerManager.getsInstance()
                .unregisterService(currentUser,
                        new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (user == null) {
                                    ServSimplesAppLogger.e(TAG, "'user' is null");
                                    return;
                                }
                                if (ServSimplesAppLogger.ISLOGABLE)
                                    Toast.makeText(getContext(), "Serviço removido com sucesso",
                                            Toast.LENGTH_SHORT).show();
                                retrieveUserProfileInfo();
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(getContext(),
                                        ServerResponseCodeParser.parseToString(message),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void editService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "editService");
        Intent intent = new Intent(getActivity(), ServicesHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_EDIT_SERVICE);
        startActivity(getFilledIntentWithCurrentServiceInfo(intent));
    }

    private Intent getFilledIntentWithCurrentServiceInfo(Intent intent) {
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_ID, mCurrentService.getId());
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_NAME, mCurrentService.getName());
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_DESCRIPTION, mCurrentService.getDescription());
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_CATEGORY, mCurrentService.getCategory());
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_COST_VALUE, mCurrentService.getCost().getValue());
        intent.putExtra(ServSimplesConstants.CURRENT_SERVICE_COST_TIME, mCurrentService.getCost().getTime());
        return intent;
    }

    private void createService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "createService");
        startActivity(new Intent(getActivity(), ServicesHolderActivity.class));
    }

    private void performDeleteUser() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "deleteProfile");
        ServerManager.getsInstance()
                .unregisterUser(PersistHelper.getCurrentUser(getContext()),
                        new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                            @Override
                            public void onSuccess(User user) {
                                PersistHelper.saveUserInfo(new User(), getContext());
                                PersistHelper.setUserLogged(getContext(), false);
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                requireActivity().finish();
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(getContext(),
                                        ServerResponseCodeParser.parseToString(message),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void editProfile() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "editProfile");
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_EDIT_PROFILE);
        startActivity(intent);
    }

    private void performLogOut() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "logOut");
        PersistHelper.saveUserInfo(new User(), getContext());
        PersistHelper.setUserLogged(getContext(), false);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    private void findViewsById(View view) {
        mLogout = view.findViewById(R.id.tv_profile_miscellaneous_logout);
        mEditProfile = view.findViewById(R.id.tv_profile_miscellaneous_edit_profile);
        mDeleteUser = view.findViewById(R.id.tv_profile_miscellaneous_delete_profile);
        mTvUserName = view.findViewById(R.id.servicedetail_professionalinfo_tv_name);
        mTvUserBio = view.findViewById(R.id.servicedetail_professionalinfo_tv_bio);

        //Services
        mTvServiceName = view.findViewById(R.id.servicedetail_infoservice_tv_name);
        mTvServiceDescription = view.findViewById(R.id.servicedetail_infoservice_tv_description);
        mTvServiceCategory = view.findViewById(R.id.servicedetail_infoservice_tv_category);
        mTvServiceValue = view.findViewById(R.id.servicedetail_infoservice_tv_value);
        mTvServiceTime = view.findViewById(R.id.servicedetail_infoservice_tv_time);
        mSpServices = view.findViewById(R.id.sp_services);
        mServicesCard = view.findViewById(R.id.card_services_info);
        mServicesSettingsCard = view.findViewById(R.id.card_services_setting);
        mTvCreateService = view.findViewById(R.id.tv_profile_services_settings_create_service);
        mTvEditService = view.findViewById(R.id.tv_profile_services_settings_edit_service);
        mTvDeleteService = view.findViewById(R.id.tv_profile_services_settings_delete_service);

        //Agenda
        mTvAddAvailability = view.findViewById(R.id.tv_profile_agenda_settings_create_availability);
        mTvShowAvailabilities = view.findViewById(R.id.tv_profile_agenda_settings_show_availabilities);
        mAgendaSettingsCard = view.findViewById(R.id.card_agenda_setting);

        // Appointment
        mTvShowAppointment = view.findViewById(R.id.tv_profile_agenda_settings_show_appointments);
    }
}
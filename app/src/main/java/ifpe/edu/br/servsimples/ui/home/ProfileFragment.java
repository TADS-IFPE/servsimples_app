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
import ifpe.edu.br.servsimples.ui.services.MyServicesDropDownAdapter;
import ifpe.edu.br.servsimples.ui.services.ServicesHolderActivity;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;


public class ProfileFragment extends Fragment {


    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final int GET_USER_OK = 0;
    private static final int GET_USER_NOT_OK = 1;
    private TextView mLogout;
    private TextView mEditProfile;
    private TextView mDeleteUser;
    private TextView mTvUserName;
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

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_USER_OK:
                    mTvUserName.setText(mcurrentUser.getName());
                    // TODO colocar aqui o texto da bio do usuario
                    List<Service> services = mcurrentUser.getServices();
                    if (services.isEmpty()) {
                        Toast.makeText(getContext(), "O usuário ainda não oferece serviços",
                                Toast.LENGTH_SHORT).show();
                        mServicesCard.setVisibility(View.GONE);
                    } else {
                        MyServicesDropDownAdapter servicesAdapter =
                                new MyServicesDropDownAdapter(getContext(), services);
                        mSpServices.setAdapter(servicesAdapter);
                        mSpServices.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int position, long id) {
                                        Service service = services.get(position);
                                        mTvServiceName.setText(service.getName());
                                        mTvServiceDescription.setText(service.getDescription());
                                        mTvServiceCategory.setText(service.getCategory());
                                        mTvServiceValue.setText(service.getCost().getValue());
                                        mTvServiceTime.setText(service.getCost().getTime());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                    }
                    break;
                case GET_USER_NOT_OK:
                    Toast.makeText(getContext(), "Não foi possível recuperar informações do usuário",
                            Toast.LENGTH_SHORT).show();
                    logOut();
                    break;
            }
        }
    };

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

    @Override
    public void onResume() {
        super.onResume();
        retrieveUserProfileInfo();
    }

    private void retrieveUserProfileInfo() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "retrieveUserProfileInfo");

        new Thread(() -> ServerManager.getInstance()
                .getUser(PersistHelper.getUser(getContext()),
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
                                mHandler.sendEmptyMessage(GET_USER_NOT_OK);
                            }
                        })).start();
    }

    private void setUpListeners() {
        mLogout.setOnClickListener(view -> logOut());
        mEditProfile.setOnClickListener(view -> editProfile());
        mDeleteUser.setOnClickListener(View -> deleteProfile());
        mTvCreateService.setOnClickListener(View -> createService());
        mTvEditService.setOnClickListener(View -> editService());
        mTvDeleteService.setOnClickListener(View -> deleteService());
    }

    private void deleteService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "deleteService");
    }

    private void editService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "editService");
    }

    private void createService() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "createService");
        startActivity(new Intent(getActivity(), ServicesHolderActivity.class));
    }

    private void deleteProfile() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "deleteProfile");
        ServerManager.getInstance()
                .unregisterUser(PersistHelper.getUser(getContext()),
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
                                Toast.makeText(getContext(), "Não foi possível deletar o usuário",
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

    private void logOut() {
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
        mTvUserName = view.findViewById(R.id.tv_profile_name);

        //Services
        mTvServiceName = view.findViewById(R.id.tv_profile_services_name);
        mTvServiceDescription = view.findViewById(R.id.tv_profile_services_description);
        mTvServiceCategory = view.findViewById(R.id.tv_profile_services_category);
        mTvServiceValue = view.findViewById(R.id.tv_profile_services_value);
        mTvServiceTime = view.findViewById(R.id.tv_profile_services_time);
        mSpServices = view.findViewById(R.id.sp_services);
        mServicesCard = view.findViewById(R.id.card_services_info);
        mTvCreateService = view.findViewById(R.id.tv_profile_services_settings_create_service);
        mTvEditService = view.findViewById(R.id.tv_profile_services_settings_edit_service);
        mTvDeleteService = view.findViewById(R.id.tv_profile_services_settings_delete_service);
    }
}
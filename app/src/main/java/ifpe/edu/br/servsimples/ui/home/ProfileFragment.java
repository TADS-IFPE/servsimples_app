/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServSimplesServerManager;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.LoginActivity;
import ifpe.edu.br.servsimples.ui.RegisterActivity;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;


public class ProfileFragment extends Fragment {


    private TextView mLogout;
    private TextView mEditProfile;
    private TextView mDeleteUser;
    private TextView mTvUserName;

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
        ServSimplesServerManager.getInstance().getUser(PersistHelper.getUser(getContext()),
                new IServerManagerInterfaceWrapper.serverRequestCallback() {
                    @Override
                    public void onSuccess(User user) {
                        if (user == null) {
                            Toast.makeText(getContext(), "Não foi possível recuperar informações do usuário",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mTvUserName.setText(user.getName());
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private void setUpListeners() {
        mLogout.setOnClickListener(view -> logOut());
        mEditProfile.setOnClickListener(view -> editProfile());
        mDeleteUser.setOnClickListener(View -> deleteProfile());
    }

    private void deleteProfile() {
        ServSimplesServerManager.getInstance()
                .unregisterUser(PersistHelper.getUser(getContext()),
                        new IServerManagerInterfaceWrapper.serverRequestCallback() {
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
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_EDIT_PROFILE);
        startActivity(intent);
    }

    private void logOut() {
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
    }
}
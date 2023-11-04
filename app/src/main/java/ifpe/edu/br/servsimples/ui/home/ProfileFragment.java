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

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.LoginActivity;
import ifpe.edu.br.servsimples.ui.RegisterActivity;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;


public class ProfileFragment extends Fragment {


    private TextView mLogout;
    private TextView mEditProfile;

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

    private void setUpListeners() {
        mLogout.setOnClickListener(view -> logOut());
        mEditProfile.setOnClickListener(view -> editProfile());
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
    }
}
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ifpe.edu.br.servsimples.R;


public class ServicesFragment extends Fragment {


    public ServicesFragment() {
    }

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        findViewsById(view);
        setUpListeners();
        return view;
    }

    private void setUpListeners() {

    }

    private void findViewsById(View view) {

    }
}
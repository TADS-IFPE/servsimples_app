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
import android.widget.Button;

import androidx.fragment.app.Fragment;

import ifpe.edu.br.servsimples.R;


public class ServicesFragment extends Fragment {

    private Button mServiceAdd;

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
        mServiceAdd.setOnClickListener(view -> {
            performAddService();
        });
    }

    private void performAddService() {
        //startActivity(new Intent(getActivity(), ServicesHolderActivity.class));
    }

    private void findViewsById(View view) {
        mServiceAdd = view.findViewById(R.id.bt_service_add);
    }
}
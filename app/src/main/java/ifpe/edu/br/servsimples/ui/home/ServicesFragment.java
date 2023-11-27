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

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.ui.services.ServicesHolderActivity;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;


public class ServicesFragment extends Fragment {

    private TextView mTvSearchService;

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

    private void searchService() {
        Intent intent = new Intent(getActivity(), ServicesHolderActivity.class);
        intent.setAction(ServSimplesConstants.ACTION_SEARCH_SERVICE);
        startActivity(intent);
    }
    private void setUpListeners() {
        mTvSearchService.setOnClickListener(v -> searchService());
    }


    private void findViewsById(View view) {
        mTvSearchService = view.findViewById(R.id.tv_service_search);
    }
}
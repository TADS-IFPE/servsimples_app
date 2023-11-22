/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.model.Cost;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;

public class ServicesHolderActivity extends AppCompatActivity
        implements UIInterfaceWrapper.FragmentUtil {

    private static final String TAG = ServicesHolderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_holder);
        String action = getIntent().getAction();
        if (ServSimplesConstants.ACTION_EDIT_SERVICE.equals(action)) {
            Service currentService = getCurrentServiceFromIntent(getIntent());
            Fragment editService = AddServiceFragment.newInstance(currentService);
            openFragment(editService, false);
        } else {
            Fragment addService = AddServiceFragment.newInstance();
            openFragment(addService, false);
        }
    }

    private Service getCurrentServiceFromIntent(Intent intent) {
        String name = intent.getStringExtra(ServSimplesConstants.CURRENT_SERVICE_NAME);
        String category = intent.getStringExtra(ServSimplesConstants.CURRENT_SERVICE_CATEGORY);
        String description = intent.getStringExtra(ServSimplesConstants.CURRENT_SERVICE_DESCRIPTION);
        String costTime = intent.getStringExtra(ServSimplesConstants.CURRENT_SERVICE_COST_TIME);
        String costValue = intent.getStringExtra(ServSimplesConstants.CURRENT_SERVICE_COST_VALUE);
        Long id = intent.getLongExtra(ServSimplesConstants.CURRENT_SERVICE_ID, -1);

        Cost cost = new Cost();
        cost.setValue(costValue);
        cost.setTime(costTime);

        Service service = new Service();
        service.setId(id);
        service.setName(name);
        service.setCategory(category);
        service.setDescription(description);
        service.setCost(cost);

        return service;
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.services_fragment_holder, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (NullPointerException e) {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.e(TAG, "Fail when getting fragment manager");
        }
    }
}
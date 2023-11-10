/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.ServSimplesServerManagerImpl;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class HomeHolderActivity extends AppCompatActivity
        implements UIInterfaceWrapper.FragmentUtil {

    private static final String TAG = HomeHolderActivity.class.getSimpleName();
    private BottomNavigationView mNavigationView;
    private int lastFragmentOpened = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_holder);
        setUpBottomNavigationView();
        findViews();
        setListeners();
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            //if (action != null /* &&
            //        action.equals(ServSimplesConstants.ACTION_LAUNCH_MY_DEVICES)*/) {
            //    mNavigationView.setSelectedItemId(R.id.m2);
            //} else {
                openFragment(HomeFragment.newInstance(), false);
                lastFragmentOpened = 1;
            //}
        }
    }

    private void setListeners() {

    }

    private void findViews() {

    }

    private void setUpBottomNavigationView() {
        mNavigationView = findViewById(R.id.main_bottom_nav);
        mNavigationView.setOnItemSelectedListener(item -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                if (ServSimplesAppLogger.ISLOGABLE)
                    ServSimplesAppLogger.e(TAG, "setUpBottomNavigationView fail: ActionBar is null");
                return false;
            }
            int itemId = item.getItemId();
            if (itemId == R.id.m1) {
                if (lastFragmentOpened != 1) {
                    actionBar.setTitle("Início");
                    Fragment homeFragment = HomeFragment.newInstance();
                    openFragment(homeFragment, false);
                    lastFragmentOpened = 1;
                }
            } else if (itemId == R.id.m2) {
                if (lastFragmentOpened != 2) {
                    actionBar.setTitle("Serviços");
                    Fragment myDevicesFragment = ServicesFragment.newInstance();
                    openFragment(myDevicesFragment, false);
                    lastFragmentOpened = 2;
                }
            } else if (itemId == R.id.m3) {
                if (lastFragmentOpened != 3) {
                    actionBar.setTitle("Perfil");
                    Fragment profile = ProfileFragment.newInstance();
                    openFragment(profile, false);
                    lastFragmentOpened = 3;
                }
            }
            return true;
        });
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_holder, fragment);
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
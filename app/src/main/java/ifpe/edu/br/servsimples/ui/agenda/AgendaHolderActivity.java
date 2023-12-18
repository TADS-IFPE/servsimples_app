/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.agenda;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class AgendaHolderActivity extends AppCompatActivity
        implements UIInterfaceWrapper.FragmentUtil {

    private final String TAG = AgendaHolderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_holder);
        setTitle("Agenda");
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "onCreate");

        Bundle bundle = new Bundle();
        bundle.putInt(RegisterAvailabilityFragment.KEY_REGISTER_STATE_CODE, RegisterAvailabilityFragment.PICK_DATE);

        Fragment registerAvailabilityFragment = RegisterAvailabilityFragment.newInstance(bundle);
        openFragment(registerAvailabilityFragment, false);
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.agenda_fragment_holder, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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
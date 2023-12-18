/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui;

import androidx.fragment.app.Fragment;

public interface UIInterfaceWrapper {
    interface FragmentUtil {
        void openFragment(Fragment fragment, boolean addToBackStack);
    }

    interface INavigate {
        void setBackPress(boolean canBackPress);
    }
}
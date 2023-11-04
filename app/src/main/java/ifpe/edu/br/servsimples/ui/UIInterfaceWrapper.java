/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui;

import androidx.fragment.app.Fragment;

public interface UIInterfaceWrapper {
    public interface FragmentUtil {
        void openFragment(Fragment fragment, boolean addToBackStack);
    }

    public interface INavigate {
        void setBackPress(boolean canBackPress);
    }
}

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import android.app.AlertDialog;
import android.content.Context;

public class DialogUtils {


    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  DialogUtilsCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sim", (dialogInterface, i) -> callback.onYes())
                .setNegativeButton("Não", (dialogInterface, i) -> callback.onNo());
        builder.create().show();
    }

    public interface DialogUtilsCallback {
        void onYes();
        void onNo();
    }
}
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
    }

    public static void showErrorDialogDialog(Context context,
                                             String message, IDialogOkCallback callback) {
        if (context == null || message == null || callback == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Erro");
        builder.setMessage(message);

        builder.setPositiveButton("OK", (dialog, which) -> callback.onOk());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  IDialogYesNoCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sim", (dialogInterface, i) -> callback.onOk())
                .setNegativeButton("Não", (dialogInterface, i) -> callback.onNo());
        builder.create().show();
    }

    public interface IDialogYesNoCallback extends IDialogOkCallback {
        void onNo();
    }

    public interface IDialogOkCallback {
        void onOk();
    }
}
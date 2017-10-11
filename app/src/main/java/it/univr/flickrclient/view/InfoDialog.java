package it.univr.flickrclient.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import it.univr.flickrclient.R;

/**
 * Progetto Flickr Client
 * Verifica Automatica di Sistemi
 * AA 2017/2018
 *
 * Created by:
 *  - Matteo Castagnaro - VR407832
 *  - Francesco Marretta - VR412875
 */

/**
 * Classe che estende AlertDialog per la creazione del dialog di informazioni
 */
public class InfoDialog extends AlertDialog {

    public final AlertDialog alert;

    /**
     * Costruttore della classe che permette la creazione del dialog
     *
     * @param activity Activity dove viene creato il dialog
     */
    public InfoDialog(Activity activity) {
        super(activity);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Matteo Castagnaro - VR407832\nFrancesco Marretta - VR412875\n\nv1.0\nSettembre 2017");
        builder.setTitle(activity.getResources().getString(R.string.app_name));
        builder.setCancelable(false);

        builder.setPositiveButton("Chiudi",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert = builder.create();
    }
}

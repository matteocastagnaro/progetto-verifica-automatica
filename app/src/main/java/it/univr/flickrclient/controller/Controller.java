package it.univr.flickrclient.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import it.univr.flickrclient.R;
import it.univr.flickrclient.view.InfoDialog;
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
 * Controllore per l'architettura MVC
 */
public class Controller {

    /**
     * Stringhe costanti utilizzate come chiavi
     */
    public final static String SEARCH_IMAGES_CHOOSE = "S";
    public final static String LAST_IMAGES_CHOOSE = "L";
    public final static String POPULAR_IMAGES_CHOOSE = "P";

    /**
     * Chiave di Flickr per l'accesso alle API
     */
    public final String FLICKR_KEY = "be2e2de6e0c41bc999230b99c216cb1f";

    /**
     * Metodo necessario per la condivisione dell'immagine selezionata.
     *
     * @param context Contesto dell'activity principale
     * @param bitmap Bitmap dell'immagine da condividere
     * @return Uri
     */
    @UiThread
    public Uri shareImage(Context context, Bitmap bitmap) {
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        return FileProvider.getUriForFile(context, "it.univr.flickrclient.fileprovider", newFile);
    }

    /**
     * Metodo che permette di lanciare il fragment passato come argomento.
     *
     * @param activity Activity principale
     * @param frag Fragment da lanciare
     * @param addToBackStack Valore boolean necessario per capire se il fragment deve essere aggiunto allo stack o meno
     */
    @UiThread
    public void launchFragment(Activity activity, Fragment frag, boolean addToBackStack) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction().replace(R.id.content_fragment, frag);
        // Se devo aggiungere il fragment nello stack o no
        if(addToBackStack){
            transaction.addToBackStack(null).commit(); // In caso di macrosezione
        } else {
            transaction.commit();
        }
    }

    /**
     * Metodo per la creazione del dialog che specifica le info generali dell'app
     *
     * @param activity Activity principale
     */
    @UiThread
    public void infoDialog(Activity activity){
        new InfoDialog(activity).alert.show();
    }

    /**
     * Metodo necessario per l'esecuzione del task passato come argomento
     *
     * @param task Task da lanciare
     */
    @UiThread
    public void launchTask(AsyncTask<Void, Void, Boolean> task){
        task.execute();
    }

    /**
     * Metodo per il lancio dell'intent relativo alla condivisione dell'immagine
     *
     * @param fragment Fragment da cui viene lanciato l'intent
     * @param contentUri Uri dell'immagine da condividere
     */
    @UiThread
    public void launchIntent(Fragment fragment, Uri contentUri){
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Permesso temporaneo per l'app per leggere il file
            shareIntent.setDataAndType(contentUri, fragment.getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            fragment.startActivity(Intent.createChooser(shareIntent, "Scegli un'app"));
        }
    }
}

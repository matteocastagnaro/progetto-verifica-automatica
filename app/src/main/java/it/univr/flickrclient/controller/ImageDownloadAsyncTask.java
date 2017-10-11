package it.univr.flickrclient.controller;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;

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
 * Task che scarica l'immagine solo nel caso della condivisione
 */
public class ImageDownloadAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private String image_url;
    private Bitmap bitmap;

    private Dialog dialog;
    private Fragment fragment;

    private MVC mvc;

    // Costruttore del task asincrono

    /**
     * Costruttore del task. Prende dal controller l'oggetto mvc
     *
     * @param context Contesto dell'activity principale che crea il task come oggetto
     * @param fragment Fragment di riferimento
     * @param url URL dell'immagine da scaricare
     */
    public ImageDownloadAsyncTask(Context context, Fragment fragment, String url){
        this.context = context;
        this.image_url = url;
        this.fragment = fragment;

        mvc = ((FlickrClientApplication) context.getApplicationContext()).getMVC();
    }

    /**
     * Metodo eseguito dall'AsyncTask prima del lancio della thread in background
     */
    @UiThread
    @Override
    protected void onPreExecute() {

        dialog = ProgressDialog.show(context, "", "Caricamento...", true);
        dialog.setCancelable(false);

    }

    /**
     * Thread in background creata dalla thread principale.
     * Effettua il download dell'immagine presente nell'url passato come argomento del costruttore. L'immagine sarà messa in una variabile Bitmap
     *
     * @param voids
     * @return
     */
    @Override
    @WorkerThread
    protected Boolean doInBackground(Void... voids) {

        try {
            URL url = new URL(image_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Metodo che viene lanciato dalla thread principale una volta che é terminata l'esecuzione della thread in background.
     * Elimina il dialog di caricamento e lancia l'intent relativo alla condivisione dell'immagine
     *
     * @param success
     */
    @UiThread
    @Override
    protected void onPostExecute(final Boolean success) {

        dialog.dismiss();

        mvc.controller.launchIntent(fragment, mvc.controller.shareImage(context, bitmap));
    }
}

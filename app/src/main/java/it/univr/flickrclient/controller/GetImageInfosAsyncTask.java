package it.univr.flickrclient.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.model.Model;
import it.univr.flickrclient.view.CommentsAdapter;

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
 * Task per effettuare il download dei commenti
 */
public class GetImageInfosAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final Activity act;

    private JSONObject obj;

    private AlertDialog dialog;
    private String query;

    private String photoId;
    private MVC mvc;

    private ListView listView;
    private ArrayList<Model.Message> messages;

    /**
     * Costruttore del task
     *
     * @param act Activity principale
     * @param photoId ID della foto di cui prendere i commenti
     * @param listView Listview da riempire con i commenti della foto
     */
    public GetImageInfosAsyncTask(Activity act, String photoId, ListView listView) {

        this.act = act;
        this.listView = listView;
        this.photoId = photoId;

        mvc = ((FlickrClientApplication) act.getApplication()).getMVC();
        messages = new ArrayList<>();

    }

    /**
     * Metodo eseguito dall'AsyncTask prima del lancio della thread in background
     */
    @Override
    @UiThread
    protected void onPreExecute() {
        dialog = ProgressDialog.show(act, "", "Caricamento...", true);
        dialog.setCancelable(false);
    }


    /**
     * Thread in background creata dalla thread principale.
     * Effettua il download dei commenti relativi all'id dell'immagine che viene passato al task tramite il costruttore
     *
     * @param voids
     * @return
     */
    @Override
    @WorkerThread
    protected Boolean doInBackground(Void... voids) {

        query = String.format("https://api.flickr.com/services/rest?method=flickr.photos.comments.getList&api_key=%s&photo_id=%s&format=json&nojsoncallback=1",
                mvc.controller.FLICKR_KEY,
                photoId);

        Log.d("QUERY", query);

        try {

            URLConnection urlConnection = new URL(query).openConnection();
            obj = new JSONObject(IOUtils.toString(urlConnection.getInputStream(), "UTF-8"));

            JSONArray arr = obj.getJSONObject("comments").getJSONArray("comment");

            for(int i = 0; i < arr.length(); i++){
                messages.add(new Model.Message(
                        arr.getJSONObject(i).getString("authorname"),
                        arr.getJSONObject(i).getString("datecreate"),
                        arr.getJSONObject(i).getString("_content")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo che viene lanciato dalla thread principale una volta che é terminata l'esecuzione della thread in background.
     * Riempie la listview con i messaggi scaricati, crea il toast per informare che il caricamento é stato completato ed elimina il dialog di caricamento
     *
     * @param success
     */
    @Override
    @UiThread
    protected void onPostExecute(final Boolean success) {

        listView.setAdapter(new CommentsAdapter(act, messages));

        Toast.makeText(act.getApplicationContext(), "Caricamento completato", Toast.LENGTH_SHORT).show();

        dialog.dismiss();

    }
}

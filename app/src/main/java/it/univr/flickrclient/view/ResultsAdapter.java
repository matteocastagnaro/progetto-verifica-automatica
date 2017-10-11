package it.univr.flickrclient.view;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.univr.flickrclient.R;
import it.univr.flickrclient.model.Model;

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
 * Classe che estende ArrayAdapter<Model.Picture> necessaria per popolare la listview delle varie
 * macrosezioni (Immagini popolari, ultime immagini, cerca immagini)
 */
public class ResultsAdapter extends ArrayAdapter<Model.Picture> {

    private Context context;
    private ArrayList<Model.Picture> data;
    private static LayoutInflater inflater = null;

    /**
     * Costruttore dell'adapter delle immagini da mostrare
     *
     * @param context Contesto dell'activity che crea l'adapter
     * @param data Lista di immagini necessaria per popolare la listview
     */
    public ResultsAdapter(Context context, ArrayList<Model.Picture> data) {
        super(context, R.layout.results_list_layout, data);
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Metodo che ritorna l'elemento in posizione i della lista
     *
     * @param i Posizione dell'elemento nella lista
     * @return Elemento in posizione i della lista
     */
    @Override
    public Model.Picture getItem(int i) {
        return data.get(i);
    }

    /**
     * Metodo di creazione della view. Viene popolata la listview con la thumbnail, il nome e la descrizione di ogni immagine della lista
     *
     * @param i ID dell'elemento nella lista
     * @param view View di riferimento
     * @param viewGroup Necessario per l'inflating del layout nella view
     * @return View
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = inflater.inflate(R.layout.results_list_layout, viewGroup, false);
        Spanned result;

        TextView title = (TextView) rowView.findViewById(R.id.result_object_title);

        // Il titolo (e la descrizione in seguito) se contengono link vengono passati correttamente
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(data.get(i).getTitle(), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(data.get(i).getTitle());
        }

        title.setText(result);

        TextView desc = (TextView) rowView.findViewById(R.id.result_object_description);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(data.get(i).getDescription(), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(data.get(i).getDescription());
        }

        desc.setText(result);

        // Impostazione dell'immagine d'anteprima presa tramite libreria Glide
        ImageView thumb = (ImageView) rowView.findViewById(R.id.result_object_thumb);
        Glide.with(context).load(data.get(i).getUrl()).into(thumb);

        return rowView;
    }

}

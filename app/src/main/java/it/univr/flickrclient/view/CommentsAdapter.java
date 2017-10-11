package it.univr.flickrclient.view;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * Classe che estende ArrayAdapter<Model.Message> necessaria per popolare la listview dei commenti dell'immagine
 */
public class CommentsAdapter extends ArrayAdapter<Model.Message> {

    private Context context;
    private ArrayList<Model.Message> data;

    /**
     * Costruttore dell'adapter dei commenti delle immagini
     *
     * @param context Contesto dell'activity che crea l'adapter
     * @param data Lista di elementi necessari per la creazione della listview
     */
    public CommentsAdapter(Context context, ArrayList<Model.Message> data) {
        super(context, R.layout.message_layout, data);

        this.context = context;
        this.data = data;
    }

    /**
     * Metodo che ritorna l'elemento in posizione i della lista
     *
     * @param i Posizione dell'elemento nella lista
     * @return Elemento in posizione i della lista
     */
    @Override
    public Model.Message getItem(int i) {
        return data.get(i);
    }

    /**
     * Metodo di creazione della view. Viene popolata la listview con le informazioni relative ai commenti scaricati dell'immagine selezionata
     *
     * @param i ID dell'elemento nella lista
     * @param view View di riferimento
     * @param viewGroup Necessario per l'inflating del layout nella view
     * @return View
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.message_layout, viewGroup, false);
        Spanned result;

        TextView name = (TextView) rowView.findViewById(R.id.message_sender);
        TextView date = (TextView) rowView.findViewById(R.id.message_time);
        TextView body = (TextView) rowView.findViewById(R.id.message_body);

        name.setText(data.get(i).getAuthor());
        date.setText(parseDate(data.get(i).getDate()));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(data.get(i).getBody(), Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(data.get(i).getBody());
        }

        body.setText(result);

        return rowView;
    }

    /**
     * Metodo utilizzato per la conversione della data da timestamp in formato umano
     *
     * @param date Stringa della data in formato timestamp
     * @return Data in formato umano
     */
    private String parseDate(String date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(Long.parseLong(date) * 1000));
    }

}

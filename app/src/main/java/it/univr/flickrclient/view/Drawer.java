package it.univr.flickrclient.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.R;

import static it.univr.flickrclient.controller.Controller.LAST_IMAGES_CHOOSE;
import static it.univr.flickrclient.controller.Controller.POPULAR_IMAGES_CHOOSE;

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
 * Classe Drawer creata soltanto nella versione per smartphone dell'applicazione.
 * Implementa OnNavigationItemSelectedListener di NavigationView per la creazione del drawer laterale
 */
public class Drawer extends DrawerLayout implements NavigationView.OnNavigationItemSelectedListener {

    private final Activity act;
    private DrawerLayout drawer;
    private MVC mvc;

    /**
     * Costruttore della classe Drawer. Viene preso l'oggetto mvc dal controllore e viene settata la creazione effettiva del drawer
     *
     * @param act Activity principale che crea il drawer
     * @param toolbar Toolbar dell'applicazione
     */
    public Drawer(Activity act, Toolbar toolbar) {
        super(act);

        this.act = act;
        mvc = ((FlickrClientApplication) act.getApplication()).getMVC();

        drawer = (DrawerLayout) act.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(act, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                closeKeyboard();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                closeKeyboard();
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        MenuItem searchImagesItem = navigationView.getMenu().findItem(R.id.nav_search_images);
        searchImagesItem.setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * Metodo senza argomenti passati in input che si occupa di chiudere la tastiera virtuale
     */
    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager)
                act.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Metodo necessario per impostare i vari click nel menu del drawer. Una volta cliccato l'elemento desiderato, il drawer viene chiuso
     *
     * @param item Elemento clickabile del menu
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        ImagesFragment frag = new ImagesFragment();
        // Per passare argomenti al fragment
        Bundle args = new Bundle();

        switch (item.getItemId()){
            case R.id.nav_search_images:
                mvc.controller.launchFragment(act, new SearchImagesFragment(), false);
                break;
            case R.id.nav_last_images:
                args.putString(LAST_IMAGES_CHOOSE, LAST_IMAGES_CHOOSE);
                frag.setArguments(args);
                mvc.controller.launchFragment(act, frag, false);
                break;
            case R.id.nav_popular_images:
                args.putString(POPULAR_IMAGES_CHOOSE, POPULAR_IMAGES_CHOOSE);
                frag.setArguments(args);
                mvc.controller.launchFragment(act, frag, false);
                break;
            case R.id.nav_dev_infos:
                mvc.controller.infoDialog(act);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}

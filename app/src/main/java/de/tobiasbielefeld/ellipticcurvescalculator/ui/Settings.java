/*
* Copyright (C) 2016  Tobias Bielefeld
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
* If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
*/

package de.tobiasbielefeld.ellipticcurvescalculator.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 *  simple activity for settings
 */

@SuppressWarnings("deprecation")
public class Settings extends AppCompatPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout relativeLayoutSettings;
    ListPreference listBackgroundColor;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_settings);

        c.loadSharedPref(this);

        ((ViewGroup) getListView().getParent()).setPadding(0, 0, 0, 0);
        addPreferencesFromResource(R.xml.pref_settings);

        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutSettings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listBackgroundColor = (ListPreference) findPreference(getString(R.string.prefKeyColor));

        c.setBackground(toolbar, relativeLayoutSettings);
        setSummaryBackground();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //only menu item is the back button in the action bar
        finish();                                                                                   //so finish this activity
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this); // Set up a listener whenever a key changes
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);//unregister the listener
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences savedData, String key) {
        switch (key) {

            case "prefKeyColor":
                setSummaryBackground();
                c.setBackground(toolbar, relativeLayoutSettings);
                break;
            case "prefKeyCurveTest":
                testCurve = savedData.getBoolean(key, true);
                break;
            case "prefKeyPointTest":
                testPoint = savedData.getBoolean(key, true);
                break;
            case "prefKeyECDSAKeyTest":
                testECDSA = savedData.getBoolean(key, true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            switch (savedData.getInt("activity", 1)) {
                case 1:
                    startActivity(new Intent(getApplicationContext(), PointAddition.class));
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(getApplicationContext(), DoubleAndAdd.class));
                    finish();
                    break;
                case 3:
                    startActivity(new Intent(getApplicationContext(), OrderOfPoint.class));
                    finish();
                    break;
                case 4:
                    startActivity(new Intent(getApplicationContext(), OrderOfCurve.class));
                    finish();
                    break;
                case 5:
                    startActivity(new Intent(getApplicationContext(), ECDH.class));
                    finish();
                    break;
                case 6:
                    startActivity(new Intent(getApplicationContext(), ECDSA.class));
                    finish();
                    break;
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order_of_a_curve:
                startActivity(new Intent(getApplicationContext(), OrderOfCurve.class));
                finish();
                break;
            case R.id.nav_doubleAndAdd:
                startActivity(new Intent(getApplicationContext(), DoubleAndAdd.class));
                finish();
                break;
            case R.id.nav_order_of_a_point:
                startActivity(new Intent(getApplicationContext(), OrderOfPoint.class));
                finish();
                break;
            case R.id.nav_point_addition:
                startActivity(new Intent(getApplicationContext(), PointAddition.class));
                finish();
                break;
            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(), About.class));
                finish();
                break;
            case R.id.nav_ecdh:
                startActivity(new Intent(getApplicationContext(), ECDH.class));
                finish();
                break;
            case R.id.nav_ecdsa:
                startActivity(new Intent(getApplicationContext(), ECDSA.class));
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSummaryBackground() {
        int index = listBackgroundColor.findIndexOfValue(savedData.getString(getString(R.string.prefKeyColor), "1"));
        listBackgroundColor.setSummary( index >= 0 ? listBackgroundColor.getEntries()[index] : null);
    }
}

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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.tobiasbielefeld.ellipticcurvescalculator.BuildConfig;
import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 * this shows information about the app, license text and changelog in a tabView
 */

public class About extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        TextView textViewBuild = (TextView) findViewById(R.id.about_text_build);
        TextView textViewVersion = (TextView) findViewById(R.id.about_text_version);
        RelativeLayout relativeLayoutAbout = (RelativeLayout) findViewById(R.id.relativeLayoutAbout);
        TextView aboutTextViewGitHubLink = (TextView) findViewById(R.id.aboutTextViewGitHubLink);
        TextView aboutLicenseText = (TextView) findViewById(R.id.aboutLicenseText);

        /* drawer stuff */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView !=null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_about);
        }

        c.setBackground(toolbar, relativeLayoutAbout);

        if (host != null) {
            host.setup();
            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec("Tab One");
            spec.setContent(R.id.tab1);
            spec.setIndicator(getString(R.string.about_tab_1));
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec("Tab Two");
            spec.setContent(R.id.tab2);
            spec.setIndicator(getString(R.string.about_tab_2));
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec("Tab Three");
            spec.setContent(R.id.tab3);
            spec.setIndicator(getString(R.string.about_tab_3));
            host.addTab(spec);
        }

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String strDt = simpleDate.format(BuildConfig.TIMESTAMP);

        if (textViewBuild != null && textViewVersion != null) {
            textViewVersion.setText(String.format(Locale.getDefault(), "%s: %s",
                    getString(R.string.app_version), BuildConfig.VERSION_NAME));
            textViewBuild.setText(String.format(Locale.getDefault(), "%s: %s",
                    getString(R.string.about_build_date), strDt));
        }

        aboutTextViewGitHubLink.setMovementMethod(LinkMovementMethod.getInstance());

        try {                                                                                       //show the gpl license from the license.html in the assets folder
            InputStream is = getAssets().open("license.html");
            aboutLicenseText.setText(Html.fromHtml(new String(getStringFromInputStream(is))));
        } catch (IOException ignored) { }
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
            case R.id.nav_doubleAndAdd:
                startActivity(new Intent(getApplicationContext(), DoubleAndAdd.class));
                finish();
                break;
            case R.id.nav_order_of_a_point:
                startActivity(new Intent(getApplicationContext(), OrderOfPoint.class));
                finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                finish();
                break;
            case R.id.nav_point_addition:
                startActivity(new Intent(getApplicationContext(), PointAddition.class));
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
            case R.id.nav_order_of_a_curve:
                startActivity(new Intent(getApplicationContext(), OrderOfCurve.class));
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private byte[] getStringFromInputStream(InputStream is) {                                       //Solution from StackOverflow, found here: https://stackoverflow.com/questions/2436385
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        byte[] bReturn = new byte[0];
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
            String sContent = sb.toString();
            bReturn = sContent.getBytes();
        } catch (IOException ignored) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
        return bReturn;
    }
}

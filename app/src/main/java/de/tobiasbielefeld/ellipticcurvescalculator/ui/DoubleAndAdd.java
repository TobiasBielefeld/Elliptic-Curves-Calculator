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
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Curve;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.MyPoint;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Result;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 *  calculates the double and add algorithm. See: https://en.wikipedia.org/wiki/Elliptic_curve_point_multiplication#Double-and-add
 *  It takes the curve, an multiplicative factor and the point as input.
 *  Then checks for input errors
 *  and finally uses the doubleAndAdd() method from Calculation
 */

public class DoubleAndAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private EditText editText[] = new EditText[6];
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_and_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_doubleAndAdd);
        }

        editText[0] = (EditText) findViewById(R.id.editText1);
        editText[1] = (EditText) findViewById(R.id.editText2);
        editText[2] = (EditText) findViewById(R.id.editText3);
        editText[3] = (EditText) findViewById(R.id.editText4);
        editText[4] = (EditText) findViewById(R.id.editText5);
        editText[5] = (EditText) findViewById(R.id.editText6);
        textView[0] = (TextView) findViewById(R.id.textView1);
        textView[1] = (TextView) findViewById(R.id.textView2);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);

        editText[0].setText(savedData.getString("2_0", "a"));
        editText[1].setText(savedData.getString("2_1", "b"));
        editText[2].setText(savedData.getString("2_2", "p"));
        editText[3].setText(savedData.getString("2_3", "s"));
        editText[4].setText(savedData.getString("2_4", "x1"));
        editText[5].setText(savedData.getString("2_5", "y1"));
        showDetails2 = savedData.getBoolean("showDetails2", false);

        c.setBackground(toolbar, scrollView);
        checkBox.setChecked(showDetails2);
        textView[0].setVisibility(showDetails2 ? View.VISIBLE : View.GONE);
        edit.putInt("activity", 2).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (savedData.getBoolean(getString(R.string.pref_key_confirm_closing_game), true)
                && (System.currentTimeMillis() - backPressedTime > BACK_PRESSED_TIME_DELTA)) {

            showToast(this,getString(R.string.press_again));
            backPressedTime = System.currentTimeMillis();
        } else
            super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        c.save(2, editText);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order_of_a_curve:
                startActivity(new Intent(getApplicationContext(), OrderOfCurve.class));
                finish();
                break;
            case R.id.nav_point_addition:
                startActivity(new Intent(getApplicationContext(), PointAddition.class));
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

    public void onClick(View view) {
        long text1, text2, text3, text4, text5, text6;
        Curve curve;
        long factor;
        MyPoint point1;
        Result result;
        textView[0].setText("");
        textView[1].setText("");

        try {
            text1 = Long.parseLong(editText[0].getText().toString());
            text2 = Long.parseLong(editText[1].getText().toString());
            text3 = Long.parseLong(editText[2].getText().toString());
            text4 = Long.parseLong(editText[3].getText().toString());
            text5 = Long.parseLong(editText[4].getText().toString());
            text6 = Long.parseLong(editText[5].getText().toString());

            curve = new Curve(text1, text2, text3);
            factor = text4;
            point1 = new MyPoint(text5, text6);

            if (testCurve && curve.p() < 4)
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_1));
            else if (testCurve && !c.primeTest(curve.p()))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_2));
            else if (testCurve && !curve.test())
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_3));
            else if (testPoint && !point1.isOnCurve(curve))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_4));
            else {
                result = c.doubleAndAdd(textView[0], curve, point1, factor);
                set(textView[1],"%s", c.pointOut(result));
            }
        } catch (Exception e) {
            set(textView[1],"%s",getString(R.string.wrong_input));
        }
    }

    public void onCheck(View view) {
        showDetails2 = !showDetails2;
        textView[0].setVisibility(showDetails2 ? View.VISIBLE : View.GONE);
    }
}

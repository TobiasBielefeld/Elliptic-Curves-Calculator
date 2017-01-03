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
 * calculates the order of a point:
 * Add the point to itself, then add the point to the result and continue
 * until it reaches the point of infinity, the number of steps is the order.
 *
 * I know there is a nicer way to do so:
 * The order of a point has to divide the order of the curve.
 * So calculate the order of the curve, look which possible numbers can divide it,
 * and test for each number the doubleAndAdd Algorithm: Number * Point.
 * If one result is the Point of Infinity, thats the order.
 *
 * But the first approach is more simple :)
 *
 * It takes a curve and a point as input,
 * then checks for errors,
 * and finally calculates the order
 */

public class OrderOfPoint extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editText[] = new EditText[5];
    public CheckBox checkBox;

    private DrawerLayout drawer;
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_of_a_point);

        c.loadSharedPref(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView !=null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_order_of_a_point);
        }

        editText[0] = (EditText) findViewById(R.id.editText1);
        editText[1] = (EditText) findViewById(R.id.editText2);
        editText[2] = (EditText) findViewById(R.id.editText3);
        editText[3] = (EditText) findViewById(R.id.editText4);
        editText[4] = (EditText) findViewById(R.id.editText5);
        textView[0] = (TextView) findViewById(R.id.textView1);
        textView[1] = (TextView) findViewById(R.id.textView2);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);

        editText[0].setText(savedData.getString("3_0", "a"));
        editText[1].setText(savedData.getString("3_1", "b"));
        editText[2].setText(savedData.getString("3_2", "p"));
        editText[3].setText(savedData.getString("3_3", "x1"));
        editText[4].setText(savedData.getString("3_4", "y1"));
        showDetails3 = savedData.getBoolean("showDetails3", false);

        c.setBackground(toolbar, scrollView);
        checkBox.setChecked(showDetails3);
        textView[0].setVisibility(showDetails3 ? View.VISIBLE : View.GONE);
        edit.putInt("activity", 3).commit();
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
        c.save(3, editText);
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
            case R.id.nav_doubleAndAdd:
                startActivity(new Intent(getApplicationContext(), DoubleAndAdd.class));
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
        long text1, text2, text3, text4, text5;
        Curve curve;
        MyPoint point1, startPoint;
        Result result;
        set(textView[0],"");
        set(textView[1],"");

        try {
            text1 = Long.parseLong(editText[0].getText().toString());
            text2 = Long.parseLong(editText[1].getText().toString());
            text3 = Long.parseLong(editText[2].getText().toString());
            text4 = Long.parseLong(editText[3].getText().toString());
            text5 = Long.parseLong(editText[4].getText().toString());

            curve = new Curve(text1, text2, text3);
            point1 = new MyPoint(text4, text5);
            startPoint = new MyPoint(point1.x, point1.y);

            if (testCurve && curve.p() < 4)
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_1));
            else if (testCurve && !c.primeTest(curve.p()))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_2));
            else if (testCurve && !curve.test())
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_3));
            else if (testPoint && !startPoint.isOnCurve(curve))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_4));
            else {
                result = c.order(point1, curve, textView[0]);
                set(textView[1], "%s %s", getString(R.string.result), String.valueOf(result.counter));
            }
        } catch (Exception e) {
            set(textView[1], getString(R.string.wrong_input));
        }
    }

    public void onCheck(View view) {
        showDetails3 = !showDetails3;
        textView[0].setVisibility(showDetails3 ? View.VISIBLE : View.GONE);
    }
}

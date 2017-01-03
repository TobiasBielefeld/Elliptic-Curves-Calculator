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

import java.util.Locale;

import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Curve;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.MyPoint;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 *  calculates the order of a curve in the following way:
 *
 *  Note: Order(Curve) is the number of points on that curve.
 *  The formula for an elliptic curve is: y² = x³ + a*x + b mod p
 *
 *  First, create an array for y values: Use integer values from 0 to p-1 as index,
 *  then calculate y² mod p for each value and save them in the array.
 *  Now we have an array with all possible y values for that curve.
 *
 *  Next, iterate through every possible x value: Use integer values from 0 to p-1 as text value,
 *  then calculate y-test = x³ + a*x + b mod p for each value.
 *  Now loop through the y array and look if the y-test is in that array.
 *
 *  If it's there, we know the point (x,y) is on the curve.
 *
 *  The loops will find every point. If an x value is on the curve, it has two points: (x,y) and (x,p-y)
 *
 *  Last but not least, add the Point of Infinity. Count all points (also Point of Inf)
 *  and thats the order of the curve.
 *
 *  It takes the curve as input
 *  Then checks for input errors
 *  Then calculates the order
 */

public class OrderOfCurve extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editText[] = new EditText[3];
    public CheckBox checkBox;

    private DrawerLayout drawer;
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_of_a_curve);

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
            navigationView.setCheckedItem(R.id.nav_order_of_a_curve);
        }

        editText[0] = (EditText) findViewById(R.id.editText1);
        editText[1] = (EditText) findViewById(R.id.editText2);
        editText[2] = (EditText) findViewById(R.id.editText3);
        textView[0] = (TextView) findViewById(R.id.textView1);
        textView[1] = (TextView) findViewById(R.id.textView2);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);

        editText[0].setText(savedData.getString("4_0", "a"));
        editText[1].setText(savedData.getString("4_1", "b"));
        editText[2].setText(savedData.getString("4_2", "p"));
        showDetails4 = savedData.getBoolean("showDetails4", false);

        c.setBackground(toolbar, scrollView);
        checkBox.setChecked(showDetails4);
        textView[0].setVisibility(showDetails4 ? View.VISIBLE : View.GONE);
        edit.putInt("activity", 4).commit();
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
        c.save(4, editText);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order_of_a_point:
                startActivity(new Intent(getApplicationContext(), OrderOfPoint.class));
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
        int iCounter = 1;
        long yValues[];
        long testValues;
        long text1, text2, text3;
        Curve curve;
        set(textView[0],"");
        set(textView[1],"");

        try {
            text1 = Long.parseLong(editText[0].getText().toString());
            text2 = Long.parseLong(editText[1].getText().toString());
            text3 = Long.parseLong(editText[2].getText().toString());

            curve = new Curve(text1, text2, text3);

            if (testCurve && curve.p() < 4)
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_1));
            else if (testCurve && !c.primeTest(curve.p()))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_2));
            else if (testCurve && !curve.test())
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_3));
            else {
                set(textView[1], "%s", getString(R.string.points_on_the_curve));

                yValues = new long[(int)text3];

                for (int y = 0; y < curve.p(); y++) {
                    yValues[y] = (y * y) % curve.p();
                }

                for (int x = 0; x < curve.p(); x++) {
                    testValues = (x * x * x + curve.a() * x + curve.b()) % curve.p();

                    for (int i = 0; i < curve.p(); i++) {
                        if (testValues == yValues[i]) {
                            iCounter++;
                            textView[0].setText(String.format(Locale.getDefault(), "%s%s ", textView[0].getText(), c.pointOut(new MyPoint(x, i))));

                        }
                    }
                }

                set(textView[0], "%s PointOfInf ", textView[0].getText());
                set(textView[1], "%s %s", getString(R.string.result), iCounter);
            }
        } catch (Exception e) {
            set(textView[0], "");
            set(textView[1], getString(R.string.wrong_input));
        }
    }

    public void onCheck(View view) {
        showDetails4 = !showDetails4;
        textView[0].setVisibility(showDetails4 ? View.VISIBLE : View.GONE);
    }
}

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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Curve;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.MyPoint;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Result;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 * Calculates the Elliptic Curves Diffie Hellman Algorithm, see https://en.wikipedia.org/wiki/Elliptic_curve_Diffie%E2%80%93Hellman
 *
 * It takes a curve, a public point and two private points as input,
 * then checks for input errors,
 * and then calculates the algorithm.
 * The shared Key KeyAB will be calculated in both possible ways.
 */

public class ECDH extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private EditText editText[] = new EditText[7];
    private CheckBox checkBox[] = new CheckBox[5];
    private LinearLayout l1;
    private TextView text[] = new TextView[8];

    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdh);

        c.loadSharedPref(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_ecdh);
        }

        editText[0] = (EditText) findViewById(R.id.editText1);
        editText[1] = (EditText) findViewById(R.id.editText2);
        editText[2] = (EditText) findViewById(R.id.editText3);
        editText[3] = (EditText) findViewById(R.id.editText4);
        editText[4] = (EditText) findViewById(R.id.editText5);
        editText[5] = (EditText) findViewById(R.id.editText6);
        editText[6] = (EditText) findViewById(R.id.editText7);
        textView[1] = (TextView) findViewById(R.id.textView1);

        checkBox[0] = (CheckBox) findViewById(R.id.checkBox1);
        checkBox[1] = (CheckBox) findViewById(R.id.checkBox2);
        checkBox[2] = (CheckBox) findViewById(R.id.checkBox3);
        checkBox[3] = (CheckBox) findViewById(R.id.checkBox4);
        checkBox[4] = (CheckBox) findViewById(R.id.checkBox5);
        text[0] = (TextView) findViewById(R.id.textView2);
        text[1] = (TextView) findViewById(R.id.textView3);
        text[2] = (TextView) findViewById(R.id.textView4);
        text[3] = (TextView) findViewById(R.id.textView5);
        text[4] = (TextView) findViewById(R.id.textView6);
        text[5] = (TextView) findViewById(R.id.textView7);
        text[6] = (TextView) findViewById(R.id.textView8);
        text[7] = (TextView) findViewById(R.id.textView9);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
        l1 = (LinearLayout) findViewById(R.id.linearLayout2);

        editText[0].setText(savedData.getString("5_0", "a"));
        editText[1].setText(savedData.getString("5_1", "b"));
        editText[2].setText(savedData.getString("5_2", "p"));
        editText[3].setText(savedData.getString("5_3", "x"));
        editText[4].setText(savedData.getString("5_4", "y"));
        editText[5].setText(savedData.getString("5_5", "a"));
        editText[6].setText(savedData.getString("5_6", "b"));

        c.setBackground(toolbar, scrollView);
        l1.setVisibility(View.GONE);
        edit.putInt("activity", 5).commit();
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
        c.save(5, editText);
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
            case R.id.nav_order_of_a_point:
                startActivity(new Intent(getApplicationContext(), OrderOfPoint.class));
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
        long text1, text2, text3, text4, text5, text6, text7;
        Curve curve;
        MyPoint generator, pointA, pointB;
        long privA, privB;
        Result result;
        set(textView[1],"");
        l1.setVisibility(View.GONE);

        try {
            text1 = Long.parseLong(editText[0].getText().toString());
            text2 = Long.parseLong(editText[1].getText().toString());
            text3 = Long.parseLong(editText[2].getText().toString());
            text4 = Long.parseLong(editText[3].getText().toString());
            text5 = Long.parseLong(editText[4].getText().toString());
            text6 = Long.parseLong(editText[5].getText().toString());
            text7 = Long.parseLong(editText[6].getText().toString());

            curve = new Curve(text1, text2, text3);
            generator = new MyPoint(text4, text5);
            privA = text6;
            privB = text7;
            pointA = new MyPoint(0, 0);
            pointB = new MyPoint(0, 0);

            if (testCurve && curve.p() < 4)
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_1));
            else if (testCurve && !c.primeTest(curve.p()))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_2));
            else if (testCurve && !curve.test())
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_3));
            else if (testPoint && !generator.isOnCurve(curve))
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_4));
            else {
                l1.setVisibility(View.VISIBLE);
                for (int i = 1; i < 5; i++)
                    checkBox[i].setChecked(false);

                text[1].setVisibility(View.GONE);
                text[3].setVisibility(View.GONE);
                text[5].setVisibility(View.GONE);
                text[7].setVisibility(View.GONE);

                set( text[0], "KpubA = KprivA * P\n = %s *%s", privA, c.pointOut(generator));
                result = c.doubleAndAdd(text[1], curve, generator, privA);
                pointA.x = result.x3;
                pointA.y = result.y3;
                set(text[0], "%s =%s", text[0].getText(), c.pointOut(pointA));

                set( text[2], "KpubB = KprivB * P\n = %s *%s", privB, c.pointOut(generator));
                result = c.doubleAndAdd(text[3], curve, generator, privB);
                pointB.x = result.x3;
                pointB.y = result.y3;
                set( text[2], "%s =%s", text[2].getText(), c.pointOut(pointB));

                set(text[4], "KAB = KprivA * KpubB\n = %s *%s", privA, c.pointOut(pointB));
                result = c.doubleAndAdd(text[5], curve, pointB, privA);
                pointB.x = result.x3;
                pointB.y = result.y3;
                set(text[4], "%s =%s", text[4].getText(), c.pointOut(pointB));

                set(text[6], "KAB = KprivB * KpubA\n = %s *%s", privB, c.pointOut(pointA));
                result = c.doubleAndAdd(text[7], curve, pointA, privB);
                pointA.x = result.x3;
                pointA.y = result.y3;
                set(text[6], "%s =%s", text[6].getText(), c.pointOut(pointA));
            }
        } catch (Exception e) {
            l1.setVisibility(View.GONE);
            set(textView[1],getString(R.string.wrong_input));
        }
    }

    public void onCheck(View view) {
        switch (view.getId()) {
            case R.id.checkBox2:
                text[1].setVisibility(checkBox[1].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox3:
                text[3].setVisibility(checkBox[2].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox4:
                text[5].setVisibility(checkBox[3].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox5:
                text[7].setVisibility(checkBox[4].isChecked() ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public static class myDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.ECDHHelpTitle)
                .setMessage(R.string.ECDHHelpText)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });

            return builder.create();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment newFragment = new myDialog();
        newFragment.show(getSupportFragmentManager(), "help");

        return true;
    }
}

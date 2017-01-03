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

import de.tobiasbielefeld.ellipticcurvescalculator.Helper.Calculation;
import de.tobiasbielefeld.ellipticcurvescalculator.R;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Curve;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.MyPoint;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Result;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

 /*
  * calculates the Elliptic Curves Digital Signature Algorithm, see https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm
  * It takes a curve, the generator A, a private key d, an ephermal key kE and the message x to sign as input.
  * Then it checks for input errors, and calculates the ECDSA algorithm.
  */

public class ECDSA extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editText[] = new EditText[8];
    public CheckBox checkBox[] = new CheckBox[9];
    public LinearLayout l1, l2;
    public TextView text[] = new TextView[30];

    private DrawerLayout drawer;
    private Calculation c = new Calculation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdsa);

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
            navigationView.setCheckedItem(R.id.nav_ecdsa);
        }

        editText[0] = (EditText) findViewById(R.id.editText1);
        editText[1] = (EditText) findViewById(R.id.editText2);
        editText[2] = (EditText) findViewById(R.id.editText3);
        editText[3] = (EditText) findViewById(R.id.editText4);
        editText[4] = (EditText) findViewById(R.id.editText5);
        editText[5] = (EditText) findViewById(R.id.editText7);
        editText[6] = (EditText) findViewById(R.id.editText8);
        editText[7] = (EditText) findViewById(R.id.editText9);

        checkBox[0] = (CheckBox) findViewById(R.id.checkBox0);
        checkBox[1] = (CheckBox) findViewById(R.id.checkBox1);
        checkBox[2] = (CheckBox) findViewById(R.id.checkBox2);
        checkBox[3] = (CheckBox) findViewById(R.id.checkBox3);
        checkBox[4] = (CheckBox) findViewById(R.id.checkBox4);
        checkBox[5] = (CheckBox) findViewById(R.id.checkBox5);
        checkBox[6] = (CheckBox) findViewById(R.id.checkBox6);
        checkBox[7] = (CheckBox) findViewById(R.id.checkBox7);
        checkBox[8] = (CheckBox) findViewById(R.id.checkBox8);

        text[2] = (TextView) findViewById(R.id.textView2);
        text[3] = (TextView) findViewById(R.id.textView3);
        text[4] = (TextView) findViewById(R.id.textView4);
        text[5] = (TextView) findViewById(R.id.textView5);
        text[6] = (TextView) findViewById(R.id.textView6);
        text[7] = (TextView) findViewById(R.id.textView7);
        text[8] = (TextView) findViewById(R.id.textView8);
        text[9] = (TextView) findViewById(R.id.textView9);
        text[10] = (TextView) findViewById(R.id.textView10);
        text[11] = (TextView) findViewById(R.id.textView11);
        text[12] = (TextView) findViewById(R.id.textView12);
        text[13] = (TextView) findViewById(R.id.textView13);
        text[14] = (TextView) findViewById(R.id.textView14);
        text[15] = (TextView) findViewById(R.id.textView15);
        text[16] = (TextView) findViewById(R.id.textView16);
        text[17] = (TextView) findViewById(R.id.textView17);
        text[18] = (TextView) findViewById(R.id.textView18);
        text[19] = (TextView) findViewById(R.id.textView19);
        text[20] = (TextView) findViewById(R.id.textView20);
        text[21] = (TextView) findViewById(R.id.textView21);

        textView[1] = (TextView) findViewById(R.id.textView1);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
        l1 = (LinearLayout) findViewById(R.id.linearLayout2);
        l2 = (LinearLayout) findViewById(R.id.linearLayout3);

        editText[0].setText(savedData.getString("6_0", "a"));
        editText[1].setText(savedData.getString("6_1", "b"));
        editText[2].setText(savedData.getString("6_2", "p"));
        editText[3].setText(savedData.getString("6_3", "x"));
        editText[4].setText(savedData.getString("6_4", "y"));
        editText[5].setText(savedData.getString("6_5", "d"));
        editText[6].setText(savedData.getString("6_6", "kE"));
        editText[7].setText(savedData.getString("6_7", "x"));

        c.setBackground(toolbar, scrollView);
        l1.setVisibility(View.GONE);
        edit.putInt("activity", 6).commit();
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
        c.save(6, editText);
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
            case R.id.nav_ecdh:
                startActivity(new Intent(getApplicationContext(), ECDH.class));
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {

        long text1, text2, text3, text4, text5, text6, text7, text8;
        Curve curve;
        MyPoint A, B = new MyPoint(0, 0), PR = new MyPoint(0, 0), P1 = new MyPoint(0, 0), P2 = new MyPoint(0, 0);
        long q, d, kE, x, r, s, w, u1, u2;
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
            text8 = Long.parseLong(editText[7].getText().toString());

            curve = new Curve(text1, text2, text3);
            A = new MyPoint(text4, text5);
            d = text6;
            kE = text7;
            x = text8;

            if (!c.primeTest(curve.p()))                                                            //always test if p is prime, else calculation of ord(A) can be an infinite loop
                set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_2));
            else {
                result = c.order(A, curve, text[3]);
                q = result.counter;

                if (testCurve && curve.p() < 4)
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_1));
                else if (testCurve && !curve.test())
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_3));
                else if (testPoint && !A.isOnCurve(curve))
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_4));
                else if (testPoint && !c.primeTest(q))
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_8));
                else if (testECDSA && (d <= 0 || d >= q))
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_6));
                else if (testECDSA && (kE <= 0 || kE >= q))
                    set(textView[1], "%s %s", getString(R.string.error), getString(R.string.error_7));
                else {
                    l1.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 8; i++)
                        checkBox[i].setChecked(false);

                    text[3].setVisibility(View.GONE);
                    text[5].setVisibility(View.GONE);
                    text[7].setVisibility(View.GONE);
                    text[9].setVisibility(View.GONE);
                    text[12].setVisibility(View.GONE);
                    text[14].setVisibility(View.GONE);
                    text[16].setVisibility(View.GONE);
                    text[20].setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);

                    /*
                        SETUP PHASE
                     */

                    set(text[2], "%s = q = %s", getString(R.string.order_of_a), q);

                    set(text[4], "B = d * A\n = %s * %s", d, c.pointOut(A));
                    result = c.doubleAndAdd(text[5], curve, A, d);
                    B.x = result.x3;
                    B.y = result.y3;
                    set(text[4], "%s = %s", text[4].getText(), c.pointOut(B));

                    set(text[18], "%s = ( Curve, A, B )", getString(R.string.public_Key));

                    /*
                        SIGNING
                     */

                    set(text[6], "R = kE * A\n = %s * %s", kE, c.pointOut(A));
                    result = c.doubleAndAdd(text[7], curve, A, kE);
                    PR.x = result.x3;
                    PR.y = result.y3;
                    set(text[6], "%s = %s", text[6].getText(), c.pointOut(PR));

                    r = PR.x;
                    s = ((x + d * r) * c.invers(kE, q)) % q;
                    w = c.invers(s, q);
                    u1 = (w * x) % q;
                    u2 = (w * r) % q;

                    set(text[8], "r = %s, s = %s", r, s);

                    set(text[9], "r = xR = %s\n" +
                                    "s = ( x + d * r ) * kE⁻¹ mod q \n" +
                                    "  = ( %s + %s * %s ) * %s⁻¹ mod %s\n" +
                                    "  = %s",
                            r, x, d, r, kE, q, s);

                    set(text[21], "%s = x , ( r , s )", getString(R.string.signature));

                    /*
                        VERIFICATION
                     */

                    set(text[19], "w = %s, u1 = %s, u2 = %s", w, u1, u2);

                    set(text[20], "w = s⁻¹ mod q\n" +
                                    "   = %s⁻¹ mod %s\n" +
                                    "   = %s\n" +
                                    "u1 = w * x mod q\n" +
                                    "   = %s * %s mod %s\n" +
                                    "   = %s\n" +
                                    "u2 = w * r mod q\n" +
                                    "   = %s * %s mod %s\n" +
                                    "   = %s"
                            , s, q, w, w, x, q, u1, w, r, q, u2);

                    set(text[10], "P = u1 * A + u2 * B\n  = %s *%s + %s *%s", u1, c.pointOut(A), u2, c.pointOut(B));


                    result = c.doubleAndAdd(text[12], curve, A, u1);
                    P1.x = result.x3;
                    P1.y = result.y3;
                    set(text[11], "%s *%s =%s", u1, c.pointOut(A), c.pointOut(P1));

                    result = c.doubleAndAdd(text[14], curve, B, u2);
                    P2.x = result.x3;
                    P2.y = result.y3;
                    set(text[13], "%s *%s =%s", u2, c.pointOut(B), c.pointOut(P2));

                    result = c.pointAddition(P1, P2, curve);
                    set(text[16], "%s", result.output);
                    set(text[15], "%s +%s = %s", c.pointOut(P1), c.pointOut(P2), c.pointOut(new MyPoint(result.x3, result.y3)));

                    set(text[10], "%s\n  =%s", text[10].getText(), text[15].getText());

                    set(text[17], "Px = r    <=>    %s = %s\n%s %s %s%s",
                            result.x3, r, getString(R.string.signature), getString(R.string.is), (r == result.x3 ? "" : getString(R.string.not) + " "), getString(R.string.valid));
                }
            }
        } catch (Exception e) {
            l1.setVisibility(View.GONE);
            set(textView[1],getString(R.string.wrong_input));
        }

    }

    public void onCheck(View view) {
        switch (view.getId()) {
            case R.id.checkBox0:
                text[3].setVisibility(checkBox[0].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox1:
                text[5].setVisibility(checkBox[1].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox2:
                text[7].setVisibility(checkBox[2].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox3:
                text[9].setVisibility(checkBox[3].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox4:
                text[12].setVisibility(checkBox[4].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox5:
                text[14].setVisibility(checkBox[5].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox6:
                text[16].setVisibility(checkBox[6].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox7:
                text[20].setVisibility(checkBox[7].isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkBox8:
                l2.setVisibility(checkBox[8].isChecked() ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public static class myDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.ECDSAHelpTitle)
                    .setMessage(R.string.ECDSAHelpText)
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

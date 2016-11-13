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

package de.tobiasbielefeld.ellipticcurvescalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/*
 * shared data for the whole app, i thought this was simpler to code
 */

public class SharedData {


    public static TextView textView[] = new TextView[2];
    public static boolean showDetails1;
    public static boolean showDetails2;
    public static boolean showDetails3;
    public static boolean showDetails4;
    private static Toast toast;

    public static long backPressedTime;
    public static long BACK_PRESSED_TIME_DELTA = 2000;
    public static SharedPreferences savedData;
    public static SharedPreferences.Editor edit;
    public static int color;
    public static int activity;
    public static boolean testPoint;
    public static boolean testCurve;
    public static boolean testECDSA;
    public static String p1IsInf, p2IsInf, noCalcNeeded, x1EqualsX2, step, doubling, adding, result0, result1;

    public static void showToast(Context context, String text) {                                    //simple function to show a new toast text
        if (toast == null)
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);                              //initialize toast
        else
            toast.setText(text);

        toast.show();
    }

    //to shorten my code, so there isnt "String.format(Locale.getDefault()" everywhere
    public static void set(TextView view, String format, Object... args){
        view.setText(String.format(Locale.getDefault(), format, args));
    }

    public static String set(String format, Object... args){
        return String.format(Locale.getDefault(), format, args);
    }

    public static void set(TextView view, String text){
        view.setText(text);
    }
}

package de.tobiasbielefeld.ellipticcurvescalculator.Helper;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.tobiasbielefeld.ellipticcurvescalculator.classes.Curve;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.LongMod;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.MyPoint;
import de.tobiasbielefeld.ellipticcurvescalculator.classes.Result;

import de.tobiasbielefeld.ellipticcurvescalculator.R;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/**
 * basic calculation methods for addition, multiplication and so on.
 * There are also some helper functions
 */

public class Calculation {

    //Point addition with two points on a curve
    public Result pointAddition(MyPoint point1, MyPoint point2, Curve curve) {
        String output;

        if (point1.isInf())
            return new Result(point2.x, point2.y, p1IsInf + "\n" + noCalcNeeded);
        if (point2.isInf())
            return new Result(point1.x, point1.y, p2IsInf + "\n" + noCalcNeeded);

        LongMod s = new LongMod(0, curve.p());
        LongMod x3 = new LongMod(0, curve.p());
        LongMod y3 = new LongMod(0, curve.p());
        LongMod s1 = new LongMod(0, curve.p());
        LongMod s2 = new LongMod(0, curve.p());

        if (point1.x == point2.x && point1.y == point2.y) {
            s1.set(3 * point1.x * point1.x + curve.a());
            s2.set(invers(2 * point1.y, curve.p()));

            output  = set("s = ( 3 * x1² + a ) * ( 2 * y1 )⁻¹ mod p\n");
            output += set("  = ( 3 * %s² + %s ) * ( 2 * %s )⁻¹ mod %s\n", point1.x, curve.a(), point1.y, curve.p());
            output += set("  =  %s * %s mod %s\n", s1.get(), s2.get(), curve.p());
        } else if (point1.x == point2.x) {
            return new Result(-1, -1, x1EqualsX2 + "\n" + noCalcNeeded);
        } else {
            s1.set(point2.y - point1.y);
            s2.set(point2.x - point1.x);
            s2.set(invers(s2.get(), curve.p()));

            output  = set("s = ( y2 - y1 ) * ( x2 - x1 )⁻¹ mod p\n");
            output += set("  = ( %s - %s ) * ( %s - %s )⁻¹ mod %s\n", point2.y, point1.y, point2.x, point1.x, curve.p());
            output += set("  =  %s * %s mod %s\n", s1.get(), s2.get(), curve.p());
        }

        s.set(s1.get() * s2.get());
        x3.set(s.get() * s.get() - point1.x - point2.x);
        y3.set(s.get() * (point1.x - x3.get()) - point1.y);

        output += set("   = %s\n\n", s.get());
        output += set("x3 = s² - x1 - x2 mod p\n");
        output += set("   = %s² - %s - %s mod %s\n", s.get(), point1.x, point2.x, curve.p());
        output += set("   = %s \n\n", x3.get());
        output += set("y3 = s * ( x1 - x3 ) - y1 mod p\n");
        output += set("   = %s * ( %s - %s ) - %s mod %s\n", s.get(), point1.x, x3.get(), point1.y, curve.p());
        output += set("   = %s", y3.get());

        return new Result(x3.get(), y3.get(), output);
    }

    //point multiplication using the double and add algorithm
    public Result doubleAndAdd(TextView text, Curve curve, MyPoint point1, long factor) {
        String sNumber = "1";
        MyPoint point = new MyPoint(point1.x, point1.y);
        MyPoint start = new MyPoint(point.x, point.y);
        Result result = new Result(point.x, point.y, "");

        char binary[] = Long.toBinaryString(factor).toCharArray();

        set(text, "%s -> %s\n", factor, new String(binary));

        if (factor == 0) {
            result.x3 = 0;
            result.y3 = 0;
            set(text, "%s", text.getText() + pointOut(result));
        }

        if (factor == 1) {
            set(text, "%s", text.getText() + pointOut(result));
        }

        for (int i = 1; i < binary.length; i++) {
            set(text, "%s\n" +
                    "-----------\n" +
                    "%s %sa: ",
                    text.getText(), step, i);

            set(text, "%s -> ", text.getText() + sNumber);
            sNumber += "0";
            set(text, "%s", text.getText() + sNumber);
            set(text, "%s\n" +
                    "%s: %s +%s\n" +
                    "-----------\n",
                    text.getText(), doubling, pointOut(point), pointOut(point));

            result = pointAddition(point, point, curve);
            point.x = result.x3;
            point.y = result.y3;

            set(text, "%s%s\n" +
                    "\n" +
                    "%s\n",
                    text.getText(), result.output, pointOut(result));

            if (binary[i] == '1') {
                set(text, "%s\n" +
                        "-----------\n" +
                        "%s %sb: ",
                        text.getText(), step, i);

                set(text, "%s%s ->", text.getText(), sNumber);
                sNumber = sNumber.substring(0, sNumber.length() - 1) + "1";
                set(text, "%s%s", text.getText(), sNumber);
                set(text, "%s\n" +
                        "%s: %s + %s\n" +
                        "-----------\n",
                        text.getText(), adding, pointOut(point), pointOut(start));

                result = pointAddition(point, start, curve);
                point.x = result.x3;
                point.y = result.y3;
                set(text, "%s%s\n" +
                        "\n" +
                        "%s\n",
                        text.getText(), result.output, pointOut(result));
            }
        }

        return result;
    }

    //calcualtes order of a point on a curve
    public Result order(MyPoint point, Curve curve, TextView view) {
        long iCounter = 0;
        Result result = new Result(0, 0, "");
        MyPoint point1 = new MyPoint(point.x, point.y);
        MyPoint startPoint = new MyPoint(point.x, point.y);

        set(view, "-----------\n" +
                "%s %s%s = %s\n" +
                "-----------",
                step, ++iCounter, pointOut(point1), pointOut(startPoint));

        while (!point1.isInf()) {
            set(view, "%s\n" +
                    "-----------\n" +
                    "%s %s%s + %s\n" +
                    "-----------\n",
                    view.getText(), step, ++iCounter, pointOut(point1), pointOut(startPoint));

            result = pointAddition(point1, startPoint, curve);
            point1.x = result.x3;
            point1.y = result.y3;
            set(view, "%s%s\n" +
                    "\n" +
                    "%s\n",
                    view.getText(), result.output, pointOut(result));
        }

        result.counter = iCounter;
        return result;
    }


    //calculates the invers value: x^(-1) mod p
    public long invers(long number, long p) {
        int i = 1;
        long q;
        long s[] = new long[100], t[] = new long[100], r[] = new long[100];

        s[0] = 1;
        s[1] = 0;
        t[0] = 0;
        t[1] = 1;
        r[0] = p;
        r[1] = number;

        while (r[i] != 0) {
            i++;
            r[i] = r[i - 2] % r[i - 1];
            q = (r[i - 2] - r[i]) / r[i - 1];
            s[i] = s[i - 2] - q * s[i - 1];
            t[i] = t[i - 2] - q * t[i - 1];
        }

        while (t[i - 1] < 0)
            t[i - 1] += p;

        return t[i - 1];
    }

    //tests if a number is prime
    public boolean primeTest(long number) {
        if (number % 2 == 0)
            return false;

        for (int i = 4; i < number / 2 + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    //output of a point like: "Result: ( x , y )
    public String pointOut(Result result) {
        if (result.isInf())
            return set("%s PointOfInf", result0);
        else
            return set("%s ( %s , %s )", result0, result.x3, result.y3);
    }

    public String pointOut(MyPoint point) {
        if (point.isInf())
            return (" PointOfInf");
        else
            return set(" ( %s , %s )", point.x, point.y);
    }

    //sets backgrounds for the activity
    public void setBackground(Toolbar toolbar, View view) {
        switch (savedData.getString("prefKeyColor", "5")) {
            case "1":
                toolbar.setBackgroundResource(R.color.blue);
                view.setBackgroundResource(R.drawable.color_blue);
                break;
            case "2":
                toolbar.setBackgroundResource(R.color.green);
                view.setBackgroundResource(R.drawable.color_green);
                break;
            case "3":
                toolbar.setBackgroundResource(R.color.red);
                view.setBackgroundResource(R.drawable.color_red);
                break;
            case "4":
                toolbar.setBackgroundResource(R.color.yellow);
                view.setBackgroundResource(R.drawable.color_yellow);
                break;
            case "5":
                toolbar.setBackgroundResource(R.color.colorPrimary);
                view.setBackgroundResource(R.drawable.color_white);
                break;
        }
    }

    //saves the current values on the editTexts, so they can be restored later
    public void save(int activity, EditText[] editText) {
        for (int i = 0; i < editText.length; i++)
            edit.putString(Integer.toString(activity) + "_" + Integer.toString(i), editText[i].getText().toString());

        switch (activity) {
            case 1:
                edit.putBoolean("showDetails1", showDetails1);
                break;
            case 2:
                for (int i = 0; i < 6; i++)
                    edit.putString("2_" + Integer.toString(i), editText[i].getText().toString());
                edit.putBoolean("showDetails2", showDetails2);
                break;
            case 3:
                for (int i = 0; i < 5; i++)
                    edit.putString("3_" + Integer.toString(i), editText[i].getText().toString());
                edit.putBoolean("showDetails3", showDetails3);
                break;
            case 4:
                for (int i = 0; i < 2; i++)
                    edit.putString("4_" + Integer.toString(i), editText[i].getText().toString());
                edit.putBoolean("showDetails4", showDetails4);
                break;
            case 5:
                for (int i = 0; i < 7; i++)
                    edit.putString("5_" + Integer.toString(i), editText[i].getText().toString());
                break;
            case 6:
                for (int i = 0; i < 8; i++)
                    edit.putString("6_" + Integer.toString(i), editText[i].getText().toString());
                break;

        }

        edit.apply();
    }

    public void loadSharedPref(Activity activity){
        if (savedData==null) {
            savedData = PreferenceManager.getDefaultSharedPreferences(activity);
            edit = savedData.edit();
            edit.apply();
        }
    }
}

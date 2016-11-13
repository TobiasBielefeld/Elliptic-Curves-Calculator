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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import de.tobiasbielefeld.ellipticcurvescalculator.R;

import static de.tobiasbielefeld.ellipticcurvescalculator.SharedData.*;

/*
 *  Simply loads strings and saved preferences, then loads the last opened activity
 */

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedData = PreferenceManager.getDefaultSharedPreferences(this);
        edit = savedData.edit(); edit.apply();

        activity = savedData.getInt("activity",1);
        testCurve = savedData.getBoolean(getString(R.string.prefKeyCurveTest),true);
        testPoint = savedData.getBoolean(getString(R.string.prefKeyPointTest),true);
        testECDSA = savedData.getBoolean(getString(R.string.prefKeyECDSAKeyTest),true);

        p1IsInf = getString(R.string.p1IsInf);
        p2IsInf = getString(R.string.p2IsInf);
        noCalcNeeded = getString(R.string.noCalcNeeded);
        x1EqualsX2 = getString(R.string.x1EqualsX2);
        step = getString(R.string.step);
        doubling = getString(R.string.Doubling);
        adding = getString(R.string.Adding);
        result0 = getString(R.string.result);
        result1 = getString(R.string.result_1);

        switch (activity) {
            case 1: startActivity(new Intent(getApplicationContext(), PointAddition.class)); break;
            case 2: startActivity(new Intent(getApplicationContext(), DoubleAndAdd.class)); break;
            case 3: startActivity(new Intent(getApplicationContext(), OrderOfPoint.class)); break;
            case 4: startActivity(new Intent(getApplicationContext(), OrderOfCurve.class)); break;
            case 5: startActivity(new Intent(getApplicationContext(), ECDH.class)); break;
            case 6: startActivity(new Intent(getApplicationContext(), ECDSA.class)); break;
        }

        finish();
    }
}

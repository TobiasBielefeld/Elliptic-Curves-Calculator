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

package de.tobiasbielefeld.ellipticcurvescalculator.classes;

/*
 * class for elliptic curves with Long integer values
 */

public class Curve {

    private LongMod a;
    private LongMod b;
    private long p;

    public Curve(long pA, long pB, long pP) {
        a = new LongMod(pA, pP);
        b = new LongMod(pB, pP);

        p = pP;
    }

    public long a() {
        return a.get();
    }

    public long b() {
        return b.get();
    }

    public long p() {
        return p;
    }

    public boolean test() {
        //checks if its an valid curve, this statement has to be fullfilled
        return ((4 * a.get() * a() * a() + 27 * b.get() * b.get()) % p != 0);
    }
}

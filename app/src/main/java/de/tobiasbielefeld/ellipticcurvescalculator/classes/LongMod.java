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
 * my class for modulo values. It uses long integers and automatically calculates
 * value mod p when the number is set.
 */

public class LongMod {

    private long value;
    private long modulo;

    public LongMod(long pValue, long mod) {
        modulo = mod;
        set(pValue);
    }

    public long get() {
        return value;
    }

    public void set(long pValue) {
        value = pValue;

        while (value < 0)
            value += modulo;

        value %= modulo;
    }
}

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
 * A custom result for the calculation methods. i want to show step by step solutions, so a string
 * is also part of the result.
 * x3 and y3 are one point,
 * counter is used for order of a point
 */

public class Result {

    public long x3;
    public long y3;
    public String output;
    public long counter;

    public Result(long p1, long p2, String p3) {
        x3 = p1;
        y3 = p2;
        output = p3;
    }

    public boolean isInf() {
        return x3 == -1 && y3 == -1;
    }
}

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
 * A point on the elliptic curve with long integers.
 */

public class MyPoint {

    public long x;
    public long y;

    public MyPoint(long pX, long pY) {
        x=pX;
        y=pY;
    }

    public boolean isOnCurve(Curve curve) {
        return ( (x==-1 && y==-1 ) || ( x*x*x+curve.a()*x+curve.b() ) % curve.p() == (y*y) % curve.p() );
    }

    public boolean isInf() {
        return x==-1 && y==-1;
    }
}

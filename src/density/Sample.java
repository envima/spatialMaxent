/*
Copyright (c) 2016 Steven Phillips, Miro Dudik and Rob Schapire

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions: 

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software. 

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
*/

package density;

import java.util.HashMap;

public class Sample {
    int point, row, col, spatial,  spatial2, spatial3, spatial4, spatial5;
    double lat, lon;
    String name;
    public HashMap featureMap;  // maps feature names to doubles

    public Sample(int p, int r, int c, double lat, double lon, String s, int spat, int spat2, int spat3, int spat4, int spat5) { this(p,r,c,lat,lon,s,spat, spat2, spat3, spat4, spat5,null); }
    public Sample(int p, int r, int c, double lat, double lon, String s, int spat, int spat2, int spat3, int spat4, int spat5, HashMap map) {
	point = p; row=r; col=c;
	this.lat = lat;
	this.lon = lon;
    spatial = spat;
    spatial2=spat2;
    spatial3=spat3;
    spatial4=spat4;
    spatial5=spat5;
	featureMap = map;
	name = s;
    }
    public Sample(int p, HashMap map) {
        point = p;
        featureMap = map;
    }

    public Sample(int p, int r, int c, int lat, int lon, String s, int spat, int spat2, int spat3, int spat4, int spat5, HashMap map) {
    }

   /* public Sample(int p, int r, int c, int lat, int lon, String s, HashMap map) {
    }  */


    int getPoint() { return point; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    int getRow(GridDimension dim) { return dim.toRow(lat); }
    int getCol(GridDimension dim) { return dim.toCol(lon); }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getName() { return name; }
    public int getSpatial2() { return spatial2; }
    public int getSpatial3() { return spatial3; }
    public int getSpatial4() { return spatial4; }
    public int getSpatial5() { return spatial5; }
    public int getSpatial() { return spatial; }

}

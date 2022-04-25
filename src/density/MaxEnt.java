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

import javax.swing.*;
import java.io.*;
import java.util.*;

public class MaxEnt {

    static void checkVersion() {
        String version = System.getProperties().getProperty("java.version");
        double v = Double.parseDouble(version);
        System.out.println(version);
        // can't call Utils, as Utils needs 1.4 to load (prefs.Preferences)
        //	if (v < 1.4) Utils.fatalException("Java version is " + version + ", need 1.4 or later", null);
        if (v < 1.5) {
            JOptionPane.showMessageDialog(null, "Java version is " + version + ", but Maxent needs 1.5 or later.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }


    public static void main(String args[]) {
       /** input files **/
        final Params params = new Params();
        params.setOutputdirectory("D:\\maxent\\outputs");
        params.setEnvironmentallayers("D:\\maxent\\layers");
        params.setSamplesfile("D:\\maxent\\samples\\bradypus_spatial.csv");
        params.setSelections();
        Runner runner = new Runner(params);
        runner.start();
        /*SampleSet sampleSet = runner.sampleSet;
        System.out.println(sampleSet.speciesMap);
        System.out.println(sampleSet.dimension);
        System.out.println(sampleSet.header);
        System.out.println(sampleSet);
        String[] nam = sampleSet.getNames();
        System.out.println(nam);
        System.out.println(runner.cv());
*/

        /** here you can now try everything out :D **/

  /*
	splitForCV(int n) {  // returns sampleset of test data
	String[] names = sampleSet.getNames();
	SampleSet testss = new SampleSet();
	for (int i=0; i<names.length; i++) {
	    ArrayList old = (ArrayList) sampleSet.speciesMap.get(names[i]);
	    int[] order = sampleSet.randomPermutation(old.size());
	    int num = old.size() < n ? old.size() : n;
	    ArrayList[] train = new ArrayList[num];
	    ArrayList[] test = new ArrayList[num];
	    for (int j=0; j<num; j++) {
		train[j] = new ArrayList();
		test[j] = new ArrayList();
	    }
	    for (int k=0; k<old.size(); k++) {
		int fold = order[k] % num;
		test[fold].add(old.get(k));
		for (int j=0; j<num; j++)
		    if (j!=fold) train[j].add(old.get(k));
	    }
	    for (int j=0; j<num; j++) {
		sampleSet.speciesMap.put(names[i]+"_"+j, train[j]);
		testss.speciesMap.put(names[i]+"_"+j, test[j]);
	    }
	}
	return testss;
    }


*/


         runner.end();




        System.out.println(":P");
       /* params.readFromArgs(args);
        Utils.applyStaticParams(params);  // also in runner
        if (params.getboolean("visible")) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    checkVersion();

                }
            });
        }
        else {
            checkVersion();
            params.setSelections();
            Runner runner = new Runner(params);
            runner.start();
            runner.end();
        }*/

    }




}



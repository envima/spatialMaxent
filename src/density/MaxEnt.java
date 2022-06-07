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
import java.util.*;

public class MaxEnt {

    static void checkVersion() {
        String version = System.getProperties().getProperty("java.version");
        double v = Double.parseDouble(version);
        // can't call Utils, as Utils needs 1.4 to load (prefs.Preferences)
        //	if (v < 1.4) Utils.fatalException("Java version is " + version + ", need 1.4 or later", null);
        if (v < 1.5) {
            JOptionPane.showMessageDialog(null, "Java version is " + version + ", but Maxent needs 1.5 or later.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }


    public static void main(String args[]) {
        final Params params = new Params();

        params.readFromArgs(args);
        Utils.applyStaticParams(params);  // also in runner

        if (params.getboolean("visible")) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    checkVersion();
                    createAndShowGUI(params);
                }
            });
        }
        else {
            checkVersion();
            params.setSelections();
            Runner runner = new Runner(params);
            if(runner.is("fvs")){

                /**
                 * - return all possible variable combinations as arraylist
                 * - let startFvs return average test gain just define ArrayList before
                 * - create new forwardvariableselection function that calls
                 *      start function instead of maxentRun()
                 *      - integratethis forwardVariableSelection into a
                 *      start function to get neccessary features (!)
                 *      - nicht optimal, da alles zweimal geladen wird...
                 *    **/

                //ArrayList with all variables
                ArrayList<String> varNamesAL = new ArrayList<>();
                varNamesAL.addAll(List.of(params.layers));

                // ArrayList with best variable combination
                ArrayList<String> bestVariables = new ArrayList<>();
                /** pass best Variables ArrayList to function to save output **/
                runner.forwardVariableSelectionNew(varNamesAL ,bestVariables);


                if(runner.is("ffs")){
                    /**
                     * -> create start function for ffs **/

                    double bestBetaMultiplier = 0;
                    ArrayList<String> bestFeatures = new ArrayList<>();
                    runner.forwardFeatureSelection(bestVariables, bestFeatures, bestBetaMultiplier);


                    runner.startFfs(bestVariables, bestFeatures, bestBetaMultiplier);
                    runner.end();
                } else {
                    /** get best variable combination
                     * final run with variable combination**/

                    ArrayList<Double> testGain = new ArrayList<>();
                    ArrayList<Double> testAuc = new ArrayList<>();
                    runner.startFvs(bestVariables, testGain, testAuc);
                    runner.end();
                }




            } else {
                runner.start();
                runner.end();
            }


        }



    }

    private static void createAndShowGUI(Params params) {
        JFrame f = Utils.topLevelFrame = new JFrame("Maximum Entropy Species Distribution Modeling, Version " + Utils.version);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI gui = new GUI(params);
        //	f.setJMenuBar(gui.makeMenuBar());
        f.setContentPane(gui.createContentPane());
        gui.topLevelFrame = f;
        //	params.applyToInterface(gui);
        gui.applyParams();
        f.pack();
        //	f.setResizable(false);
        f.setVisible(true);
        if (params.getboolean("autoRun"))
            gui.doRun(true);
    }

}



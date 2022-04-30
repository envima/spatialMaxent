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

// works with a collection of samples.

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashSet;

public class SampleSet {
    GridDimension dimension;
    HashMap speciesMap = new HashMap();  // names to ArrayLists of Samples

    // locations in a sample file of species, x, y, env1
    public static int speciesIndex = 0, xIndex = 1, yIndex = 2, spatialIndex = 3, firstEnvVar = 4, necessaryFields = 4;
    public static void setNCEAS_FORMAT() {
	speciesIndex = 1; xIndex = 4; yIndex = 5; firstEnvVar = 7; necessaryFields = 6;
    }
    static IOException necessaryFieldsException() {
	return new IOException("Sample file needs four columns: "+
			       "species (column "+(speciesIndex+1)+"), "+
			       "longitude (column "+xIndex+"), "+
			       "latitude (column "+xIndex+"), "+
					"spatial (column "+(spatialIndex+1));
    }

    public static int NODATA_value = -9999;

    public String header;
    public SampleSet() {}

    public void write(String fileName) throws IOException {
	PrintWriter out = Utils.writer(fileName);
	Sample[] s = getSamples();
	String[] env = (s.length==0 || s[0].featureMap==null) ? new String[0] :
	    (String[]) s[0].featureMap.keySet().toArray(new String[0]);
	out.print("Species,Longitude,Latitude");
	for (int i=0; i<env.length; i++)
	    out.print("," + env[i]);
	out.println();
	for (int i=0; i<s.length; i++) {
	    out.print(s[i].name + "," + s[i].lon + "," + s[i].lat);
	    for (int j=0; j<env.length; j++)
		out.print("," + s[i].featureMap.get(env[j]));
	    out.println();
	}
	out.close();
    }

    public String[] getNames() {
	String[] result = (String[]) speciesMap.keySet().toArray(new String[0]);
	// sort with numerical suffix sorted numerically
	Arrays.sort(result, new Comparator<String>() {
			public int compare(String s1, String s2) {
			    if (s1.indexOf("_")==-1 || s2.indexOf("_")==-1)
				return s1.compareTo(s2);
			    int loc1=s1.lastIndexOf("_"), loc2=s2.lastIndexOf("_");
			    String sp1 = s1.substring(0,loc1), sp2 = s2.substring(0,loc2);
			    int c = sp1.compareTo(sp2);
			    if (c!=0) return c;
			    try {
				int n1 = Integer.parseInt(s1.substring(loc1+1));
				int n2 = Integer.parseInt(s2.substring(loc2+1));
				return n1<n2 ? -1 : n1>n2 ? 1 : 0;
			    } catch (NumberFormatException e) {
				return s1.compareTo(s2);
			    }
			}});
	return result;
    }


    public Sample[] getSamples(String s) {
	if (s==null) return getSamples();
	ArrayList a = (ArrayList) speciesMap.get(sanitizeSpeciesName(s));
	return (a==null) ? new Sample[0] : (Sample[]) a.toArray(new Sample[0]);
    }	
	
    Grid toGrid(String name) {
	final boolean[][] hasData = new boolean[dimension.nrows][dimension.ncols];
	Sample[] ss = getSamples();
	for (int i=0; i<ss.length; i++) 
	    hasData[ss[i].row][ss[i].col] = true;
	return new GridByte(dimension, name) {
		{ NODATA_value = -1; }
		public byte evalByte(int r, int c) { return 1; }
		public boolean hasData(int r, int c) { return hasData[r][c]; }
	    };
    }

    public Sample[] getSamples() {
	ArrayList union = new ArrayList();
	String[] names = getNames();
	Arrays.sort(names);
	for (int j=0; j<names.length; j++) {
	    ArrayList a = (ArrayList) speciesMap.get(names[j]);
	    for (int i=0; i<a.size(); i++) union.add(a.get(i));
	}
	return (Sample[]) union.toArray(new Sample[0]);
    }	
	
    static String sanitizeSpeciesName(String s) {
	s = s.trim().replace(' ', '_');
	s = s.replace('?', '_');
	s = s.replace('!', '_');
	s = s.replace('~', '_');
	s = s.replaceAll("\"", "");
	s = s.replace('\'', '_');
	s = s.replace('\\', '_');
	s = s.replace('/', '_');
	s = s.replace('{', '_');
	s = s.replace('}', '_');
	s = s.replace('(', '_');
	s = s.replace(')', '_');
	return s;
    }

    public void removeDuplicates(GridDimension dim) {
	for (Iterator it = speciesMap.keySet().iterator(); it.hasNext(); ) {
	    String speciesName = (String) it.next();
	    HashSet points = new HashSet();
	    ArrayList newa = new ArrayList(), olda = (ArrayList) speciesMap.get(speciesName);
	    for (int i=0; i<olda.size(); i++) {
		Sample s = (Sample) olda.get(i);
		Object o = (dim==null) ? (Object) (s.getLat() + "" + s.getLon()) : 
		    new Integer(s.getRow()*dim.ncols + s.getCol());
		if (!points.contains(o)) {
		    points.add(o);
		    newa.add(olda.get(i));
		}
	    }
	    speciesMap.put(speciesName, newa);
	}
    }

    public SampleSet(GridDimension dim, HashMap speciesMap) {
	dimension = dim;
	this.speciesMap = speciesMap;
    }	

    void removeSample(Sample s) {
	ArrayList a = (ArrayList) speciesMap.get(s.getName());
	if (!a.contains(s)) 
	    Utils.warn("SampleSet: removing non-existent sample");
	a.remove(s);
    }

    int[] randomPermutation(int ns) {
	double[] rnd = new double[ns];
	for (int j=0; j<ns; j++)
	    rnd[j] = Utils.generator.nextDouble();
	return DoubleIndexSort.sort(rnd);
    }

	 SampleSet splitForCV(int n) {  // returns sampleset of test data
	String[] names = getNames();
	SampleSet testss = new SampleSet();
	for (int i=0; i<names.length; i++) {
	    ArrayList old = (ArrayList) speciesMap.get(names[i]);
	    int[] order = randomPermutation(old.size());
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
		speciesMap.put(names[i]+"_"+j, train[j]);
		testss.speciesMap.put(names[i]+"_"+j, test[j]);
	    }
	}
	return testss;
    }







public static void main (String[] args){

	final Params params = new Params();
	params.setOutputdirectory("D:\\maxent\\outputs");
	params.setEnvironmentallayers("D:\\maxent\\layers");
	params.setSamplesfile("D:\\maxent\\samples\\bradypus_spatial.csv");
	//params.setReplicatetype("spatial crossvalidate");
	params.setSelections();

	Runner runner = new Runner(params);
	runner.start();

	// create sample Set with parameters from Params
	SampleSet sampleSet = runner.sampleSet;



	//String[] result = (String[]) sampleSet.speciesMap.keySet().toArray(new String[0]);

	//String[] result = (String[]) speciesMap.values().toArray(new String[0]);
	System.out.println(Arrays.toString(sampleSet.speciesMap.values().toArray()));
	ArrayList union = new ArrayList();
	String[] names = sampleSet.getNames();
	ArrayList a = (ArrayList) sampleSet.speciesMap.get(names[0]);
	//System.out.println(a);
	for (int i=0; i<a.size(); i++) union.add(a.get(i));
	Sample[] sampletest =	(Sample[]) union.toArray(new Sample[0]);
	//System.out.println(sampletest);

	//System.out.println(Arrays.toString(names));
	//System.out.println(sampleSet.header.toString());
	ArrayList old = (ArrayList) sampleSet.speciesMap.get("bradypus_variegatus");
	Sample s = (Sample) old.get(1);
	String t = ((Sample) old.get(1)).spatial;
	System.out.println(t);


	List<Sample> species = (List<Sample>) sampleSet.speciesMap.get("bradypus_variegatus");
	List<String> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
	//field1List.forEach(System.out::println);
	HashSet<String> locHset = new HashSet<String>(locations);
	System.out.println(locHset.size());

	// Converting HashSet to ArrayList
	List<String> locArr = new ArrayList<String>(locHset);
	System.out.println(locArr.get(1));
	System.out.println(locArr.get(2));
	System.out.println(locArr.get(0));

	/**convert HashSet back to ArrayList**/

	//System.out.println(old);
	//System.out.println(old.size());

	//System.out.println(sampleSet.header);
	//System.out.println(sampleSet.getSamples());

	//System.out.println(s.getSpatial());
	//System.out.println(sampleSet.header);
	//String headers = sampleSet.header;
	//System.out.println(headers.length());
	//Sample s2 = (Sample) old.get(2);
	//System.out.println(s.lat);
	//System.out.println(s.col);
	//System.out.println(s.name);
	//System.out.println(s.row);
	//System.out.println(s.point);
	//System.out.println(s.featureMap);
	//System.out.println(s.getSpatial());

/*
	for (int i = 0; i < old.size(); i++){

		for (int j = 0; j < old.get(i).size(); j++){
			System.out.println("Value " + old.get(i)[j] + "\n");

		}
		//printWriter.print("Length " + data.get(j). + "\n");
	}*/
	
	//int num = old.size() < 10 ? old.size() : 10;
	//System.out.println(num);
	//int[] order = sampleSet.randomPermutation(old.size());
	//System.out.println(Arrays.toString(order));
	//int fold = order[0] % num;
	//System.out.println(fold);
}



        SampleSet splitForSpatialCV() {  // returns sampleset of test data //n = number locations
            String[] names = getNames(); /**[bradypus_variegatus]**/
			SampleSet testss = new SampleSet();

			for (int i=0; i<names.length; i++) { // ist 1
				List<Sample> species = (List<Sample>) speciesMap.get(names[i]);
				List<String> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
				//field1List.forEach(System.out::println);
				HashSet<String> locHset = new HashSet<String>(locations);
				// Converting HashSet to ArrayList
				List<String> locArr = new ArrayList<String>(locHset);
				ArrayList old = (ArrayList) speciesMap.get(names[i]); //org hashmap with values


				int num = Math.min(old.size(), species.size()); //minimum ist 3
				ArrayList[] train = new ArrayList[num]; // arraylist mit je 3 elementen
				ArrayList[] test = new ArrayList[num];
				for (int j=0; j<num; j++) { // darin wird je eine neue arraylist erstellt
					train[j] = new ArrayList();
					test[j] = new ArrayList();
				} // here data gets split in cv Folds!


			/**for loop over number locations then for loop over each Sample assigning them to
			 * the fold **/
				for (int l=0; l<locHset.size(); l++) {
					for (int k = 0; k < old.size(); k++) { // k = 1:114 & num = 10
						Sample s = (Sample) old.get(k);
						String fold = s.getSpatial();
						if (fold == locArr.get(l)) {
							test[l].add(old.get(k)); // add row to array
						} else train[l].add(old.get(k)); // fold here should be: 1:3 length of spatial blocks = 3
					}
				}

			/**add names to  each test and train set**/
			for (int j=0; j<num; j++) {
				speciesMap.put(names[i]+"_"+j, train[j]);
				testss.speciesMap.put(names[i]+"_"+j, test[j]);
			}
		}
		return testss;
	}




    void replicate(int n, boolean bootstrap) {
	String[] names = getNames();
	for (int i=0; i<names.length; i++) {
	    ArrayList old = (ArrayList) speciesMap.get(names[i]);
	    for (int j=0; j<n; j++) {
		ArrayList a = new ArrayList();
		for (int k=0; k<old.size(); k++)
		    a.add(old.get(bootstrap ?
				  Utils.generator.nextInt(old.size()) : 
				  k));
		speciesMap.put(names[i]+"_"+j, a);
	    }
	}
    }

    // take a random sample of each species.  Destructive.
    SampleSet randomSample(int percentTestPointsPerSpecies) {
	HashMap rnd = new HashMap();
	String[] names = getNames();
	for (int i=0; i<names.length; i++) {
	    ArrayList old = (ArrayList) speciesMap.get(names[i]);
	    ArrayList a = new ArrayList();
	    int toRemove = (int) ((percentTestPointsPerSpecies*old.size()) / 100.0);
	    for (int j=0; j<toRemove; j++)
		if (old.size() != 0) {
		    int sel = (int) (Utils.generator.nextDouble()*old.size());
		    a.add(old.get(sel));
		    old.remove(sel);
		}
	    if (old.size() != 0)
		rnd.put(names[i], a);
	}
	return new SampleSet(dimension, rnd);
    }
	    
    // randomly permute, then greedily keep points at least mindist apart
    public SampleSet spatialFilter(double mindist) {
	HashMap filtered = new HashMap();
	String[] names = getNames();
	for (int i=0; i<names.length; i++) {
	    ArrayList old = (ArrayList) speciesMap.get(names[i]);
	    int[] order = randomPermutation(old.size());
	    ArrayList a = new ArrayList();
	    for (int j=0; j<old.size(); j++) {
		boolean close = false;
		Sample s = (Sample) old.get(order[j]);
		for (int k=0; k<a.size(); k++)
		    if (dist(s, (Sample) a.get(k)) < mindist) {
			close = true;
			break;
		    }
		if (!close)
		    a.add(s);
	    }
	    filtered.put(names[i], a);
	}
	return new SampleSet(dimension, filtered);
    }

    double dist(Sample s1, Sample s2) {
	double dlat = s1.getLat() - s2.getLat(), dlon = s1.getLon() - s2.getLon();
	return Math.sqrt(dlat*dlat + dlon*dlon);
    }


}

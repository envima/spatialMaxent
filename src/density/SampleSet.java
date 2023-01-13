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
import com.google.common.collect.Sets;

public class SampleSet {
	GridDimension dimension;
	HashMap speciesMap = new HashMap();  // names to ArrayLists of Samples

	// locations in a sample file of species, x, y, env1
	public static int speciesIndex = 0, xIndex = 1, yIndex = 2, spatialIndex = 3, firstEnvVar = 4, necessaryFields = 4;

	public static void setNCEAS_FORMAT() {
		speciesIndex = 1;
		xIndex = 4;
		yIndex = 5;
		firstEnvVar = 7;
		necessaryFields = 6;
	}

	static IOException necessaryFieldsException() {
		return new IOException("Sample file needs four columns: " +
				"species (column " + (speciesIndex + 1) + "), " +
				"longitude (column " + xIndex + "), " +
				"latitude (column " + xIndex + "), " +
				"spatial (column " + (spatialIndex + 1));
	}

	public static int NODATA_value = -9999;

	public String header;

	public SampleSet() {
	}

	public void write(String fileName) throws IOException {
		PrintWriter out = Utils.writer(fileName);
		Sample[] s = getSamples();
		String[] env = (s.length == 0 || s[0].featureMap == null) ? new String[0] :
				(String[]) s[0].featureMap.keySet().toArray(new String[0]);
		out.print("Species,Longitude,Latitude");
		for (int i = 0; i < env.length; i++)
			out.print("," + env[i]);
		out.println();
		for (int i = 0; i < s.length; i++) {
			out.print(s[i].name + "," + s[i].lon + "," + s[i].lat);
			for (int j = 0; j < env.length; j++)
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
				if (s1.indexOf("_") == -1 || s2.indexOf("_") == -1)
					return s1.compareTo(s2);
				int loc1 = s1.lastIndexOf("_"), loc2 = s2.lastIndexOf("_");
				String sp1 = s1.substring(0, loc1), sp2 = s2.substring(0, loc2);
				int c = sp1.compareTo(sp2);
				if (c != 0) return c;
				try {
					int n1 = Integer.parseInt(s1.substring(loc1 + 1));
					int n2 = Integer.parseInt(s2.substring(loc2 + 1));
					return n1 < n2 ? -1 : n1 > n2 ? 1 : 0;
				} catch (NumberFormatException e) {
					return s1.compareTo(s2);
				}
			}
		});
		return result;
	}


	public Sample[] getSamples(String s) {
		if (s == null) return getSamples();
		ArrayList a = (ArrayList) speciesMap.get(sanitizeSpeciesName(s));
		return (a == null) ? new Sample[0] : (Sample[]) a.toArray(new Sample[0]);
	}

	Grid toGrid(String name) {
		final boolean[][] hasData = new boolean[dimension.nrows][dimension.ncols];
		Sample[] ss = getSamples();
		for (int i = 0; i < ss.length; i++)
			hasData[ss[i].row][ss[i].col] = true;
		return new GridByte(dimension, name) {
			{
				NODATA_value = -1;
			}

			public byte evalByte(int r, int c) {
				return 1;
			}

			public boolean hasData(int r, int c) {
				return hasData[r][c];
			}
		};
	}

	public Sample[] getSamples() {
		ArrayList union = new ArrayList();
		String[] names = getNames();
		Arrays.sort(names);
		for (int j = 0; j < names.length; j++) {
			ArrayList a = (ArrayList) speciesMap.get(names[j]);
			for (int i = 0; i < a.size(); i++) union.add(a.get(i));
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
			for (int i = 0; i < olda.size(); i++) {
				Sample s = (Sample) olda.get(i);
				Object o = (dim == null) ? (Object) (s.getLat() + "" + s.getLon()) :
						new Integer(s.getRow() * dim.ncols + s.getCol());
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

	static int[] randomPermutation(int ns) {
		double[] rnd = new double[ns];
		for (int j = 0; j < ns; j++)
			rnd[j] = Utils.generator.nextDouble();
		return DoubleIndexSort.sort(rnd);
	}

	SampleSet splitForCV(int n) {  // returns sampleset of test data
		String[] names = getNames();
		SampleSet testss = new SampleSet();
		for (int i = 0; i < names.length; i++) {
			ArrayList old = (ArrayList) speciesMap.get(names[i]);
			int[] order = randomPermutation(old.size());
			int num = old.size() < n ? old.size() : n;
			ArrayList[] train = new ArrayList[num];
			ArrayList[] test = new ArrayList[num];
			for (int j = 0; j < num; j++) {
				train[j] = new ArrayList();
				test[j] = new ArrayList();
			}
			for (int k = 0; k < old.size(); k++) {
				int fold = order[k] % num; // number 0:9
				test[fold].add(old.get(k)); // add ONE set to test
				for (int j = 0; j < num; j++)
					if (j != fold) train[j].add(old.get(k));
			}
			for (int j = 0; j < num; j++) {
				speciesMap.put(names[i] + "_" + j, train[j]);
				testss.speciesMap.put(names[i] + "_" + j, test[j]);
			}
		}
		return testss;
	}


	public static void main(String[] args) {

		final Params params = new Params();
		params.setOutputdirectory("D:\\Natur40\\FFSCV\\Pyrisitia_lisa\\data\\output\\test_spatialMaxent");
		params.setEnvironmentallayers("D:\\Natur40\\FFSCV\\SA\\layer");
		params.setSamplesfile("D:\\Natur40\\FFSCV\\SA\\samples\\sa04_01spatialFolds_fold10.csv");
		//params.setReplicatetype("spatial-crossvalidate");
		params.setSelections();

		Runner runner = new Runner(params);
		runner.prepFFME();
		// create sample Set with parameters from Params

		SampleSet sampleSet = runner.sampleSet;

		//String[] names = sampleSet.getNames();
		//System.out.println(Arrays.toString(names));
		System.out.println(sampleSet.header);
		//System.out.println(sampleSet.speciesMap);
		//System.out.println(sampleSet.dimension);


		/**
		 *  start splitspatial cv
		 * **/
		//	SampleSet splitTripleFfscv() {  // returns sampleset of test data //n = number locations
		String[] names = sampleSet.getNames();
		//System.out.println(names);

		SampleSet testss = new SampleSet();

		//	for (int i=0; i<names.length; i++) { // ist 1
		int i = 0;
		/**        NUMBER OF FOLDS FOR 2 var combinations**/
		List<Sample> species = (List<Sample>) sampleSet.speciesMap.get(names[i]);
		List<Integer> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
		ArrayList<Integer> locationsArrList = (ArrayList<Integer>) locations;
		ArrayList<Integer> order = (ArrayList<Integer>) locations;

		//field1List.forEach(System.out::println);
		HashSet<Integer> locHset = new HashSet<Integer>(locations);
		// Converting HashSet to ArrayList
		List<Integer> locArr = new ArrayList<Integer>(locHset);
		ArrayList<Integer> positions = new ArrayList<>();
		for (int p = 0; p < locArr.size(); p++) {
			positions.add(p);
		}

		//combinations for ffme
		// create guava Sets with all possible combinations of var Integer
		Set<Set<Integer>> comb = Sets.combinations(Sets.newHashSet(locArr), 2);
		// create empty array
		Integer[][] allComb = new Integer[comb.size()][2];

		//add values from Set<Set<Integer>> to Array
		for (int c = 0; c < comb.size(); c++) {
			//get one set
			Set<Integer> arr = comb.stream().collect(Collectors.toList()).get(c);
			// get each element of set
			allComb[c][0] = arr.stream().collect(Collectors.toList()).get(0);
			allComb[c][1] = arr.stream().collect(Collectors.toList()).get(1);
		} // END combinations for FFME

		ArrayList old = (ArrayList) sampleSet.speciesMap.get(names[i]); //org hashmap with values
		//System.out.println(old);

		/**        NUMBER OF FOLDS FOR 3 var combinations**/
		Set<Set<Integer>> comb3 = Sets.combinations(Sets.newHashSet(locArr), 3);
		//System.out.println(comb3);


		// create empty array
		Integer[][] allComb3 = new Integer[comb3.size()][3];
		System.out.println(comb3.size());
		System.out.println(Arrays.deepToString(allComb3));
		//add values from Set<Set<Integer>> to Array
		for (int c = 0; c < comb3.size(); c++) {
			//get one set
			Set<Integer> arr = comb3.stream().collect(Collectors.toList()).get(c);
			// get each element of set
			allComb3[c][0] = arr.stream().collect(Collectors.toList()).get(0);
			allComb3[c][1] = arr.stream().collect(Collectors.toList()).get(1);
			allComb3[c][2] = arr.stream().collect(Collectors.toList()).get(2);
		} // END combinations for FFME
		System.out.println(Arrays.deepToString(allComb3));
		System.out.println(Arrays.deepToString(allComb));
		System.out.println(allComb.length);
		System.out.println(allComb3.length);
		int num = allComb.length  + allComb3.length; //Anzahl der ffme cv runs (folds)
		System.out.println(num);

		ArrayList[] train = new ArrayList[num]; // arraylist mit je num elementen
		ArrayList[] test = new ArrayList[num];
		for (int j = 0; j < num; j++) { // darin wird je eine neue arraylist erstellt
			train[j] = new ArrayList();
			test[j] = new ArrayList();
		}

		/**
		 * EINTEILUNG DER FOLDS
		 * **/
		// 2 fold combination cross-validation:
		for (int fold = 0; fold < allComb.length; fold++) {
			ArrayList<Integer> valPosition = new ArrayList<>();
			ArrayList<Integer> trainPosition = new ArrayList<>();

			for (int f = 0; f < order.size(); f++) {
				int cvFold = order.get(f);
				int testFold1 = allComb[fold][0];// index 0 needs to be chnaged
				int testFold2 = allComb[fold][1];//index 0 needs to be changed
				if (cvFold == testFold1 || cvFold == testFold2) {
					valPosition.add(f);
				} else trainPosition.add(f);
			}
			// here data gets split in cv Folds!
			for (int k = 0; k < old.size(); k++) {
				//int k =0;

				//int[][] k2 = new int[1][2];
				if (valPosition.contains(k)) {
					test[fold].add(old.get(k));
				} else {
					train[fold].add(old.get(k));
				}
			}
			// ende zuordnung ein fold in ffme
		}// end loop over folds of ffme
/** 3 Fold combination cross-validation **/
		for (int fold = 0; fold < allComb3.length; fold++) {
			ArrayList<Integer> valPosition = new ArrayList<>();
			ArrayList<Integer> trainPosition = new ArrayList<>();

			for (int f = 0; f < order.size(); f++) {
				int cvFold = order.get(f);
				int testFold1 = allComb3[fold][0];// index 0 needs to be chnaged
				int testFold2 = allComb3[fold][1];//index 0 needs to be changed
				int testFold3 = allComb3[fold][2];//index 0 needs to be changed
				if (cvFold == testFold1 || cvFold == testFold2 || cvFold == testFold3) {
					valPosition.add(f);
				} else trainPosition.add(f);
			}
			// here data gets split in cv Folds!
			for (int k = 0; k < old.size(); k++) {
				//int k =0;

				int fold2 = allComb.length + fold;
				//int[][] k2 = new int[1][2];
				if (valPosition.contains(k)) {
					test[fold2].add(old.get(k));
				} else {
					train[fold2].add(old.get(k));
				}
			}
			// ende zuordnung ein fold in ffme
		}// end loop over folds of ffme

		for (int j = 0; j < num; j++) { //3
			sampleSet.speciesMap.put(names[i] + "_" + j, train[j]);
			testss.speciesMap.put(names[i] + "_" + j, test[j]);
		}
		//}
		//	return testss;
System.out.println(sampleSet.speciesMap.keySet());
System.out.println(testss.speciesMap.keySet());
	}


	SampleSet splitCombinedFfscv() {  // returns sampleset of test data //n = number locations

		String[] names = getNames();
		//System.out.println(names);

		SampleSet testss = new SampleSet();

			for (int i=0; i<names.length; i++) { // ist 1
		//int i = 0;
		/**        NUMBER OF FOLDS FOR 2 var combinations**/
		List<Sample> species = (List<Sample>) speciesMap.get(names[i]);
		List<Integer> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
		ArrayList<Integer> locationsArrList = (ArrayList<Integer>) locations;
		ArrayList<Integer> order = (ArrayList<Integer>) locations;

		//field1List.forEach(System.out::println);
		HashSet<Integer> locHset = new HashSet<Integer>(locations);
		// Converting HashSet to ArrayList
		List<Integer> locArr = new ArrayList<Integer>(locHset);
		ArrayList<Integer> positions = new ArrayList<>();
		for (int p = 0; p < locArr.size(); p++) {
			positions.add(p);
		}

		//combinations for ffme
		// create guava Sets with all possible combinations of var Integer
		Set<Set<Integer>> comb = Sets.combinations(Sets.newHashSet(locArr), 2);
		// create empty array
		Integer[][] allComb = new Integer[comb.size()][2];

		//add values from Set<Set<Integer>> to Array
		for (int c = 0; c < comb.size(); c++) {
			//get one set
			Set<Integer> arr = comb.stream().collect(Collectors.toList()).get(c);
			// get each element of set
			allComb[c][0] = arr.stream().collect(Collectors.toList()).get(0);
			allComb[c][1] = arr.stream().collect(Collectors.toList()).get(1);
		} // END combinations for FFME

		ArrayList old = (ArrayList) speciesMap.get(names[i]); //org hashmap with values
		//System.out.println(old);

		/**        NUMBER OF FOLDS FOR 3 var combinations**/
		Set<Set<Integer>> comb3 = Sets.combinations(Sets.newHashSet(locArr), 3);
		//System.out.println(comb3);


		// create empty array
		Integer[][] allComb3 = new Integer[comb3.size()][3];

		//add values from Set<Set<Integer>> to Array
		for (int c = 0; c < comb3.size(); c++) {
			//get one set
			Set<Integer> arr = comb3.stream().collect(Collectors.toList()).get(c);
			// get each element of set
			allComb3[c][0] = arr.stream().collect(Collectors.toList()).get(0);
			allComb3[c][1] = arr.stream().collect(Collectors.toList()).get(1);
			allComb3[c][2] = arr.stream().collect(Collectors.toList()).get(2);
		} // END combinations for FFME

		int num = allComb.length  + allComb3.length; //Anzahl der ffme cv runs (folds)


		ArrayList[] train = new ArrayList[num]; // arraylist mit je num elementen
		ArrayList[] test = new ArrayList[num];
		for (int j = 0; j < num; j++) { // darin wird je eine neue arraylist erstellt
			train[j] = new ArrayList();
			test[j] = new ArrayList();
		}

		/**
		 * EINTEILUNG DER FOLDS
		 * **/
		// 2 fold combination cross-validation:
		for (int fold = 0; fold < allComb.length; fold++) {
			ArrayList<Integer> valPosition = new ArrayList<>();
			ArrayList<Integer> trainPosition = new ArrayList<>();

			for (int f = 0; f < order.size(); f++) {
				int cvFold = order.get(f);
				int testFold1 = allComb[fold][0];// index 0 needs to be chnaged
				int testFold2 = allComb[fold][1];//index 0 needs to be changed
				if (cvFold == testFold1 || cvFold == testFold2) {
					valPosition.add(f);
				} else trainPosition.add(f);
			}
			// here data gets split in cv Folds!
			for (int k = 0; k < old.size(); k++) {
				//int k =0;

				//int[][] k2 = new int[1][2];
				if (valPosition.contains(k)) {
					test[fold].add(old.get(k));
				} else {
					train[fold].add(old.get(k));
				}
			}
			// ende zuordnung ein fold in ffme
		}// end loop over folds of ffme
/** 3 Fold combination cross-validation **/
		for (int fold = 0; fold < allComb3.length; fold++) {
			ArrayList<Integer> valPosition = new ArrayList<>();
			ArrayList<Integer> trainPosition = new ArrayList<>();

			for (int f = 0; f < order.size(); f++) {
				int cvFold = order.get(f);
				int testFold1 = allComb3[fold][0];// index 0 needs to be chnaged
				int testFold2 = allComb3[fold][1];//index 0 needs to be changed
				int testFold3 = allComb3[fold][2];//index 0 needs to be changed
				if (cvFold == testFold1 || cvFold == testFold2 || cvFold == testFold3) {
					valPosition.add(f);
				} else trainPosition.add(f);
			}
			// here data gets split in cv Folds!
			for (int k = 0; k < old.size(); k++) {
				//int k =0;

				int fold2 = allComb.length + fold;
				//int[][] k2 = new int[1][2];
				if (valPosition.contains(k)) {
					test[fold2].add(old.get(k));
				} else {
					train[fold2].add(old.get(k));
				}
			}
			// ende zuordnung ein fold in ffme
		}// end loop over folds of ffme

		for (int j = 0; j < num; j++) { //3
			speciesMap.put(names[i] + "_" + j, train[j]);
			testss.speciesMap.put(names[i] + "_" + j, test[j]);
		}
		}
			return testss;
}


/**
 *
 * METHOD TO SPLIT SAMPLES IN ALL POSSIBLE 3 FOLD COMBINATIONS
 *
 * **/
	SampleSet splitFfscv3Comb() {  // returns sampleset of test data //n = number locations

		String[] names = getNames();
		//System.out.println(names);

		SampleSet testss = new SampleSet();

		for (int i=0; i<names.length; i++) { // ist 1
			//int i = 0;
			/**        NUMBER OF FOLDS FOR 2 var combinations**/
			List<Sample> species = (List<Sample>) speciesMap.get(names[i]);
			List<Integer> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
			ArrayList<Integer> locationsArrList = (ArrayList<Integer>) locations;
			ArrayList<Integer> order = (ArrayList<Integer>) locations;

			//field1List.forEach(System.out::println);
			HashSet<Integer> locHset = new HashSet<Integer>(locations);
			// Converting HashSet to ArrayList
			List<Integer> locArr = new ArrayList<Integer>(locHset);
			ArrayList<Integer> positions = new ArrayList<>();
			for (int p = 0; p < locArr.size(); p++) {
				positions.add(p);
			}

			ArrayList old = (ArrayList) speciesMap.get(names[i]); //org hashmap with values
			//System.out.println(old);

			/**        NUMBER OF FOLDS FOR 3 var combinations**/
			Set<Set<Integer>> comb3 = Sets.combinations(Sets.newHashSet(locArr), 3);
			//System.out.println(comb3);


			// create empty array
			Integer[][] allComb3 = new Integer[comb3.size()][3];

			//add values from Set<Set<Integer>> to Array
			for (int c = 0; c < comb3.size(); c++) {
				//get one set
				Set<Integer> arr = comb3.stream().collect(Collectors.toList()).get(c);
				// get each element of set
				allComb3[c][0] = arr.stream().collect(Collectors.toList()).get(0);
				allComb3[c][1] = arr.stream().collect(Collectors.toList()).get(1);
				allComb3[c][2] = arr.stream().collect(Collectors.toList()).get(2);
			} // END combinations for FFME

			int num =  allComb3.length; //Anzahl der ffme cv runs (folds)


			ArrayList[] train = new ArrayList[num]; // arraylist mit je num elementen
			ArrayList[] test = new ArrayList[num];
			for (int j = 0; j < num; j++) { // darin wird je eine neue arraylist erstellt
				train[j] = new ArrayList();
				test[j] = new ArrayList();
			}

/** 3 Fold combination cross-validation **/
			for (int fold = 0; fold < allComb3.length; fold++) {
				ArrayList<Integer> valPosition = new ArrayList<>();
				ArrayList<Integer> trainPosition = new ArrayList<>();

				for (int f = 0; f < order.size(); f++) {
					int cvFold = order.get(f);
					int testFold1 = allComb3[fold][0];// index 0 needs to be chnaged
					int testFold2 = allComb3[fold][1];//index 0 needs to be changed
					int testFold3 = allComb3[fold][2];//index 0 needs to be changed
					if (cvFold == testFold1 || cvFold == testFold2 || cvFold == testFold3) {
						valPosition.add(f);
					} else trainPosition.add(f);
				}
				// here data gets split in cv Folds!
				for (int k = 0; k < old.size(); k++) {
					//int k =0;

					int fold2 =  fold;
					//int[][] k2 = new int[1][2];
					if (valPosition.contains(k)) {
						test[fold2].add(old.get(k));
					} else {
						train[fold2].add(old.get(k));
					}
				}
				// ende zuordnung ein fold in ffme
			}// end loop over folds of ffme

			for (int j = 0; j < num; j++) { //3
				speciesMap.put(names[i] + "_" + j, train[j]);
				testss.speciesMap.put(names[i] + "_" + j, test[j]);
			}
		}
		return testss;
	}




	SampleSet splitFfscv() {  // returns sampleset of test data //n = number locations
		String[] names = getNames();
		//System.out.println(names);

		SampleSet testss = new SampleSet();

		for (int i=0; i<names.length; i++) { // ist 1
			List<Sample> species = (List<Sample>) speciesMap.get(names[i]);
			List<Integer> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
			ArrayList<Integer> order = (ArrayList<Integer>) locations;

			//field1List.forEach(System.out::println);
			HashSet<Integer> locHset = new HashSet<Integer>(locations);
			// Converting HashSet to ArrayList
			List<Integer> locArr = new ArrayList<Integer>(locHset);

			//combinations for ffme
			// create guava Sets with all possible combinations of var Integer
			Set<Set<Integer>> comb = Sets.combinations(Sets.newHashSet(locArr), 2);
			// create empty array
			Integer[][] allComb = new Integer[comb.size()][2];

			//add values from Set<Set<Integer>> to Array
			for (int c = 0; c < comb.size(); c++) {
				//get one set
				Set<Integer> arr = comb.stream().collect(Collectors.toList()).get(c);
				// get each element of set
				allComb[c][0] = arr.stream().collect(Collectors.toList()).get(0);
				allComb[c][1] = arr.stream().collect(Collectors.toList()).get(1);
			} // END combinations for FFME

			ArrayList old = (ArrayList) speciesMap.get(names[i]); //org hashmap with values
			//System.out.println(old);

			int num = allComb.length; //Anzahl der ffme cv runs (folds)
			//	int num = Math.min(old.size(), locArr.size()); //Anzahl der cv runs (folds)
			//System.out.println(num);

			ArrayList[] train = new ArrayList[num]; // arraylist mit je num elementen
			ArrayList[] test = new ArrayList[num];
			for (int j = 0; j < num; j++) { // darin wird je eine neue arraylist erstellt
				train[j] = new ArrayList();
				test[j] = new ArrayList();
			}

			for (int fold = 0; fold < allComb.length; fold++) {
				ArrayList<Integer> valPosition = new ArrayList<>();
				ArrayList<Integer> trainPosition = new ArrayList<>();

				for (int f = 0; f < order.size(); f++) {
					int cvFold = order.get(f);
					int testFold1 = allComb[fold][0];// index 0 needs to be chnaged
					int testFold2 = allComb[fold][1];//index 0 needs to be changed
					if (cvFold == testFold1 || cvFold == testFold2) {
						valPosition.add(f);
					} else trainPosition.add(f);
				}
				// here data gets split in cv Folds!
				for (int k = 0; k < old.size(); k++) {
					//int k =0;

					//int[][] k2 = new int[1][2];
					if (valPosition.contains(k)) {
						test[fold].add(old.get(k));
					} else {
						train[fold].add(old.get(k));
					}
				}
				// ende zuordnung ein fold in ffme
			}// end loop over folds of ffme

			for (int j = 0; j < allComb.length; j++) { //3
				speciesMap.put(names[i] + "_" + j, train[j]);
				testss.speciesMap.put(names[i] + "_" + j, test[j]);
			}
		}
		return testss;
	}

        SampleSet splitForSpatialCV() {  // returns sampleset of test data //n = number locations
            String[] names = getNames();
			SampleSet testss = new SampleSet();

			for (int i=0; i<names.length; i++) { // ist 1
				List<Sample> species = (List<Sample>) speciesMap.get(names[i]);
				List<Integer> locations = species.stream().map(Sample::getSpatial).collect(Collectors.toList());
				ArrayList<Integer> locationsArrList = (ArrayList<Integer>) locations;
				//field1List.forEach(System.out::println);
				HashSet<Integer> locHset = new HashSet<Integer>(locations);
				// Converting HashSet to ArrayList
				List<Integer> locArr = new ArrayList<Integer>(locHset);
				ArrayList old = (ArrayList) speciesMap.get(names[i]); //org hashmap with values
				ArrayList<Integer> positions = new ArrayList<>();
				for(int p =0; p<locArr.size(); p++){
					positions.add(p);
				}


				int num = Math.min(old.size(), locArr.size()); //minimum ist 3
				ArrayList[] train = new ArrayList[num]; // arraylist mit je 3 elementen
				ArrayList[] test = new ArrayList[num];
				for (int j=0; j<num; j++) { // darin wird je eine neue arraylist erstellt
					train[j] = new ArrayList();
					test[j] = new ArrayList();
				} // here data gets split in cv Folds!

				// number of all points (114)
				for (int k=0; k<old.size(); k++) {

					int value = locationsArrList.get(k);
					int index = locArr.indexOf(value);
					int fold = positions.get(index); // number 0:9


					test[fold].add(old.get(k)); // add ONE set to test
					for (int j=0; j<num; j++)
						if (j!=fold) train[j].add(old.get(k));
				}

			for (int j=0; j<num; j++) { //3
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

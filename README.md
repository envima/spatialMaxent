## spatialMaxent

### What is it?
spatialMaxent is an extension for `maxent version 3.4.4` (Copyright 2016 Steven Phillips, Miro Dudik and Rob Schapire), that provides a Forward-Variables-Selection (FVS), Forward-Feature-Selection (FFS) and tuning of the beta multiplier together with a Leave-Location-Out (LLO) cross-validation. These methods are especially suited for spatial data.

### How to run it?

The spatialMaxent extension requires a Java 18 (or newer) environment to run. If you are on windows you can then download the jar and the batch file [here] and just run the batch file. It can be used as maxent version 3.4.4 via the command line or the GUI. 

### How to structure your data?

The data structure that needs to be supplied to maxent changes insofar as it is necessary to provide the samples file with a fourth column containing the association of each point with a spatial fold as integer value. If the SWD data format is choosen also the environmental layers .csv file needs to have an additional column for the folds, but no values are required here.

Example of a samples file created to work with spatialMaxent on the [maxent tutorial data](https://biodiversityinformatics.amnh.org/open_source/maxent/):

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings3.png)

To create the spatial folds you could for example have a look at the [blockCV R package](https://doi.org/10.1111/2041-210X.13107) by Valavi et al. (2019).
___

### Spatial validation, variable selection and parameter tuning in one go:
#### Spatial Validation

The spatial validation can be chosen in the basic tab as `Replicated run type`. If the `spatial crossvalidate` option is used the setting of `replicates` will have no effect anymore as the number of replicates will be set to the number of distinct folds in the sample data. 

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings1.png)



All the tuning procedures can be found in the GUI in a new tab of the settings called `spatial`. Each of the steps variable selection, feature selection and beta multiplier tuning can be omitted but we highly recommend to do all of them.

If you are working on the command line these are the parameters to use:
|**Name**|**Type**|**Default**|
| --------- |:--------:| -----:|
|fvs| boolean| true|
|ffs| boolean| true|
|tuneBeta|boolean| true|
|betaStart| double| 0.5|
|betaEnd| double| 7|
|betaStep| double| 0.5|
|allModels|boolean|false|



#### Forward Variable Selection (FVS)

The `Forward Variable Selection` as designed by [Meyer et al. (2018)](https://doi.org/10.1016/j.envsoft.2017.12.001) is a variable selection method particularly designed for spatial data. FFS trains all possible 2 variable combinations, choses the best one and then trains these two variables together with each of the remaining variables and repeats this step until the model show no increase in performance.

#### Forward Feature Selection (FFS)
The `Forward Feature Selection` follows the same structure as the FVS. It trains one model with each feature (hinge, linear, threshold, product, quadratic). Then the best one is chosen and the other features are tested in combination with it until no in crease in model performance is observed. Note that the setting of features in the main maxent tab (`Auto features`, `Hinge features`, etc.) will be ignored by spatialMaxent if a FFS is done as all features are tested one after another.

#### Beta multiplier tuning
The beta multiplier tuning has three input parameters: The lowest beta multiplier to be tuned (beta start) the highest beta multiplier to be tuned (beta end) and the steps in which the beta multiplier is increased from betastart to the highest beta multiplier (beta step). e.g.(beta start=1,beta end=5 and beta step=0.5 will try the following beta multipliers: 1 1.5 2 2.5 3 3.5 4 4.5 5).

Which model is the best one can be either determined based on the `test gain` or the `test auc` value (`decision parameter`).

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings2.png)

#### The `allModels` setting
You can set the `allModels`setting to true if you want to generate output not only for the last model with the selected variables, features and beta multiplier but for each step of the FFS, FVS and beta multiplier tuning. **Be very careful with this setting!** If it is set to true depending on your input data huge amounts of data will be generated and the processing time will increase considerably. It is not recommended for the output of large rasters and high amount of variables.


#### general notes
The FFS, FVS and beta multiplier tuning all have large processing times. Especially if the FVS is done and there are lots of input variables alone in the first step of the FFS this can lead to a large amount of models to be trained if the input consists of 10 variables 45 models are trained in the first step of the fvs if it consists of 20 variables it increases to 190 models. Just keep in mind, that the whole process can take a while.
 
___

### References

Meyer, H., Reudenbach, C., Hengl, T., Katurji, M., & Nauss, T. (2018). Improving performance of spatio-temporal machine learning models using forward feature selection and target-oriented validation. Environmental Modelling & Software, 101, 1–9. https://doi.org/10.1016/j.envsoft.2017.12.001

Meyer, H., Reudenbach, C., Wöllauer, S., & Nauss, T. (2019). Importance of spatial predictor variable selection in machine learning applications – Moving from data reproduction to spatial prediction. Ecological Modelling, 411, 108815. https://doi.org/10.1016/j.ecolmodel.2019.108815

Phillips, S. J., Anderson, R. P., & Schapire, R. E. (2006). Maximum entropy modeling of species geographic distributions. Ecological Modelling, 190(3–4), 231–259. https://doi.org/10.1016/j.ecolmodel.2005.03.026

Valavi, R., Elith, J., Lahoz‐Monfort, J. J., & Guillera‐Arroita, G. (2019). block CV: An r package for generating spatially or environmentally separated folds for k‐fold cross‐validation of species distribution models. Methods in Ecology and Evolution, 10(2), 225–232. https://doi.org/10.1111/2041-210X.13107

### spatialMaxent was created using:

maxent version 3.4.4 Copyright (c) 2016 Steven Phillips, Miro Dudik and Rob Schapire

OpenJDK JDK18

IntelliJ IDEA Community Edition 2021.3.3

Google Core Libraries for Java: Guava 31.1

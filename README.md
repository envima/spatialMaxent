## spatialMaxent

* [Download spatialMaxent](https://github.com/Nature40/spatialMaxent/raw/main/out/artifacts/spatialMaxent_jar.zip)
* [Tutorial](https://nature40.github.io/spatialMaxentPaper/)

## Short Introduction

### What is it?
spatialMaxent is an extension for `Maxent version 3.4.4` (Copyright 2016 Steven Phillips, Miro Dudik and Rob Schapire), that provides a Forward-Variables-Selection (FVS), Forward-Feature-Selection (FFS) and tuning of the regularization multiplier together with spatial cross-validation. These methods are especially suited for spatial data. A Tutorial on spatialMaxent can be found [here](https://nature40.github.io/spatialMaxentPaper/).

### How to run it?

The spatialMaxent extension requires a Java 18 (or newer) environment to run. If you are on windows you can then download the jar and the batch file [here](https://github.com/Nature40/spatialMaxent/raw/main/out/artifacts/spatialMaxent_jar.zip) and just run the batch file. It can be used as Maxent version 3.4.4 via the command line or the GUI. 

### How to structure your data?

The data structure that needs to be supplied to maxent changes insofar as it is necessary to provide the samples file with a fourth column containing the association of each point with a spatial fold as integer value. If the SWD data format is chosen also the environmental layers .csv file needs to have an additional column for the folds, but no values are required here. Note that in spatialMaxent contrary to Maxent the processing of several species at once from the same samples file is not supported.

Example of a samples file created to work with spatialMaxent on the [maxent tutorial data](https://biodiversityinformatics.amnh.org/open_source/maxent/):

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings3.png)

To create the spatial folds you could for example have a look at the [blockCV R package](https://doi.org/10.1111/2041-210X.13107) by Valavi et al. (2019).
___

### Spatial validation, variable selection and parameter tuning in one go:
#### Spatial Validation

The spatial validation can be chosen in the basic tab as `Replicated run type`. If the `spatial crossvalidate` option is used the setting of `replicates` will have no effect anymore as the number of replicates will be set to the number of distinct folds in the sample data. 

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings1.png)



All the tuning procedures can be found in the GUI in a new tab of the settings called `spatial`. Each of the steps variable selection, feature selection and regularization multiplier tuning can be omitted but we highly recommend to do all of them.

If you are working on the command line these are the parameters to use:
|**Name**|**Type**|**Default**|
| --------- |:--------:| -----:|
|fvs| boolean| true|
|ffs| boolean| true|
|tuneRM|boolean| true|
|RMMin| double| 0.5|
|RMMax| double| 7|
|RMIncrease| double| 0.5|
|cvGrids|boolean|false|
|finalModel|boolean|true|




#### Forward Variable Selection (FVS)

The `Forward Variable Selection` as designed by [Meyer et al. (2018)](https://doi.org/10.1016/j.envsoft.2017.12.001) is a variable selection method particularly designed for spatial data. FFS trains all possible 2 variable combinations, choses the best one and then trains these two variables together with each of the remaining variables and repeats this step until the model show no increase in performance. FFS will be run in parallel if threads>1.

#### Forward Feature Selection (FFS)
The `Forward Feature Selection` follows the same structure as the FVS. It trains one model with each feature (hinge, linear, threshold, product, quadratic). Then the best one is chosen and the other features are tested in combination with it until no increase in model performance is observed. Note that the setting of features in the main maxent tab (`Auto features`, `Hinge features`, etc.) will be ignored by spatialMaxent if a FFS is done as all features are tested one after another.

#### Regularization multiplier tuning
The regularization multiplier tuning has three input parameters: The lowest regularization multiplier to be tuned the highest regularization multiplier to be tuned and the steps in which the regularization multiplier is increased from the minimum regularization multiplier to the highest regularization multiplier. e.g. (RMMin=1,RMMax=5 and RMIncrease=0.5 will try the following regularization multipliers: 1 1.5 2 2.5 3 3.5 4 4.5 5).

Which model is the best one can be either determined based on the `test gain` or the `test auc` value (`decision parameter`).

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings2.png)

#### Optional output of summarized grids
With the setting `cvGrids ` it is possible to omit the output of the summarized html page and grids of cross-validation or spatial cross-validation. The grids average, max, median, min and stddev will not be written to disc if this parameter is set to false.

#### Calculate a final Model
The setting `finalModel ` can be used to train one model at the end with the selected variables, the selected features and the selected regularization-multiplier and all presence records. 


#### general notes
The FFS, FVS and regularization multiplier tuning all have large processing times. Especially if the FVS is done and there are lots of input variables alone in the first step of the FFS this can lead to a large amount of models to be trained if the input consists of 10 variables 45 models are trained in the first step of the fvs if it consists of 20 variables it increases to 190 models. Just keep in mind, that the whole process can take a while.
 
___

### References

Meyer, H., Reudenbach, C., Hengl, T., Katurji, M., & Nauss, T. (2018). Improving performance of spatio-temporal machine learning models using forward feature selection and target-oriented validation. Environmental Modelling & Software, 101, 1–9. https://doi.org/10.1016/j.envsoft.2017.12.001

Meyer, H., Reudenbach, C., Wöllauer, S., & Nauss, T. (2019). Importance of spatial predictor variable selection in machine learning applications – Moving from data reproduction to spatial prediction. Ecological Modelling, 411, 108815. https://doi.org/10.1016/j.ecolmodel.2019.108815

Phillips, S. J., Anderson, R. P., & Schapire, R. E. (2006). Maximum entropy modeling of species geographic distributions. Ecological Modelling, 190(3–4), 231–259. https://doi.org/10.1016/j.ecolmodel.2005.03.026

Valavi, R., Elith, J., Lahoz‐Monfort, J. J., & Guillera‐Arroita, G. (2019). block CV: An r package for generating spatially or environmentally separated folds for k‐fold cross‐validation of species distribution models. Methods in Ecology and Evolution, 10(2), 225–232. https://doi.org/10.1111/2041-210X.13107

### spatialMaxent was created using:

maxent version 3.4.4 Copyright 2016 Steven Phillips, Miro Dudik and Rob Schapire

OpenJDK JDK18

IntelliJ IDEA Community Edition 2021.3.3 Copyright 2000-2021 JetBrains s.r.o.


Google Core Libraries for Java: Guava 31.1

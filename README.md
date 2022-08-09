## spatialMaxent

### What is it?
spatialMaxent is an extension for `maxent version 3.4.4` (Copyright 2016 Steven Phillips, Miro Dudik and Rob Schapire), that provides a Forward-Variables-Selection (FVS), Forward-Feature-Selection (FFS) and tuning of the beta multiplier together with a Leave-Location-Out (LLO) cross-validation. These methods are especially suited for spatial data.
___

### How to run it?

The spatialMaxent extension requires a Java 18 (or newer) environment to run. If you are on windows you can then download the jar and the batch file [here] and just run the batch file. It can be used as maxent version 3.4.4 via the command line or the GUI. 

___

### How to structure your data?

The data structure that needs to be supplied to maxent changes insofar as it is necessary to provide the samples file with a fourth column containing the association of each point with a spatial fold. If the SWD data format is choosen also the environmental layers .csv file needs to have an additional column for the folds, but no values are required here. 

Example of a samples file created to work with spatialMaxent on the [maxent tutorial data](https://biodiversityinformatics.amnh.org/open_source/maxent/):

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings3.png)

To create the spatial folds you could for example have a look at the [blockCV R package](https://doi.org/10.1111/2041-210X.13107) by Valavi et al. (2019).
___

### Spatial validation, variable selection and parameter tuning in one go:
#### Spatial Validation

The spatial validation can be chosen in the basic tab as `Replicated run type`. If the `spatial crossvalidate` option is used the setting of `replicates` will have no effect anymore as the number of replicates will be set to the number of distinct folds in the sample data. 

![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings1.png)

___

For all the tuning procedures the settings have a new tab called `spatial`. 


![alt text](https://github.com/Baldl/spatialMaxent/blob/main/images/settings2.png)

Each of the tuning steps can be omitted but we highly recommend to do each one of them.

command line parameters:
|Name|type|default|
| --------- |:--------:| -----:|
|fvs| boolean| true|
|ffs| boolean| true|
|tuneBeta|boolean| true|
|betaStart| double| 0.5|
|betaEnd| double| 7|
|betaStep| double| 0.5|
|allModels|boolean|false|



#### Forward Variable Selection (FVS)

* parameter: fvs=true/false 

The FFS (parameter: ffs=true/false) trains one model with each feature (hinge, linear, threshold, product, quadratic). Then the best one is chosen and the other features ar . Note that the setting of features in the main maxent tab ("Auto features", "Hinge features", etc.) will be ignored by spatialMaxent as all features are tested one after another.


The beta multiplier tuning has three input parameters: The lowest beat multiplier to be tuned (beta start) the highest beta multiplier to be tuned and the steps in which the beta multiplier is increased from betastart to betaend. e.g.(1,5,0.5 will try the following beta multipliers: 1 1.5 2 2.5 3 3.5 4 4.5 5)

Which model is the best can be either determined based on the test gain or the test auc value (decision parameter).
___

## References

Valavi, R., Elith, J., Lahoz‐Monfort, J. J., & Guillera‐Arroita, G. (2019). block CV: An r package for generating spatially or environmentally separated folds for k ‐fold cross‐validation of species distribution models. Methods in Ecology and Evolution, 10(2), 225–232. https://doi.org/10.1111/2041-210X.13107

---
title: spatialMaxent
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

The new Maxent extension, `spatialMaxent` ([download here](https://github.com/Nature40/spatialMaxent/raw/main/out/artifacts/spatialMaxent_jar.zip)),  which consists of a new validation method (replicate type) a variable and feature selection and regularization multiplier tuning is introduced here. A spatial cross-validation is implemented for the validation. For spatial cross-validation the sample points get grouped by spatially delineated locations. In each cross-validation round one of the locations is held back as test data while the models get trained with the data from the other locations ([Valavi et al., 2018]( https://doi.org/10.1111/2041-210X.13107); [Meyer et al., 2018]( https://doi.org/10.1016/j.envsoft.2017.12.001)). For spatialMaxent the samples file that is created for training must contain further information about the association of the individual samples to the locations. An n-fold cross-validation is performed, where the number of replicates/folds is equal to the number of distinct locations(s), since one location is always left out for testing and the training is done with the remaining folds. 

The training of the maxent model can be extended by three consecutive tuning processes: 
1. A forward variable selection (FVS) [(Meyer et al., 2019)]( https://doi.org/10.1016/j.ecolmodel.2019.108815)
2. A forward feature selection (FFS)
3. regularization multiplier tuning

To perform FVS, models are first trained with all possible 2-variable combinations, of which the best one is selected based on either test-gain or the test AUC. The decision parameter on which the decision for the best model is made is averaged over the results of all folds. The best performing two-variable combination is trained together with all of the remaining variables separately and the best model is selected again. This step gets repeated until no more improvement is obtained by adding further variables to the model (for more details on FVS see: (Meyer et al., 2019)). All further models are computed using only the variables selected by FVS. 
The FFS follows the same basic concept as FVS, except that the first models are trained with only one of the feature types (linear, hinge, etc.). The model with the best feature is selected and another one is added until no improvement can be observed. The next models are trained with only the selected variables and the selected features. 
Regularization multiplier tuning is done by computing models with regularization multipliers from RMmin to RMmax in RMincrease increments. The model with the best regularization multiplier is selected for the final model run. The final model is trained with the selected variables, selected features, the best beta multiplier and all samples.

![]({{ site.baseurl }}/assets/images/pseudocode.png)
Pseudocode in Figure above taken from [Meyer et al., 2018](https://doi.org/10.1016/j.envsoft.2017.12.001)

All tuning/selection steps can be performed with random cross-validation and spatial cross-validation. Note that this procedure is extremely computationally intensive. This means that large quantities of variables in particular are linked with large computational costs. To reduce the computation time, all three steps were parallelized and the number of cores to be used can be specified in the GUI. Nevertheless, depending on the computing capacity and the data set, the procedure can take several hours or even days. 
SpatialMaxent should be applied with FVS, FFS and regularization multiplier tuning, and all models should be validated with spatial cross-validation, but it is however also possible to only use parts of the tuning procedure. 
The implementation of the spatial extension for Maxent was done in Java using openjdk 18 and it runs on Java >= 18. The extension [is available as a .jar file]( https://github.com/Nature40/spatialMaxent/raw/main/out/artifacts/spatialMaxent_jar.zip) and can be used in the same way as the original Maxent using either the GUI or the command line.



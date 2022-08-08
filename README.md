## spatialMaxent

spatialMaxent is an extension for maxent version 3.4.4 (Copyright 2016 Steven Phillips, Miro Dudik and Rob Schapire), that provides a Forward-Variables-Selection (FVS), Forward-Feature-Selection (FFS) and tuning of the beta multiplier together with a Leave-Location-Out (LLO) cross-validation. These methods are especially suited to work with spatial data. 

The extension is available as a .jar file here[Link] and can be used as maxent version 3.4.4 via the command line or the GUI. 

The data structure that needs to be supplied to maxent changes insofar as it is necessary to provide the samples file with a fourth column containing the association of each point with a spatial fold. If the SWD data format is choosen also the environmental layers .csv file needs to have an additional column for the folds, but no values are required here. 

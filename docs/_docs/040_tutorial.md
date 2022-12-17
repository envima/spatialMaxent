---
title: Tutorial
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

This R tutorial will guide you through the modeling of a total of 20 species with spatialMaxent. We use data from the National Center for Ecological Analysis and Synthesis (NCEAS) dataset for the Canada region ([Elith et al. 2020]( https://doi.org/10.17161/bi.v15i2.13384)).  Before we can start modeling we need to download the data, make sure all necessary R packages are installed and we have a clean working environment.
###	Check if all necessary R packages are installed
This tutorial was created with R version 4.1.2 and requires the following packages. Please make sure you have them installed before we start:
* [raster]( https://cran.r-project.org/web/packages/raster/index.html) version 3.6.3,
* [sf]( https://cran.r-project.org/web/packages/sf/index.html) version 1.0.8,
* [Metrics]( https://cran.r-project.org/web/packages/Metrics/index.html) version 0.1.4, 
* [dplyr]( https://cran.r-project.org/web/packages/dplyr/index.html) version 1.0.8
* [ecospat]( https://cran.r-project.org/web/packages/ecospat/index.html) version 3.3

### Create directory structure
For the tutorial to run smoothly, it is recommended that you use the same folder structure as shown here. Start by creating an empty folder, and set it as the working directory in R. In this folder we will now create the following directory structure:
```
src
└─ functions
data
└─ samples
└─ output    
└─ layers
└─ background
```
You can set up the working environment with the following script:
<script src="https://gist.github.com/Baldl/533446161f5cd9f1af3ec039936e90ca.js"></script>

### Data download 
[Download the zip file (via DataDryad)](https://doi.org/10.5061/dryad.dbrv15f4z) with the preprocessed data and extract it into the data folder. It contains three folders: samples, background, and layers. The folder layers contains 11 environmental layers in the ascii format exactly as they are provided in the NCEAS dataset. There are two files in the background folder each one of them contains `10000 background points` for the Canada region. Once as a georeferenced geopackage file and once as a csv file, which is already in the format we need for modeling with spatialMaxent: the first four columns contain species, longitude, latitude, and spatial folds. Whereby the spatial folds column remains empty for the background points or contains NAs. From line five the extracted values from the raster layers follow. 

The structure of the samples files seems a bit confusing at first sight, but don't let that scare you off. There is a folder for each species, which follows the naming of the anonymized species in the NCEAS dataset (e.g. `can01`). In this folder you will find all presence-points of the species, which were obtained from the presence-only and presence-absence data as geopackage (e.g. `can01.gpkg`).  

These presence-only points were divided into seven spatial folds using the [BlockCV package]( https://doi.org/10.1111/2041-210X.13107). Of these seven folds, five are always used to train the models and two are left out for external testing. Since there can be large differences in the model results depending on which two folds are left out for testing, models are created for all possible combinations of test and training data. This amounts to a total of 21 possible combinations of training and test folds. Therefore, there are 21 subfolders in each species folder (e.g. `can01_01` to `can01_21`), each containing a test and a training data set. The test data set is available as geopackage file, the training data set as geopackage and as csv file. The csv file is already in the correct format for spatial maxent. The first three columns contain species, longitude and latitude. The fourth line contains the membership of the presence-only points to a spatial fold. This is followed by the information from the environmental layers.




Now you have all data ready to start the modeling!

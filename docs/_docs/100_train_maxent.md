---
title: Train spatialMaxent models
driveId: 1jNdPPfUeYyHhbYpO4Z11NSETqropOBlt/preview
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---


We will now train two models for each species in the Canada dataset. One with a random 5-fold cross-validation and the maxent default settings (default model) and one with spatial cross-validation and forward-variable-selection, forward-feature-selection and regularization multiplier tuning (spatial model). Both models are trained and evaluated on the exact same data and we will compare their performance in the next section. 

As we are doing a Forward Fold Metric Estimation as described [here](../070_spatial_blocks), we will need to create 21 models for each species. 

## spatialMaxent GUI
Watch the video below to see how to train the maxent model for the species `can01` and the fold can01_01 in the spatialMaxent GUI.

{% include googleDrivePlayer.html id=page.driveId %}

## spatialMaxent from the command line

As we are training quite a few models here 21 models per species for 20 species for 2 different methods we have a total of 840 models to train, to do this we will not rely on the GUI but use a R script to call spatialMaxent from the command line. For the default model we will set the parameters `fvs`, `ffs` and `tuneRM`to false and chose crossvalidate as replicate type. We are also projecting the models with the raster layers to the whole study area. 


<script src="https://gist.github.com/Baldl/85da22ff487244423370834dd2b93a8a.js"></script>



## Prediction - spatial model
Exemplary prediction for the species `can01`and the fold can01_01 created with the spatial model.

{% include media4 url="/assets/web_pages/prediction_spatial.html" %} [Full screen version of the map]({{ site.baseurl }}assets/web_pages/prediction_spatial.html){:target="_blank"}


## Prediction - default model
Exemplary prediction for the species `can01`and the fold can01_01 created with the default model.

{% include media4 url="/assets/web_pages/prediction_default.html" %} [Full screen version of the map]({{ site.baseurl }}assets/web_pages/prediction_default.html){:target="_blank"}

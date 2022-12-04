---
title: NCEAS data
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---
The default parameters of Maxent were determined by modeling 225 species in a total of six regions of the world [(Phillips & Dudík, 2008)]( https://doi.org/10.1111/j.0906-7590.2008.5203.x). This NCEAS (National Center for Ecological Analysis and Synthesis) data has recently been published as an open benchmark dataset that was explicitly assembled to compare SDM methods [(Elith et al., 2020)]( https://doi.org/10.17161/bi.v15i2.13384). 
It contains six regions of the world: Australian Wet Tropics (AWT), Ontario Canada (CAN), New South Wales (NSW), New Zealand (NZ), South American countries (SA) and Switzerland (SWI).  The species themselves are anonymized and only assigned to a biological group. The data consists of presence-only (PO) records, presence-absence (PA) records, background points (BP) and environmental predictors in the form of environmental layers for each of the regions. The PO and BP data are intended to train the SDM models, and the PA data to evaluate them. For a detailed description of the NCEAS dataset see [Elith et al. (2020)]( https://doi.org/10.17161/bi.v15i2.13384).

In this tutorial we will use the data for the region Ontario in Canada (see map below). The data species records needed for this exercises can be downloaded via the [disdat r-package]( https://cran.r-project.org/web/packages/disdat/index.html) or over [osf]( https://osf.io/kwc4v/). The environmental grids can only be downloaded via osf. 

The data preparation of the species presence records and background points are shown in the [exercise below](http://127.0.0.1:4000/spatialMaxentPaper/docs/060_nceas/#get-presence-records-from-presence-only-po-and-presence-absence-pa-data). You can either follow this workflow or download the prepared dataset for the presence-only records and background points [here]( https://github.com/Nature40/spatialMaxent/blob/main/tutorialData.zip?raw=true) and [skip directly to the spatialMaxent modeling]( https://nature40.github.io/spatialMaxentPaper/docs/090_spatialMaxent_short_introduction/). 

If you are skipping directly to the modeling workflow make sure that you have also downloaded the environmental grids via [osf]( https://osf.io/kwc4v/) and that you have stored  your data according to the following folder structure to run the tutorial smoothly.
```
src
└─ functions
data
└─ samples
└─ output    
└─ layers
└─ background
```


{% include media4 url="/assets/web_pages/study_area_can.html" %} [Full screen version of the map]({{ site.baseurl }}assets/web_pages/study_area_can.html){:target="_blank"}


We will not use the PA and PO data to train and test the models as they are not spatial independent from each other the presence points of both datasets show a similar pattern that a random partition of train and test data would provide. Therefore we will combine the presence points from both datasets into one and part the test and train data ourselves.

{% include media4 url="/assets/web_pages/PA_PO.html" %} [Full screen version of the map]({{ site.baseurl }}/assets/web_pages/PA_PO.html){:target="_blank"}

## Get presence records from Presence-Only (PO) and Presence-Absence (PA) data

First of all, we will prepare the presence records for this region. Here we use the package [disdat]( https://cran.r-project.org/web/packages/disdat/index.html) to get the species records. The function `disdat::disPo()` can be used to derive all PO species records for one region. From this dataframe we can get a total of 20 individual species. As we are not using the (PA) data to evaluate the models we will also use the presence records from the PA data for modeling.  To download the PA data we need two different function: the presence and absence information can be downloaded for all species with the function `disdat::disPa()` the environmental information has to be downloaded separately with the function `disdat::disEnv`. Note that we will keep only the presence records and will delete the absence records from the dataset.

The script below downloads the data and saves all presence only records for each species individually as [geopackage]( https://de.wikipedia.org/wiki/GeoPackage).


<script src="https://gist.github.com/Baldl/1988b47add66c6b7029d7d42f6fb7f75.js"></script>



For the species `can01` you can see the two combined datasets in the map below. In the next exercise we will then part them into spatial folds. 

{% include media4 url="/assets/web_pages/can01.html" %} [Full screen version of the map]({{ site.baseurl }}/assets/web_pages/can01.html){:target="_blank"}



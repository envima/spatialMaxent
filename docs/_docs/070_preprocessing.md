---
title: Data Preprocessing
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

This R tutorial will guide you through the modeling of a total of 20 species with spatialMaxent. We use data from the National Center for Ecological Analysis and Synthesis (NCEAS) dataset for the Canada region ([Elith et al. 2020]( https://doi.org/10.17161/bi.v15i2.13384)).  As you have decided to start the tutorial with the data preprocessing we will guide you through the hole process from deriving the data to model evaluation.

###	Check if all necessary R packages are installed
This tutorial was created with R version 4.1.2 and requires the following packages. Please make sure you have them installed before we start:
* [raster]( https://cran.r-project.org/web/packages/raster/index.html) version 3.6.3,
* [sf]( https://cran.r-project.org/web/packages/sf/index.html) version 1.0.8,
* [Metrics]( https://cran.r-project.org/web/packages/Metrics/index.html) version 0.1.4, 
* [dplyr]( https://cran.r-project.org/web/packages/dplyr/index.html) version 1.0.8
* [ecospat]( https://cran.r-project.org/web/packages/ecospat/index.html) version 3.3
* [disdat](https://cran.r-project.org/web/packages/disdat/index.html) version 1.0.0
* [clhs]( https://cran.r-project.org/web/packages/clhs/index.html) version 0.9.0

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

### The NCEAS data

Here we give a short introduction on what the NCEAS data are. The default parameters of Maxent were determined by modeling 225 species in a total of six regions of the world [(Phillips & Dudík, 2008)]( https://doi.org/10.1111/j.0906-7590.2008.5203.x). This National Center for Ecological Analysis and Synthesis (NCEAS) data has recently been published as an open benchmark dataset that was explicitly assembled to compare species distributiom modeling methods [(Elith et al., 2020)]( https://doi.org/10.17161/bi.v15i2.13384). 
It contains six regions of the world: Australian Wet Tropics (AWT), Ontario Canada (CAN), New South Wales (NSW), New Zealand (NZ), South American countries (SA) and Switzerland (SWI).  The species themselves are anonymized and only assigned to a biological group. The data consists of presence-only (PO) records, presence-absence (PA) records, background points (BP) and environmental predictors in the form of environmental layers for each of the regions. The PO and BP data are intended to train the SDM models, and the PA data to evaluate them. For a detailed description of the NCEAS dataset see [Elith et al. (2020)]( https://doi.org/10.17161/bi.v15i2.13384).

In this tutorial we will use the data for the region Ontario in Canada (see map below).

{% include media4 url="/assets/web_pages/study_area_can.html" %} [Full screen version of the map]({{ site.baseurl }}assets/web_pages/study_area_can.html){:target="_blank"}



### Download environmental layers

The environmental layers will be used exactly as they are provided in the NCEAS dataset. Go to [osf]( https://osf.io/kwc4v/) and download the environmental layers for the region canada into the `data/layers` folder.
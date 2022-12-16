---
title: Presence-only data
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---
 

 The data species records needed for this exercises can be downloaded via the [disdat r-package]( https://cran.r-project.org/web/packages/disdat/index.html) or over [osf]( https://osf.io/kwc4v/). The environmental grids can only be downloaded via osf.






We will not use the presence-absence (PA) and presence-only (PO) data to train and test the models as they are not spatial independent from each other the presence points of both datasets show a similar pattern that a random partition of train and test data would provide. Therefore we will combine the presence points from both datasets into one and part the test and train data ourselves.

{% include media4 url="/assets/web_pages/PA_PO.html" %} [Full screen version of the map]({{ site.baseurl }}/assets/web_pages/PA_PO.html){:target="_blank"}

## Get presence records from Presence-Only (PO) and Presence-Absence (PA) data

First of all, we will prepare the presence records for this region. Here we use the package [disdat]( https://cran.r-project.org/web/packages/disdat/index.html) to get the species records. The function `disdat::disPo()` can be used to derive all PO species records for one region. From this dataframe we can get a total of 20 individual species. As we are not using the (PA) data to evaluate the models we will also use the presence records from the PA data for modeling.  To download the PA data we need two different function: the presence and absence information can be downloaded for all species with the function `disdat::disPa()` the environmental information has to be downloaded separately with the function `disdat::disEnv`. Note that we will keep only the presence records and will delete the absence records from the dataset.

The script below downloads the data and saves all presence only records for each species individually as [geopackage]( https://de.wikipedia.org/wiki/GeoPackage).


<script src="https://gist.github.com/Baldl/1988b47add66c6b7029d7d42f6fb7f75.js"></script>



For the species `can01` you can see the two combined datasets in the map below. In the next exercise we will then part them into spatial folds. 

{% include media4 url="/assets/web_pages/can01.html" %} [Full screen version of the map]({{ site.baseurl }}/assets/web_pages/can01.html){:target="_blank"}



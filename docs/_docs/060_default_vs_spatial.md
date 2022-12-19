---
title: Default vs. spatial
header:
  image: "/assets/images/title_image.jpg"
  caption: "Data: [OpenStreetMap](https://www.openstreetmap.org/copyright) & [Elith et al. 2020](https://doi.org/10.17161/bi.v15i2.13384)"
---


To assess which modeling approach performs best we will use four different evaluation criteria. We will calculate the [AUC (area under the receiver operating characteristic curve)]( https://en.wikipedia.org/wiki/Receiver_operating_characteristic) and the [MAE (mean absolute error)]( https://en.wikipedia.org/wiki/Mean_absolute_error) as proposed by  [Konowalik & Nosol (2021)]( 10.1038/s41598-020-80062-1). Both  metrics will be calculated on the spatially seperated test data from the presence records using the [R package Metrics]( https://cran.r-project.org/web/packages/Metrics/index.html). As a third metric we will calculate the Boyce-Index [(Boyce et al., 2002)]( 10.1016/S0304-3800(02)00200-4) using the [R package ecospat]( https://cran.r-project.org/web/packages/ecospat/index.html)  with the prediction raster and the test data. To assess how complex the models are we will furthermore have a look at the number of parameters of each model. We will determine all four metrics for each combination of training and test data and calculate the median of all these values to get a comprehensive picture of the performance of each modeling approach. 

We will start by calculating the metrics for each fold: 

<script src="https://gist.github.com/Baldl/73afe43f676f6dc7be89d1b52b42b156.js"></script>


### Median values for each species
Once we have the metrics for all combinations of training and test data we will calculate the median for each species using the script below. These are our final results. 
<script src="https://gist.github.com/Baldl/50297403717267d81cedadf26af1c56e.js"></script>

To have a look at the final results and compare the modeling approaches with each other we will now plot the different metrics. The first plot shows for how many of the species which modeling approach provided the better results compared to the other modeling approach:

![]({{ site.baseurl }}/assets/images/results1.png)

*Figure: Number of species (y-axis) for which the maximum Boyce-Index, the maximum AUC value, the minimum MAE or the minimum number of parameters was measured. Sums up to 20 species for each metric.*

The next plots are inspired by the plots proposed by [Konowalik & Nosol (2021)]( 10.1038/s41598-020-80062-1). Therefore we plot the MAE on the x-axis and the AUC and the Boyce-Index on the y-axis. The modeling approach that is closer to the upper right corner performed better.

![]({{ site.baseurl }}/assets/images/results2.png)
*Figure: Results of region Ontario, Canada modeled with the modeling approaches spatial (yellow) and standard (purple). AUC (point) and Boyce-Index (triangle) on the y-axis and the MAE on the x-axis. For each Species the median value is given.*


We repeat the same plots again this time with the number of parameters instead of the MAE on the x-axis. Again the modeling approach that is closer to the upper right corner performed better.

![]({{ site.baseurl }}/assets/images/results3.png)
*Figure: Results of region Ontario, Canada modeled with the methods spatial (yellow) and standard (purple). AUC (point) and Boyce-Index (triangle) on the y-axis and the Number of parameters on the x-axis. For each Species the median value is given.*



This brings us to the end of the tutorial. If you haven't done so yet, you could take a look at data preprocessing on the following pages.
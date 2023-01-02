---
title: Spatial blocks
header:
  image: "/assets/images/title_image.jpg"
  caption: "Data: [OpenStreetMap](https://www.openstreetmap.org/copyright) & [Elith et al. 2020](https://doi.org/10.17161/bi.v15i2.13384)"
---


spatialMaxent performs a spatial cross-validation during the model training. When working with environmental data a spatial validation strategy hast to be chosen that accounts for spatial autocorrelation in the data [(Ploton et al. 2020)]( https://www.nature.com/articles/s41467-020-18321-y). If the data is partitioned random the models can be easily overfitted on the training data which can lead to very complex models that generalize badly. In addition to the internal validation strategy, the test data not included in the model must also be spatially seperated.
To create spatially separated folds for spatial cross-validation we will now use the [blockCV package ]( https://cran.r-project.org/web/packages/blockCV/index.html) by [Valavi et al. 2019]( https://doi.org/10.1111/2041-210X.13107) to part the data into seven folds. 

Of these seven folds, five are always used to train the models and two are left out for external testing. Since there can be large differences in the model results depending on which two folds are left out for testing, models are created for all possible combinations of test and training data. This amounts to a total of 21 possible combinations of training and test folds. 

The median of all 21 models for each species is then used to assess the model performance. Each of the PO points will be part of the test and training data at some point while at the same time the models can be evaluated with independent data.

In the next script we will first generate spatial blocks and then create training and test files for each possible combination and save them separately.
<script src="https://gist.github.com/Baldl/2e6898ca011c8290894986b507f335d5.js"></script>

The spatial blocks generated with this code for the species `can01` can be seen in the map displayed at the bottom of the page.
{% include media4 url="/assets/web_pages/spatial_blocks.html" %} [Full screen version of the map]({{ site.baseurl }}/assets/web_pages/spatial_blocks.html){:target="_blank"}

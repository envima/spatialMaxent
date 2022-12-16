---
title: Overview
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

Species Distribution Models (SDMs) and Habitat Suitability Models (HSMs, SDMs) are an indispensable tool in conservation research and practice as they have the potential to forecast the distribution of invasive or endangered species under climate change scenarios or to identify areas of high value for the protection of endangered species. Government authorities are increasingly relying on these techniques as a basis for conservation management decisions. 
Among modelling approaches the SDM-software Maxent [(Phillips et al., 2006)]( https://doi.org/10.1016/j.ecolmodel.2005.03.026) is probably the most popular, not least because it is readily available as an easy to operate graphical user interface (GUI).
Here we present an extension for maxent: spatialMaxent implements a forward variable-selection and feature-class-selection algorithms together with regularization-multiplier tuning based on spatial cross-validation to enable the accounting for spatial auto-correlation already during model tuning. In the following tutorial we will display the benefit of the spatialMaxent compared to modeling with maxent using the NCEAS data that was used to derive the default settings of maxent. This tutorial will guide you through the workflow displayed below. 

![]({{ site.baseurl }}/assets/images/methods.png)

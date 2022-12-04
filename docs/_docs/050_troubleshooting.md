---
title: Troubleshooting
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

If you cannot get spatialMaxent to run on your PC it is likely that either your java version or your jar file could not be found. Have a look if your system environmental variables and update the path variables with a path to your java installation. If this is not sufficient try moving the path to the java installation on the first position in the list of the paths and restart your pc. 

If you pc is still not able to find the java installation you can work with the complete path to the java installation and or the jar file on the command line. For example:

```console
/usr/lib/jvm/java-18-openjdk-amd64/bin/java -jar /media/memory01/casestudies/maxent/spatialMaxent_jar/spatialMaxent.jar
```
Please also note spatialMaxent is still in development and  therefore bugs may still occur frequently. Please report any unexpected behavior to the [spatialMaxent repository on github]( https://github.com/Nature40/spatialMaxent).

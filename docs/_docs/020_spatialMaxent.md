---
title: spatialMaxent
header:
  image: "/assets/images/title_image.jpg"
  caption: "Photo credit: Herr Olsen [CC BY-NC 2.0] via [flickr.com](https://www.flickr.com/photos/herrolsen/26966727587/)"
---

{% capture java-version %}

**NOTE: spatialMaxent needs at least Java 18 to run successfully! More information below.**

{% endcapture %}
<div class="notice--success">
  {{ java-version | markdownify }}
</div> 

Let´s go! To start your adventure towards species distribution modeling with spatial data just [download spatialMaxent]( https://github.com/envima/spatialMaxent/raw/main/out/artifacts/spatialMaxent_jar.zip) and extract the .zip file. On windows you can run spatialMaxent by opening the spatialMaxent.bat file on linux you can open the spatialMaxent.jar file via the command line. Change the directory to the folder in which the jar file is stored beforehand.
```console
java -jar spatialMaxent.jar
```

### Java version
To run spatialMaxent successfully you need to have at least Java 18 running on your PC. If running spatialMaxent gives you an error message and your unsure which Java version is installed on your PC check your Java version on the command line:
```console
java -version
```
If the Java version is less than Java 18 download a newer version for example the latest [openJDK 19](https://jdk.java.net/19/). Restart your PC afterwards to update your path variables especially if you are working under windows. Then try to run spatialMaxent again. If your PC is still not able to find the correct Java version update your path variables manually. Add the bin path of the Java version to your path variables e.g.:
```console
C:\User\.jdks\openjdk-19\bin
```
If this is still not sufficient try moving the Java path to the top of your list of system environment paths and restart your PC afterwards.

### Run spatialMaxent from the command line
If none of the above gave you the desired result you can try running spatialMaxent from the command line while providing the complete path to the java version and the spatialMaxent.jar file:

```console
C:\.jdks\openjdk-18\bin\java -jar D:\spatialMaxent_jar\spatialMaxent.jar 
```
Please also note spatialMaxent is under constant development and  therefore bugs may still occur frequently. Please report any unexpected behavior to the [spatialMaxent repository on github]( https://github.com/envima/spatialMaxent).

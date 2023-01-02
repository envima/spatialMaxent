---
title: Data structure
header:
  image: "/assets/images/title_image.jpg"
  caption: "Data: [OpenStreetMap](https://www.openstreetmap.org/copyright) & [Elith et al. 2020](https://doi.org/10.17161/bi.v15i2.13384)"
---

For modeling with spatialMaxent, at least two input datasets are required, just as for modeling with Maxent. These are occurrence points of the species you want to model and some environmental information.

We demonstrate briefly how the data for modeling with spatialMaxent must be prepared using the species bradypus variegatus with south america as the study area.

In general, there are two possible options for preparing the data:
## 1. Samples file and environmental layers
As samples file, a table with four columns is used in the modeling. The first three columns contain the same information as in normal modeling with Maxent: first the species, followed by longitude and latitude. For spatialMaxent the samples file needs a fourth column containing the association of each presence record with a spatial block as integer value. At minimum you therefore need four columns: species, longitude, latitude and spatial block. Save this table as csv file.

In the code fragment below, you can see a minimum reproducible example for creating such a dataset. This is done by downloading presence records for the species `bradypus variegatus` using the R package [geodata]( https://cran.r-project.org/package=geodata) and dividing them into spatial blocks using the R package [blockV]( https://cran.r-project.org/package=blockCV). <script src="https://gist.github.com/Baldl/448c8ac60565b4025bc71dc4084717fd.js"></script>
The desired output should look like this:

||**species**|**lon**|**lat**|**spatialBlock**|
| --------- |:--------:| -----:|-----:|
|1|Bradypus variegatus|-82.65246|9.634931|1|
|2|Bradypus variegatus|-85.04224|10.719722|1|
|3|Bradypus variegatus|-82.65578|9.632399|1|
|4|Bradypus variegatus|-64.89437|-14.871702|3|
|5|Bradypus variegatus|-84.00697|10.430623|1|
|6|Bradypus variegatus|-77.87285|8.387811|4|
|7|Bradypus variegatus|-67.85753|-9.969447|3|
|8|Bradypus variegatus|-84.14432|9.389028|1|
|9|Bradypus variegatus|-82.14535|9.254425|1|
|10|Bradypus variegatus|-83.59127|8.466329|4|

If you are using a samples file as shown above the environmental information gets extracted from environmental grids that are passed to spatialMaxent. Therefore in the `Environmental layers` field in Maxent and spatialMaxent the path to a folder containing raster data covering the study area has to be given. Each raster layer has to be in a separate file each and the format must be supported by spatialMaxent (e.g.  asc or grd files). Set the no data values of the raster layers to the spatialMaxent no data value (-9999).

You could download bioclimatic variables from worldclim with the geodata R package:
<script src="https://gist.github.com/Baldl/4c08899b63d1a89d232e4eb1d2d4522c.js"></script>
The file structure in your file explorer should look something like this:

|**Name**|**Type**|**Size**|
| --------- |:--------:| -----:|
|wc2.1_10m_bio_1.grd|GRD-File|1KB|
|wc2.1_10m_bio_1.grd.aux|XML-Document|1KB|
|wc2.1_10m_bio_1.gri|GRI-File|689KB|
|wc2.1_10m_bio_2.grd|GRD-File|1KB|
|wc2.1_10m_bio_2.grd.aux|XML-Document|1KB|
|wc2.1_10m_bio_2.gri|GRI-File|689KB|
|wc2.1_10m_bio_3.grd|GRD-File|1KB|
|wc2.1_10m_bio_3.grd.aux|XML-Document|1KB|
|wc2.1_10m_bio_3.gri|GRI-File|689KB|


The path to the folder containing these files serves as input to the `Environmental layers` field in spatialMaxent.
## 2. Species with data (SWD) format
The second option is to prepare both the samples file as well as the environmental layers as table in csv format. 
•	Tabelle mit vier spalten wenn background punkte über die raster daTEN in maxent extrahiert werden oder im SWD format wenn background punkte im tabellenformat vorliegen

* no data value den wert von maxent angeben
* spatialMaxent nimmt das gleiche format an, das Maxent auch annimmt zum beispiel ascii oder grid
Man kann entweder den ordner angeben in dem die grids liegen und dann maxent random die background punkte extrahieren lassen oder, 
*das SWD format wählen, bei dem die environmental layer informationen auch als tabelle vorliegen

||**species**|**lon**|**lat**|**spatialBlock**|
| --------- |:--------:| -----:|-----:|
|1|Bradypus variegatus|-82.65246|9.634931|
|2|Bradypus variegatus|-85.04224|10.719722|
|3|Bradypus variegatus|-82.65578|9.632399|
|4|Bradypus variegatus|-64.89437|-14.871702|
|5|Bradypus variegatus|-84.00697|10.430623|
|6|Bradypus variegatus|-77.87285|8.387811|
|7|Bradypus variegatus|-67.85753|-9.969447|
|8|Bradypus variegatus|-84.14432|9.389028|
|9|Bradypus variegatus|-82.14535|9.254425|
|10|Bradypus variegatus|-83.59127|8.466329|



If you are using the SWD format also the environmental layers csv file needs to have an additional column for the spatial blocks, but no values are required here (thus exactly the same structure as for the samples files is needed). Note that in spatialMaxent contrary to Maxent the processing of several species at once from the same samples file is not supported. Please prepare one file for each species.


---
title: Data structure
header:
  image: "/assets/images/title_image.jpg"
  caption: "Data: [OpenStreetMap](https://www.openstreetmap.org/copyright) & [Elith et al. 2020](https://doi.org/10.17161/bi.v15i2.13384)"
---

For modeling with spatialMaxent, at least two input datasets are required, just as for modeling with Maxent. These are occurrence points of the species you want to model and some environmental information.

We demonstrate briefly how the data for modeling with spatialMaxent must be prepared using the species bradypus variegatus with south america as study area.

In general, there are two possible options for preparing the data:
## 1. Samples file and environmental layers
As samples file, a table with four columns is used in modeling. The first three columns contain the same information as used in modeling with Maxent: first the species, followed by longitude and latitude. For spatialMaxent the samples file needs a fourth column containing the association of each presence record with a spatial fold as integer value. At minimum you therefore need four columns: species, longitude, latitude and spatial block. Save this table as csv file.

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
The second option is to prepare both the samples file as well as the environmental layers as table in csv format. In this case the background points are not randomly extracted by the software, but are provided by the user.
Since in this case no grids are available from which environmental information can be extracted for the presence points, the samples file must already contain the environmental information.
The samples file must therefore have the same columns as in the first section (species, longitude, latitude spatial fold/block) followed by the environment variables. 
An example how to create such a table can be found here:


<script src="https://gist.github.com/Baldl/acccecddbd9a0a42aeab2dda7c5162cc.js"></script>

The file should look like tis:

||**species**|	**lon**	|**lat**|	**spatialBlock**|	**wc2.1_10m_bio_1**|	**wc2.1_10m_bio_2**	|**wc2.1_10m_bio_3**	|**wc2.1_10m_bio_4**|	**wc2.1_10m_bio_5**|**wc2.1_10m_bio_6**|	**wc2.1_10m_bio_7**|	**wc2.1_10m_bio_8**|	**wc2.1_10m_bio_9**|	**wc2.1_10m_bio_10**|	**wc2.1_10m_bio_11**|	**wc2.1_10m_bio_12**|	**wc2.1_10m_bio_13**|	**wc2.1_10m_bio_14**|	**wc2.1_10m_bio_15**|	**wc2.1_10m_bio_16**|	**wc2.1_10m_bio_17**|	**wc2.1_10m_bio_18**|	**wc2.1_10m_bio_19**|
| --------- |:--------:| -----:|-----:||:--------:| -----:|-----:||:--------:| -----:|-----:||:--------:| -----:|-----:|
|1|Bradypus variegatus|	-82.652457|	9.634931|	2|	26.3713397979736|	7.75618886947632|	78.4453506469727|	69.3386688232422|	31.4296112060547|21.5422325134277|	9.88737869262695|	26.6396446228027|	26.8222484588623|	27.2542877197266|	25.510274887085	|2900	|339|	133|	26.6868801116943|	882|	478	|578|	814|
|2|Bradypus variegatus|	-85.042241|	10.719722|	2|	22.662281036377	|8.57943725585938	|75.8755340576172	|69.8643417358398	|	28.6867504119873|21.5422325134277|	9.88737869262695|	26.6396446228027|	26.8222484588623|	27.2542877197266|	25.510274887085	|2900	|339|	133|	26.6868801116943|	882|	478	|578|	814|
|3|Bradypus variegatus|	-82.655777|	9.632399|	2|	26.3713397979736|	7.75618886947632|	78.4453506469727|	69.3386688232422|	31.4296112060547|17.3794994354248|	11.3072509765625|	22.5492916107178|	23.1294174194336	|23.5757083892822	|21.8230419158936|	3067|	382|	56	|46.6819763183594	|1115|	237|	343|	793|
|4|Bradypus variegatus|	-64.894367|	-14.871702|	3|	25.7351455688477|	10.4501676559448|	63.7185859680176|	155.45280456543	|	32.9122505187988|17.3794994354248|	11.3072509765625|	22.5492916107178|	23.1294174194336	|23.5757083892822	|21.8230419158936|	3067|	382|	56	|46.6819763183594|1115|	237|	343|	793|
|5|Bradypus variegatus|	-84.006971|	10.430623|	2|	25.4486351013184|	8.55435371398926|	76.8550643920898|	77.5388259887695|	31.3567504882812|21.5422325134277|	9.88737869262695|	26.6396446228027|	26.8222484588623	|27.2542877197266|	25.510274887085|	2900|	339|	133|	26.6868801116943|	882	|478	|578|	814|
|6|Bradypus variegatus|	-77.872847|	8.387811|	4|	26.3919696807861|	7.42572927474976|	81.4537200927734|	46.0200881958008|	31.0927505493164|16.5117492675781|	16.4005012512207|	26.930290222168	|23.5290832519531	|27.0378341674805	|23.5290832519531|	1928	|301|	37	|60.5561714172363|	860	|127	|627	|127|
|7|Bradypus variegatus|	-67.857528|	-9.969447|	3|	25.1413745880127|	10.6335830688477|	70.6644287109375|	86.8138198852539|	31.9664993286133|16.9184989929199|	15.0480003356934|	25.597541809082	|23.9166259765625	|25.8485412597656	|23.8804168701172	|1675	|208|	60	|38.8501091003418	|616	|220|	491	|223|
|8|Bradypus variegatus|	-84.144317|	9.389028|	2|	25.9888286590576|	10.5179567337036|	78.9335556030273|	78.3810043334961|	33.0526313781738|19.7275543212891|	13.3250770568848|	25.6376667022705|	26.0813732147217|	27.1011867523193|	25.1406097412109|	3065|	483|	30|	63.5455589294434|	1304|	138	|565|	907|
|9|Bradypus variegatus|	-82.145355|	9.254425|	2|	25.9524059295654|	6.86185598373413|	81.3095626831055|	49.8877182006836|	29.9876289367676|21.548454284668	|8.43917465209961	|26.2053260803223	|25.4510307312012	|26.4728527069092	|25.2182121276855	|2675	|327|	101|	34.2408790588379|	882	|337	|729|	510|
|10|Bradypus variegatus|-83.59127|	8.466329|	5|	25.3653335571289	|10.0924444198608|	77.0729370117188|	75.0775146484375|	32.2106666564941|19.1159992218018|	13.0946674346924|	24.9542217254639|	25.5028877258301|	26.4582214355469|	24.5631103515625|	3215|	585|	42|	61.2455253601074|	1346|	186	|632|	1079|


The input for the environmental layers is no longer the path to the folder with the raster grids, but also a file in csv format. This file is structured in the same way as the samples file (species, longitude, latitude, spatial block/fold, followed by environmental variables). The column spatial block/fold only contains no data values (e.g. -9999) or no values but **MUST** be specified, so that the columns of the environmental variables start at exactly the same position as in the samples file.

An example how to generate such a file with 10000 randomly distributed background points:
<script src="https://gist.github.com/Baldl/4d092f612a749736e0b7270835081835.js"></script>
The background file should then look like this:

||**species**|	**lon**	|**lat**|	**spatialBlock**|	**wc2.1_10m_bio_1**|	**wc2.1_10m_bio_2**	|**wc2.1_10m_bio_3**	|**wc2.1_10m_bio_4**|	**wc2.1_10m_bio_5**|**wc2.1_10m_bio_6**|	**wc2.1_10m_bio_7**|	**wc2.1_10m_bio_8**|	**wc2.1_10m_bio_9**|	**wc2.1_10m_bio_10**|	**wc2.1_10m_bio_11**|	**wc2.1_10m_bio_12**|	**wc2.1_10m_bio_13**|	**wc2.1_10m_bio_14**|	**wc2.1_10m_bio_15**|	**wc2.1_10m_bio_16**|	**wc2.1_10m_bio_17**|	**wc2.1_10m_bio_18**|	**wc2.1_10m_bio_19**|
| --------- |:--------:| -----:|-----:||:--------:| -----:|-----:||:--------:| -----:|-----:||:--------:| -----:|-----:|
|1|Bradypus variegatus	|-81.9166666666667|	9.25|			-9999		|25.7204170227051|	6.77916669845581|	82.1717224121094|	48.8989906311035|	29.7700004577637|	21.5200004577637|	8.25|				25.6800003051758|	25.3199996948242|	26.2683334350586|	25.0433330535889|	2807|	316|	94|	32.0544166564941|	925|	392|	675	|544|
|2|Bradypus variegatus	|-68.25|			5.25|			-9999		|27.8759574890137|	10.0537090301514|	75.2832946777344|	97.0066528320312|	35.7075004577637|	22.3529987335205|	13.3545017242432|	26.7020416259766|	28.7349586486816|	29.1716251373291|	26.7020416259766|	2661|	447|	27|	70.1923217773438|	1280|	132|	134	|1280|
|3|Bradypus variegatus	|-75.75	|			7.41666666666667|-9999		|20.4075107574463|	7.92322874069214|	86.3755416870117|	38.3408317565918|	25.2022495269775|	16.0292491912842|	9.17300033569336|	20.1206245422363|	20.5524578094482|	20.9329166412354|	19.9995422363281|	2717|	355|	92|	39.3622589111328|	925	|	342|	774	|748|
|4|Bradypus variegatus	|-57.4166666666667|	-18.75|			-9999		|26.0257186889648|	10.1370620727539|	62.7235298156738|	227.307525634766|	33.3404998779297|	17.1790008544922|	16.1614990234375|	28.0115833282471|	22.8980007171631|	28.1014995574951|	22.8980007171631|	1082|	190|	21|	62.8566856384277|	488	|	76|		476	|76|
|5|Bradypus variegatus	|-67.5833333333333|	-6.08333333333333|	-9999	|25.8430938720703|	9.84622955322266|	80.5846099853516|	40.9656105041504|	31.8072490692139|	19.5887508392334|	12.2184982299805|	25.8661670684814|	25.3015842437744|	26.2741661071777|	25.3015842437744|	2559|	331|	65|	47.7778244018555|	965	|	241|	573	|241|
|6|Bradypus variegatus	|-63.25|			-22.5833333333333|	-9999	|23.3488864898682|	12.773063659668	|	52.7627182006836|	398.727325439453|	34.265251159668	|	10.0567502975464|	24.2084999084473|	26.6324577331543|	19.9756240844727|	27.5147514343262|	18.0942916870117|	1064|	189|	5|	84.3938369750977|	553	|	27|		547	|51|
|7|Bradypus variegatus	|-71.4166666666667|	12.25	|			-9999	|28.476261138916|	8.38372993469238|	73.3260116577148|	114.914047241211|	34.226749420166	|	22.793249130249	|	11.433500289917	|	28.785665512085|	27.1489162445068|	29.7602500915527|	27.0368747711182|	432	|	102|	5|	78.5265045166016|	233	|	29	|	98	|70|
|8|Bradypus variegatus	|-70.0833333333333|	0.75	|			-9999	|25.5028228759766|	9.06110382080078|	85.6801605224609|	52.3916168212891|	30.8559989929199|	20.2805004119873|	10.5754985809326|	25.3150825500488|	25.891040802002	|	26.0111656188965|	24.7291259765625|	3280|	364|	182|23.4312362670898|	1062|	618	|	626	|973|
|9|Bradypus variegatus	|-39.75|			-6.25	|			-9999	|25.156063079834|	10.2133750915527|	75.6560287475586|	105.416397094727|	32.3040008544922|	18.8042507171631|	13.4997501373291|	24.747917175293	|	25.6065845489502|	26.4520835876465|	23.9066257476807|	830	|	215|	7|	101.104278564453|	517	|	28	|	58	|148|
|10|Bradypus variegatus	|-51.9166666666667|	-16.25	|			-9999	|24.5825939178467|	12.3339786529541|	65.1170349121094|	143.526840209961|	32.556999206543	|	13.6157503128052|	18.9412498474121|	25.3168754577637|	22.6622085571289|	25.9228324890137|	22.478084564209|	1568|	289|	8|	81.0324325561523|	778	|	37	|	424|	50|

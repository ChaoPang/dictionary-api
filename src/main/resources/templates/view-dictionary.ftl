<!DOCTYPE html>
<html>
	<head>
		<title>Dictionary json</title>
		<script src="/js/jquery-1.8.3.min.js"></script>
		<script src="/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
	</head>
	<body>
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<center><h1>This is a test page for the dictionary crawler</h1></center>
				</div>
			</div>
			<div class="row">
				<div class="col-md-offset-2 col-md-8">
					<p style="font-size:14px;">There is an urgent need to standardize the semantics of biomedical data values, such as phenotypes, to enable comparative and integrative analyses. However, it is unlikely that all studies will use the same data collection protocols. As a result, retrospective standardization is often required, which involves matching of original (unstructured or locally coded) data to widely used coding or ontology systems such as SNOMED CT (clinical terms), ICD-10 (International Classification of Disease) and HPO (Human Phenotype Ontology). This data curation process is usually a time-consuming process performed by a human expert. To help mechanize this process, we have developed SORTA, a computer-aided system for rapidly encoding free text or locally coded values to a formal coding system or ontology. SORTA matches original data values (uploaded in semicolon delimited format) to a target coding system (uploaded in Excel spreadsheet, OWL ontology web language or OBO open biomedical ontologies format). It then semi- automatically shortlists candidate codes for each data value using Lucene and n-gram based matching algorithms, and can also learn from matches chosen by human experts. We evaluated SORTA's applicability in two use cases. For the LifeLines biobank, we used SORTA to recode 90 000 free text values (including 5211 unique values) about physical exercise to MET (Metabolic Equivalent of Task) codes. For the CINEAS clinical symptom coding system, we used SORTA to map to HPO, enriching HPO when necessary (315 terms matched so far). Out of the shortlists at rank 1, we found a precision/recall of 0.97/0.98 in LifeLines and of 0.58/0.45 in CINEAS. More importantly, users found the tool both a major time saver and a quality improvement because SORTA reduced the chances of human mistakes. Thus, SORTA can dramatically ease data (re)coding tasks and we believe it will prove useful for many more projects. Database URL: http://molgenis.org/sorta or as an open source download from http://www.molgenis.org/wiki/SORTA.</p>
				</div>
			</div>
		</div>
	</body>
</html>
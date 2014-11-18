<!DOCTYPE html>
<html>
	<title>Botania | vazkii.us</title>	
	<head>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="css/ripples.min.css">
        <link rel="stylesheet" href="css/material-wfont.min.css">
		<link rel="stylesheet" href="css/botania.css">
	</head>

	<body>
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		  <div class="container">
			<div class="navbar-header">
			  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			  </button>
			  <a class="navbar-brand" href="#">
				<img src="img/logo.png" />
			  </a>
			</div>
			<div class="collapse navbar-collapse">
			  <ul class="nav navbar-nav">
				<li id="header-btn-index" class="header-btn"><a href="./index.php">Home</a></li>
				<li id="header-btn-downloads" class="header-btn"><a href="./downloads.php">Downloads</a></li>
				<li id="header-btn-changelog" class="header-btn"><a href="./changelog.php">Changelog</a></li>
				<li id="header-btn-modpacks" class="header-btn"><a href="./modpacks.php">Modpacks</a></li>
				<li id="header-btn-credits" class="header-btn"><a href="./credits.php">Credits</a></li>
				<li id="header-btn-faq" class="header-btn"><a href="./faq.php">FAQ</a></li>
				<li id="header-btn-back" class="header-btn"><a href="http://vazkii.us">vazkii.us</a></li>
			  </ul>
			</div>
		  </div>
		</div>
		
		<div class="container module-contents">
			<?php require_once "module/$module.php"; ?>
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<script src="js/ripples.min.js"></script>
        <script src="js/material.min.js"></script>
		<script src="js/botania.js"></script>
		
		<?php echo "<script>\$(function(){\$('#header-btn-$module').addClass('active');});</script>"; ?>
	</body>
</html>

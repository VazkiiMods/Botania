<!DOCTYPE html>
<html>
	<title>Botania</title>	
	<head>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="css/ripples.min.css">
        <link rel="stylesheet" href="css/material-wfont.min.css">
		<link rel="stylesheet" href="css/botania.css">
		<link rel="stylesheet" href="css/minecraft-pallette.css">
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
				<li id="header-btn-license" class="header-btn"><a href="./license.php">License</a></li>
				<li id="header-btn-lexicon" class="header-btn"><a href="./lexicon.php">Lexicon</a></li>
				<li id="header-btn-faq" class="header-btn"><a href="./faq.php">FAQ</a></li>
				<li id="header-btn-back" class="header-btn"><a href="http://vazkii.us">vazkii.us</a></li>
			  </ul>
			</div>
		  </div>
		</div>
		
		<div class="container module-contents">
			<?php require_once "module/$module.php"; ?>
		</div>
		
		<footer class="footer">
			<div class="container text-muted">
				Botania (this website included) is licensed under the <a href="license.php">Botania License</a>.<br>
				Powered by <a href="http://fezvrasta.github.io/bootstrap-material-design/">Bootstrap Material Design</a> by <a href="https://github.com/FezVrasta">Fez Vrasta</a>.<br>
				Discuss Botania on the <a href="http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1294116-botania-an-innovative-natural-magic-themed-tech">Minecraft Forums</a> or the <a href="http://forum.feed-the-beast.com/threads/botania-an-innovative-natural-magic-themed-tech-mod-not-in-beta-any-more.41662/">FTB Forums</a>.<br>
				Misc Links: <a href="https://twitter.com/Vazkii">Twitter</a> <b>|</b> <a href="http://www.patreon.com/Vazkii">Patreon</a> <b>|</b> <a href="https://github.com/Vazkii/Botania">Github</a> <b>|</b> <a href="http://webchat.esper.net/?channels=vazkii">IRC</a><br><br>
				<i>Add the colors to the big sight, before the world turns black and white. Don't let unity cause monotony, chante avec moi, bring the symphony.</i> - <a href="https://www.youtube.com/watch?v=zkLJoFp2UAE">Mitchie M</a>
			</div>
		</footer>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<script src="js/ripples.min.js"></script>
        <script src="js/material.min.js"></script>
		<script src="js/botania.js"></script>
		
		<?php echo "<script>\$(function(){var module='$module';var btn=\$('#header-btn-'+module);var txt=btn.text();btn.addClass('active');document.title+=(' - '+txt);});</script>"; ?>
	</body>
</html>

<!DOCTYPE html>
<html>
	<title>Downloads - Botania | vazkii.us</title>	
	<head>
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="page.css">
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
				<img src="img/logo.png" class="shaded"/>
			  </a>
			</div>
			<div class="collapse navbar-collapse">
			  <ul class="nav navbar-nav">
				<li><a href="./index.php">Home</a></li>
				<li class="active"><a href="./downloads.php">Downloads</a></li>
				<li><a href="./changelog.php">Changelog</a></li>
			  </ul>
			</div>
		  </div>
		</div>
		
		<div class="container">
			<div class="content">
				<?php
					include 'dl.php';
				
					$files = scandir('files/');
					natsort($files);
					$files = array_reverse($files);
					$versions = parse_ini_file('versions.ini');
					$first = true;
					$total_dls = 0;
					$downloads_str = '';
					
					foreach($files as $file)
						if(strlen($file) > 2) {
							$dls_num = get_dls($file);
							$total_dls += intval($dls_num);
							$dls = number_format($dls_num);
							$button_type = 'old-dl';
							$cl_button_type = 'btn-sm';
														
							$file_version = substr($file, strlen('Botania '));
							$version_name = substr($file_version, 0, strlen($file_version) - strlen('.jar'));
							$mc_version = $versions[$version_name];
							
							$dls_txt = 'Downloads';
							if($dls_num == 1)
								$dls_txt = 'Download';
							
							if($first) {
								$button_type = 'btn-lg';
								$cl_button_type = '';
							}
							
							$downloads_str .= "<a href='dl.php?file=$file' class='btn $button_type btn-success dl'><b>$file</b></a> <a href='changelog.php#$version_name' class='btn $cl_button_type btn-info'><b>Changelog</b></a> <a class='btn $cl_button_type btn-danger dl-counter'>Minecraft <b>$mc_version</b></a> <a href='dl.php?file=$file' class='btn $cl_button_type btn-warning dl-counter'><b>$dls</b> $dls_txt</a>";
							
							if($first) {
								$downloads_str .= '<br><br><hr><font size="5">Old Versions</font><br><br>';
								$first = false;
							} else $downloads_str .= '<br>';
						}
					
					$total_dl_str = number_format($total_dls);
					print("<h3>Botania has been downloaded a total of <b>$total_dl_str</b> times.</h3>");
					print($downloads_str);
				?>
			</div>
		</div>
		
		<div id="footer">
			<a href="https://twitter.com/Vazkii"><img class="padded" src="img/twitter.png" title="@Vazkii on Twitter"></img>
			<a href="http://www.patreon.com/Vazkii"><img class="padded" src="img/patreon.png" title="Support me on Patreon"></img>
			<a href="https://github.com/Vazkii/Botania"><img class="padded" src="img/github.png" title="Botania is Open Source and available on Github!"></img>
			<a href="http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB"><img class="padded" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" title="Botania is licensed under a CC 3.0 BY-NC-SA license"></img></a>
			<a href="http://www.minecraftforum.net/topic/2440071-"><img class="padded" src="img/mcf.png" title="Discuss Botania on the MinecraftForums thread"></img>
			<a href="http://forum.feed-the-beast.com/threads/.41662"><img class="padded" src="img/ftb.png" title="Discuss Botania on the FeedTheBeast thread"></img>
			<a href="http://webchat.esper.net/?channels=vazkii"><img class="padded" src="img/irc.png" title="Discuss Botania on IRC (#vazkii on EsperNet)"></img>
		</div>

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	</body>
</html>
<!DOCTYPE html>
<html>
	<title>Downloads - Botania | vazkii.us</title>	
	<head>
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="page.css">
	</head>

	<body>
		<?php include 'incl/header.php'; ?>

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
					$last_version = '';
					$old = false;
					
					foreach($files as $file)
						if(strlen($file) > 2) {
							$dls_num = get_dls($file);
							$total_dls += intval($dls_num);
							$dls = number_format($dls_num);
							$button_type = 'old-dl';
							$cl_button_type = 'btn-sm';
														
							$file_version = substr($file, strlen('Botania '));
							$version_name = str_replace('.', '-', substr($file_version, 0, strlen($file_version) - strlen('.jar')));
							$mc_version = $versions[$version_name];
							
							$dls_txt = 'Downloads';
							if($dls_num == 0)
								$dls_txt = 'Downloads :(';
							elseif($dls_num == 1)
								$dls_txt = 'Download';
							
							if($first) {
								$button_type = 'btn-lg';
								$cl_button_type = '';
							}
							
							if($last_version != $mc_version && !$first)
								$downloads_str .= "<br><font size='4'>Minecraft <b>$mc_version</b></font><br>";
							
							$downloads_str .= "<a href='dl.php?file=$file' class='btn $button_type btn-success dl'><b><span class='glyphicon glyphicon-download'></span> $file</b></a> <a href='changelog.php#$version_name' class='btn $cl_button_type btn-info'><b>Changelog</b></a> <a class='btn $cl_button_type btn-danger dl-counter'>Minecraft <b>$mc_version</b></a> <a href='dl.php?file=$file' class='btn $cl_button_type btn-warning dl-counter'><b>$dls</b> $dls_txt</a>";
							
							if($first) {
								$downloads_str .= '<br><br><hr><font size="6">Old Versions</font><br>';
								$first = false;
							} else {
								$downloads_str .= '<br>';
								$last_version = $mc_version;
							}
						}
					
					$total_dl_str = number_format($total_dls);
					print("<div class='total-dls'><span class='glyphicon glyphicon-star'></span> Botania has been downloaded a total of <b>$total_dl_str</b> times.</div>Botania is under a Creative Commons BY-NC-SA License, more info (for modpacks and other purposes) can be found in the footer.<br><span class='warning-txt'>Note that as of beta-22, Botania requires the <a href='http://www.minecraftforum.net/topic/2535073-'>Baubles API</a>. If you have Thaumcraft installed, though, this is downloaded automatically.</span><br><br><font size='5'>Latest Download</font><br>");
					print($downloads_str);
				?>
			</div>
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<script src="jquery_cookie.min.js"></script>
		<script> $(function() { $('#header-btn-downloads').addClass('active'); }); </script>
		<?php include 'incl/footer.php'; ?>	
	</body>
</html>
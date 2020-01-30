<!DOCTYPE html>
<html>
	<title>Changelog - Botania | vazkii.us</title>
	
	<head>
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="page.css">
	</head>

	<body>
		<?php include 'incl/header.php'; ?>
		
		<div class="container">
			<div class="content">
				<font size="6">Botania Changelog</font><br>
				<?php
					$versions = array();
					$file = file('changelog.txt');
					$changelog = '';
					$first = true;
					
					foreach($file as $line) {
						if(strpos($line, '*') !== 0) {
							$key = trim(str_replace('.', '-', str_replace(' ', '-', $line)));
							$version = ucfirst(trim($line));
							
							array_push($versions, $key);
							if(!$first)
								$changelog .= '<br><div class="btn btn-xs btn-info hashlink">Back to top</div>';
							$changelog .= "<hr><div id='$key-fake'><h3>$version</h3></div>";
							$first = false;
						} else {
							$change = substr($line, 1);
							$changelog .= "<li>$change</li>";
						}
					}
					
					echo('<div class="btn-group"><button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">Version Index <span 
class="caret"></span></button><ul class="dropdown-menu">');
					foreach($versions as $version)
						echo("<li class='hashlink'><a>$version</a></li>");
					echo('</ul></div>');
					
					$changelog .= '<br><div class="btn btn-xs btn-info hashlink">Back to top</div>';
					echo($changelog);
				?>
			</div>
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<script src="jquery_cookie.min.js"></script>
		<script> $(function() { $('#header-btn-changelog').addClass('active'); }); </script>
		<?php include 'incl/footer.php'; ?>
	</body>
</html>
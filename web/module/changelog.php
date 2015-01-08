<div class='section-header'>
	<span class='glyphicon glyphicon-tag'></span>
	Botania Changelog
</div>

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
			add_buttons();
			$changelog .= "<hr><div id='$key-fake'><h3>$version</h3></div>";
			$first = false;
			$href_version = trim(str_lreplace(' ', '-', $line));
		} else {
			$change = substr($line, 1);
			$changelog .= "<li>$change</li>";
		}
	}
	
	echo('<div class="btn-group"><button type="button" class="btn btn-material-lightgreen dropdown-toggle" data-toggle="dropdown">Version Index <span 
class="caret"></span></button><ul class="dropdown-menu">');
	foreach($versions as $version)
		echo("<li class='hashlink'><a>$version</a></li>");
	echo('</ul></div>');
	
	add_buttons();
	echo($changelog);
	
	function add_buttons() {
		global $changelog, $href_version;
		$changelog .= "<br><div class='btn btn-flat btn-material-cyan hashlink'>Back to top</div><a href='dl.php?file=Botania%20$href_version.jar'><div class='btn btn-flat btn-material-lightgreen'>Download</div></a>";
	}
	
	function str_lreplace($search, $replace, $subject) {
		$pos = strrpos($subject, $search);

		if($pos != false)
			$subject = substr_replace($subject, $replace, $pos, strlen($search));

		return $subject;
	}
?>
<?php

	// Aka my "I don't want to make a database" approach, sue me.

	if(isset($_GET['file']))
		dl_mod($_GET['file']);

	function dl_mod($mod) {
		if(strlen($mod) > 0) {
			increment_dl_counter($mod);
			header("Location: files/$mod");
		}
	}
	
	function increment_dl_counter($mod) {
		$path = "dls/$mod.txt";
		$dls = get_dls($mod);
		$dls_int = intval($dls);
		++$dls_int;
		
		file_put_contents($path, $dls_int);
	}
	
	function get_dls($mod) {
		$path = 'dls/';
		$file = "$path$mod.txt";
		
		if(file_exists($file))
			return file_get_contents($file);
		
		if(!file_exists($path))
			mkdir('dls/');
			
		file_put_contents($file, '0');
		return get_dls($mod);
	}
?>
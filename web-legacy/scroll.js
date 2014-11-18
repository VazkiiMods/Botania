$(".hashlink").click(function() {
	var hash = $(this).text();
	
	if(hash == 'Back to top')
		$('html, body').animate({  scrollTop: 0 }, 500);
	else $('html, body').animate({ scrollTop: $("#" + hash + "-fake").offset().top - 100 }, 1000);
});
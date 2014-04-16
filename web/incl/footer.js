var animating = false;

$(function() {
	var remove = $.cookie('show-footer');
	if(remove === 'true') {
		$('#footer').css( { bottom: '-50px' } );
		$('.hide-footer-btn').css( { bottom: '0px', left : '-=30px' } );
		$('.hide-footer-btn').find('.glyphicon').removeClass('glyphicon-remove').addClass('glyphicon-plus');
		$('#footer-tooltip').text('Show Footer');
	}
	
	var hash = document.location.hash.substring(1);
    $('html, body').animate({ scrollTop: $("#" + hash + "-fake").offset().top - 100 }, 1000);
});

$('.hide-footer-btn').click(function() {
	if(animating)
		return;

	animating = true;
	var remove = $(this).find('.glyphicon').hasClass('glyphicon-remove');
	$('#footer').animate(  { bottom: remove ? '-50px' : '0px' } );
	
	$(this).animate( { bottom: remove ? '0px' : '15px', left: remove ? '-=30px' : '+=30px' }, function() {
		$('#footer-tooltip').text(remove ? 'Show Footer' : 'Hide Footer');
		$.cookie('show-footer', remove, { expires: 30 });
		
		var glyph = $(this).find('.glyphicon');
		glyph.fadeOut('fast', function() {
			glyph.removeClass(remove ? 'glyphicon-remove' : 'glyphicon-plus').addClass(remove ? 'glyphicon-plus' : 'glyphicon-remove').fadeIn('fast');
			animating = false;
		});
	});
});

$('.hide-footer-btn').mouseenter(function(e) {
	$('#footer-tooltip').fadeIn('fast');
});

$('.hide-footer-btn').mouseleave(function(e) {
	$('#footer-tooltip').fadeOut();
})

$(".hashlink").click(function() {
	var hash = $(this).text();
	
	if(hash == 'Back to top')
		$('html, body').animate({  scrollTop: 0 }, 500);
	else $('html, body').animate({ scrollTop: $("#" + hash + "-fake").offset().top - 100 }, 1000);
});
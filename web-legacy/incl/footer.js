const censorChars = 'abcdefghijklmnopqrstuvwxyz';
var animating = false;

$(function() {
	setInterval(changeCensor, 100);

	var remove = $.cookie('show-footer');
	if(remove === 'true') {
		$('#footer').css( { bottom: '-50px' } );
		$('.hide-footer-btn').css( { bottom: '0px', left : '-=30px' } );
		$('.hide-footer-btn').find('.glyphicon').removeClass('glyphicon-remove').addClass('glyphicon-plus');
		$('#footer-tooltip').text('Show Footer');
	}
	
	var hash = document.location.hash.substring(1);
	var offset = $("#" + hash + "-fake").offset();
	if(offset != undefined)
		$('html, body').animate({ scrollTop: offset.top - 100 }, 1000);
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
});

$('.hide-div').click(function() {
	var divname = $(this).attr('id').substring('hide-'.length);
	var div = $('#' + divname);
	if(div.is(':visible')) {
		div.hide(300);
		$(this).text('(show)');
	} else {
		div.show(300);
		$(this).text('(hide)');
	}
	
});

$(".hashlink").click(function() {
	var hash = $(this).text();
	
	if(hash == 'Back to top' || hash == '(back to top)')
		$('html, body').animate({  scrollTop: 0 }, 500);
	else $('html, body').animate({ scrollTop: $("#" + hash + "-fake").offset().top - 100 }, 1000);
});

$(".moe-scroll").click(function() {
	$('html, body').animate({ scrollTop: $("#moe-counter").offset().top - 100 }, 1000);
});

function changeCensor() {
	$('.censored').each(function() {
		$(this).text(randomStr($(this).text().length));
	});
}

function randomStr(length) {
	var str = '';
	for(var i = 0; i < length; i++)
		str += censorChars.charAt(Math.floor(Math.random() * censorChars.length));
	return str;
}
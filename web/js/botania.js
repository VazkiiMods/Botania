const censorChars = 'abcdefghijklmnopqrstuvwxyz';
var animating = false;

$(function() {
	setInterval(changeCensor, 100);
	
	var hash = document.location.hash.substring(1);
	var offset = $("#" + hash + "-fake").offset();
	if(offset != undefined)
		$('html, body').animate({ scrollTop: offset.top - 72 }, 1000);
		
	$.material.init();
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
		$('html, body').delay(hash == 'Back to top' ? 400 : 0).animate({  scrollTop: 0 }, 500);
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
$(function() {
	$(".list-group-item").on('click', function() {
		if ($(this).find("ul")) {
			$(this).toggleClass("collapsed");
			if ($(this).hasClass("collapsed")) {
				$("ul", this).hide('fast');
			} else {
				$("ul", this).show('fast');
			}
		}
	});
	$("#logoutLink").on('click', function(e) {
		e.preventDefault();
		$("#logoutForm").submit();
	});
	$("#toMainmenu").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/to/mainmenu');
	});
	$("#toMainmenu2").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/to/mainmenu');
	});
	$("#toAdmin").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/employee/to/pages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#toRole").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/role/to/pages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#toMenu").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/menu/initial');
	});
	$("#toPages").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/employee/to/pages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
});
function checkPermissionAndTransfer(stringUrl) {
	let ajaxResult = $.ajax({
		url: stringUrl,
		type: 'GET',
		async: false
	});
	if (ajaxResult.status === 200) {
		window.location.replace(stringUrl);
	} else {
		layer.msg(ajaxResult.responseJSON.message);
	}
}
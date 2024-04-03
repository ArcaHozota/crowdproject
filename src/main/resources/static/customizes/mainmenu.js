$(document).ready(function() {
	$("#toMainmenu").css('color', '#7F0020');
});
$("#categoryKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/category/initial';
	checkPermissionAndTransfer(url);
});
$("#roleKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/role/to/pages?pageNum=1';
	checkPermissionAndTransfer(url);
});
$("#adminKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/employee/to/pages?pageNum=1';
	checkPermissionAndTransfer(url);
});
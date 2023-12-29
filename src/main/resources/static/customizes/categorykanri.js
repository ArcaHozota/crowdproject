$(document).ready(function() {
	$("#businessKanri").removeClass('collapsed');
	$("ul", $("#businessKanri")).show('fast');
	$("#toCategory").css('color', 'darkred');
	let treeData = [
		{
			text: "分類管理",
			icon: "bi bi-list",
			expanded: true,
			nodes: [
				{
					text: "地域一覧",
					icon: "bi bi-globe-americas"
				},
				{
					text: "都市一覧",
					icon: "bi bi-building-fill-check"
				},
				{
					text: "駅一覧",
					icon: "bi bi-buildings-fill"
				}
			]
		}
	];
	$('#categroyTreeView').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 2,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
});
$("#categroyTreeView").on('click', '.list-group-item', function() {
	let url;
	let titleName = $(this).text();
	switch (titleName) {
		case "地域一覧":
			url = '/pgcrowd/category/to/districtPages';
			break;
		case "都市一覧":
			url = '/pgcrowd/category/to/cityPages';
			break;
		case "駅一覧":
			url = '/pgcrowd/category/to/stationPages';
			break;
	}
	checkPermissionAndTransfer(url);
});
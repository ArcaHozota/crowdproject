$(document).ready(function() {
	$("#adminKanri").removeClass('collapsed');
	$("ul", $("#adminKanri")).show('fast');
	$("#toMenu").css('color', 'darkred');
	let treeData = [
		{
			text: "社員管理",
			icon: "bi bi-person-circle",
			expanded: true,
			nodes: [
				{
					text: "社員情報追加",
					icon: "bi bi-person-fill-add"
				},
				{
					text: "社員情報一覧",
					icon: "bi bi-person-vcard"
				}
			]
		},
		{
			text: "役割管理",
			icon: "bi bi-person-badge-fill",
			expanded: true,
			nodes: [
				{
					text: "役割情報一覧",
					icon: "bi bi-person-vcard-fill"
				}
			]
		},
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
	$('#treeView').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 2,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
});
$("#treeView").on('click', '.list-group-item', function() {
	let url;
	let titleName = $(this).text();
	switch (titleName) {
		case "社員情報追加":
			url = '/pgcrowd/employee/to/addition';
			break;
		case "社員情報一覧":
			url = '/pgcrowd/employee/to/pages?pageNum=1';
			break;
		case "役割情報一覧":
			url = '/pgcrowd/role/to/pages?pageNum=1';
			break;
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
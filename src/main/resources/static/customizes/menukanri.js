$(document).ready(function() {
	$("#toMenu").css('color', 'darkred');
	let treeData = [
		{
			text: "社員管理",
			icon: "bi bi-person-circle",
			expanded: true,
			nodes: [
				{
					id: "employeeAddTree",
					text: "社員情報追加",
					icon: "bi bi-person-fill-add"
				},
				{
					id: "employeeQueryTree",
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
					id: "roleQueryTree",
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
					id: "districtQueryTree",
					text: "地域一覧",
					icon: "bi bi-globe-americas"
				},
				{
					id: "cityQueryTree",
					text: "都市一覧",
					icon: "bi bi-building-fill-check"
				},
				{
					id: "stationQueryTree",
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
	$("#employeeAddTree").on('click', function() {
		let url = '/pgcrowd/employee/to/addition';
		checkPermissionAndTransfer(url);
	});
	$("#employeeQueryTree").on('click', function() {
		let url = '/pgcrowd/employee/to/pages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#roleQueryTree").on('click', function() {
		let url = '/pgcrowd/role/to/pages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#districtQueryTree").on('click', function() {
		let url = '/pgcrowd/category/to/districtPages';
		checkPermissionAndTransfer(url);
	});
	$("#cityQueryTree").on('click', function() {
		let url = '/pgcrowd/category/to/cityPages';
		checkPermissionAndTransfer(url);
	});
	$("#stationQueryTree").on('click', function() {
		let url = '/pgcrowd/category/to/stationPages';
		checkPermissionAndTransfer(url);
	});
});
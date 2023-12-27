$(document).ready(function() {
	$("#adminKanri").removeClass('collapsed');
	$("ul", $("#adminKanri")).show('fast');
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
	}
	checkPermissionAndTransfer(url);
});
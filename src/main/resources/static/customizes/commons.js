$(function() {
	let treeData = [
		{
			id: "toMainmenu",
			text: "コントロールパネル",
			icon: "bi bi-speedometer",
		},
		{
			text: "権限管理",
			icon: "bi bi-bank2",
			expanded: true,
			nodes: [
				{
					id: "toAdmin",
					text: "ユーザ管理",
					icon: "bi bi-people-fill"
				},
				{
					id: "toRole",
					text: "役割管理",
					icon: "bi bi-list-check"
				},
				{
					id: "toMenu",
					text: "メニュー管理",
					icon: "bi bi-menu-down"
				}
			]
		},
		{
			text: "ビジネス管理",
			icon: "bi bi-boombox-fill",
			expanded: true,
			nodes: [
				{
					id: "toCertification",
					text: "資格維持",
					icon: "bi bi-check-circle-fill"
				},
				{
					id: "toCategory",
					text: "分類管理",
					icon: "bi bi-list",
					expanded: true,
					nodes: [
						{
							id: "toDistrict",
							text: "地域一覧",
							icon: "bi bi-globe-americas"
						},
						{
							id: "toCity",
							text: "都市一覧",
							icon: "bi bi-building-fill-check"
						},
						{
							id: "toStation",
							text: "駅一覧",
							icon: "bi bi-buildings-fill"
						}
					]
				}
			]
		}
	];
	$('#mainmenuTree').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 1.25,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
	$("#logoutBtn").on('click', function() {
		swal.fire({
			title: 'メッセージ',
			text: 'ログアウトしてよろしいでしょうか。',
			icon: 'question',
			showDenyButton: true,
			denyButtonText: 'いいえ',
			confirmButtonText: 'はい',
			confirmButtonColor: '#7F0020',
			denyButtonColor: '#003153'
		}).then((result) => {
			if (result.isConfirmed) {
				$("#logoutForm").submit();
			} else if (result.isDenied) {
				$(this).close();
			}
		});
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
	$("#toPersonal").on('click', function(e) {
		e.preventDefault();
		let userId = $(this).find("p").text();
		let url = '/pgcrowd/employee/to/edition?editId=' + userId + '&userId=' + userId;
		checkPermissionAndTransfer(url);
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
	$("#toCategory").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/initial';
		checkPermissionAndTransfer(url);
	});
	$("#toDistrict").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/to/districtPages';
		checkPermissionAndTransfer(url);
	});
	$("#toCity").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/to/cityPages';
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
function buildPageInfos(result) {
	let pageInfos = $("#pageInfos");
	pageInfos.empty();
	pageNum = result.data.pageNum;
	totalPages = result.data.totalPages;
	totalRecords = result.data.totalRecords;
	pageInfos.append(totalPages + "ページ中の" + pageNum + "ページ、" + totalRecords + "件のレコードが見つかりました。");
}
function buildPageNavi(result) {
	$("#pageNavi").empty();
	let ul = $("<ul></ul>").addClass("pagination");
	let firstPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("最初へ").attr("href", "#"));
	let previousPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("&laquo;").attr("href", "#"));
	if (!result.data.hasPreviousPage) {
		firstPageLi.addClass("disabled");
		previousPageLi.addClass("disabled");
	} else {
		firstPageLi.click(function() {
			toSelectedPg(1, keyword);
		});
		previousPageLi.click(function() {
			toSelectedPg(pageNum - 1, keyword);
		});
	}
	let nextPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("&raquo;").attr("href", "#"));
	let lastPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("最後へ").attr("href", "#"));
	if (!result.data.hasNextPage) {
		nextPageLi.addClass("disabled");
		lastPageLi.addClass("disabled");
	} else {
		lastPageLi.addClass("success");
		nextPageLi.click(function() {
			toSelectedPg(pageNum + 1, keyword);
		});
		lastPageLi.click(function() {
			toSelectedPg(totalPages, keyword);
		});
	}
	ul.append(firstPageLi).append(previousPageLi);
	$.each(result.data.navigatePageNums, (index, item) => {
		let numsLi = $("<li class='page-item'></li>").append(
			$("<a class='page-link'></a>").append(item).attr("href", "#"));
		if (pageNum === item) {
			numsLi.attr("href", "#").addClass("active");
		}
		numsLi.click(function() {
			toSelectedPg(item, keyword);
		});
		ul.append(numsLi);
	});
	ul.append(nextPageLi).append(lastPageLi);
	$("<nav></nav>").append(ul).appendTo("#pageNavi");
}
function formReset(element) {
	$(element)[0].reset();
	$(element).find(".form-control").removeClass("is-valid is-invalid");
	$(element).find(".form-select").removeClass("is-valid is-invalid");
	$(element).find(".form-text").removeClass("valid-feedback invalid-feedback");
	$(element).find(".form-text").text("");
}
function showValidationMsg(element, status, msg) {
	$(element).removeClass("is-valid is-invalid");
	$(element).next("span").removeClass("valid-feedback invalid-feedback");
	$(element).next("span").text("");
	if (status === "success") {
		$(element).addClass("is-valid");
		$(element).next("span").addClass("valid-feedback");
	} else {
		$(element).addClass("is-invalid");
		$(element).next("span").addClass("invalid-feedback").text(msg);
	}
}
function pgcrowdAjaxModify(url, type, data, successFunction) {
	let header = $('meta[name=_csrf_header]').attr('content');
	let token = $('meta[name=_csrf_token]').attr('content');
	$.ajax({
		url: url,
		type: type,
		data: data,
		headers: {
			[header]: token
		},
		dataType: 'json',
		contentType: 'application/json;charset=UTF-8',
		success: successFunction,
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
}
function pgcrowdNullInputboxDiscern(inputArrays) {
	for (const element of inputArrays) {
		if ($(element).val().trim() === "") {
			showValidationMsg(element, "error", "上記の入力ボックスを空になってはいけません。");
		}
	}
}
function pgcrowdInputContextGet(inputArrays) {
	let listArray = [];
	for (const element of inputArrays) {
		let inputContext = $(element).val().trim();
		listArray.push(inputContext);
		showValidationMsg(element, "success", "");
	}
	return listArray;
}
function normalPostSuccessFunction(element) {
	$(element).modal('hide');
	layer.msg('追加処理成功');
	toSelectedPg(totalRecords, keyword);
}
function normalDeleteSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		layer.msg('削除済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}
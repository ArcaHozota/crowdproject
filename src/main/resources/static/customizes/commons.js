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
	$("#toCategory").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/initial';
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
	} else if (status === "error") {
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
function pgcrowdNullInputboxDiscern(listArray) {
	for (const element of listArray) {
		if ($(element).val().trim() === "") {
			showValidationMsg(element, "error", "上記の入力ボックスを空になってはいけません。");
		}
	}
}
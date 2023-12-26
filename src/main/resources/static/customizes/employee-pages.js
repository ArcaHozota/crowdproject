let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#adminKanri").removeClass('collapsed');
	$("ul", $("#adminKanri")).show('fast');
	toSelectedPg(1, keyword);
});
$("#searchBtn2").on('click', function() {
	keyword = $("#keywordInput").val();
	toSelectedPg(1, keyword);
});
function toSelectedPg(pageNum, keyword) {
	$.ajax({
		url: '/pgcrowd/employee/pagination',
		data: {
			'pageNum': pageNum,
			'keyword': keyword
		},
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			buildTableBody(result);
			buildPageInfos(result);
			buildPageNavi(result);
		},
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
}
function buildTableBody(result) {
	$("#tableBody").empty();
	let index = result.data.records;
	$.each(index, (index, item) => {
		let idTd = $("<th scope='row' class='text-center' style='width:70px;vertical-align:bottom;'></th>").append(item.id);
		let usernameTd = $("<td scope='row' class='text-center' style='width:120px;vertical-align:bottom;'></td>").append(item.username);
		let emailTd = $("<td scope='row' class='text-center' style='width:120px;vertical-align:bottom;'></td>").append(item.email);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn")
			.append($("<i class='bi bi-pencil-fill'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='bi bi-trash-fill'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center' style='width:120px;vertical-align:bottom;'></td>").append(editBtn).append(" ").append(deleteBtn);
		$("<tr></tr>").append(idTd).append(usernameTd).append(emailTd).append(btnTd).appendTo("#tableBody");
	});
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
		$("<a class='page-link'></a>").append("最初のページへ").attr("href", "#"));
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
		$("<a class='page-link'></a>").append("最後のページへ").attr("href", "#"));
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
$("#loginAccountInput").change(function() {
	let inputLoginAccount = this.value;
	if (inputLoginAccount === "") {
		showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else {
		$.ajax({
			url: '/pgcrowd/employee/check',
			data: 'loginAcct=' + inputLoginAccount,
			type: 'GET',
			success: function(result) {
				if (result.status === 'SUCCESS') {
					showValidationMsg("#loginAccountInput", "success", "√");
					$("#saveInfoBtn").attr("ajax-va", "success");
				} else {
					showValidationMsg("#loginAccountInput", "error", result.message);
					$("#saveInfoBtn").attr("ajax-va", "error");
				}
			}
		});
	}
});
$("#usernameInput").change(function() {
	let inputUsername = this.value;
	if (inputUsername === "") {
		showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#usernameInput", "success", "√");
		$("#saveInfoBtn").attr("ajax-va", "success");
	}
});
$("#passwordInput").change(function() {
	let inputPassword = this.value;
	let regularPassword = /^[a-zA-Z-\d]{8,23}$/;
	if (inputPassword === "") {
		showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else if (!regularPassword.test(inputPassword)) {
		showValidationMsg("#passwordInput", "error", "入力したパスワードが8桁から23桁までの英数字にしなければなりません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#passwordInput", "success", "√");
		$("#saveInfoBtn").attr("ajax-va", "success");
	}
});
$("#emailInput").change(function() {
	let inputEmail = this.value;
	let regularEmail = /^^[a-zA-Z-\d._%+-]+@[a-zA-Z-\d.-]+\.[a-zA-Z]{2,}$/;
	if (inputEmail === "") {
		showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else if (!regularEmail.test(inputEmail)) {
		showValidationMsg("#emailInput", "error", "入力したメールアドレスが正しくありません。");
		$("#saveInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#emailInput", "success", "√");
		$("#saveInfoBtn").attr("ajax-va", "success");
	}
});
$("#saveInfoBtn").on('click', function() {
	let inputLoginAccount = $("#loginAccountInput").val().trim();
	let inputUsername = $("#usernameInput").val().trim();
	let inputPassword = $("#passwordInput").val().trim();
	let inputEmail = $("#emailInput").val().trim();
	let inputRole = $("#roleInput").val();
	if ($(this).attr("ajax-va") === "error") {
		return false;
	} else if (inputLoginAccount === "" || inputUsername === "" || inputPassword === "" || inputEmail === "") {
		if (inputLoginAccount === "" && inputUsername === "" && inputPassword === "" && inputEmail === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputLoginAccount === "" && inputUsername === "" && inputPassword === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
		} else if (inputLoginAccount === "" && inputUsername === "" && inputEmail === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputLoginAccount === "" && inputPassword === "" && inputEmail === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputUsername === "" && inputPassword === "" && inputEmail === "") {
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputPassword === "" && inputEmail === "") {
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputLoginAccount === "" && inputUsername === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
		} else if (inputLoginAccount === "" && inputEmail === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputUsername === "" && inputPassword === "") {
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
		} else if (inputUsername === "" && inputEmail === "") {
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		} else if (inputLoginAccount === "" && inputPassword === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
		} else if (inputLoginAccount === "") {
			showValidationMsg("#loginAccountInput", "error", "ログインアカウントを空になってはいけません。");
		} else if (inputUsername === "") {
			showValidationMsg("#usernameInput", "error", "ユーザ名称を空になってはいけません。");
		} else if (inputPassword === "") {
			showValidationMsg("#passwordInput", "error", "パスワードを空になってはいけません。");
		} else {
			showValidationMsg("#emailInput", "error", "メールアドレスを空になってはいけません。");
		}
	} else {
		let header = $('meta[name=_csrf_header]').attr('content');
		let token = $('meta[name=_csrf_token]').attr('content');
		$.ajax({
			url: '/pgcrowd/employee/infosave',
			type: 'POST',
			dataType: 'json',
			data: JSON.stringify({
				'loginAccount': inputLoginAccount,
				'username': inputUsername,
				'password': inputPassword,
				'email': inputEmail,
				'roleId': inputRole
			}),
			headers: {
				[header]: token
			},
			contentType: 'application/json;charset=UTF-8',
			success: function() {
				window.location.replace('/pgcrowd/employee/to/pages?pageNum=' + totalRecords);
			},
			error: function(result) {
				layer.msg(result.responseJSON.message);
			}
		});
	}
});
$("#addInfoBtn").on('click', function(e) {
	e.preventDefault();
	$.ajax({
		url: '/pgcrowd/employee/to/addition',
		type: 'GET',
		dataType: 'json',
		success: function() {
			window.location.replace('/pgcrowd/employee/to/addition');
		},
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
});
$("#tableBody").on('click', '.delete-btn', function() {
	let userName = $(this).parents("tr").find("td:eq(0)").text().trim();
	let userId = $(this).attr("deleteId");
	let header = $('meta[name=_csrf_header]').attr('content');
	let token = $('meta[name=_csrf_token]').attr('content');
	if (confirm("この" + userName + "という社員の情報を削除するとよろしいでしょうか。")) {
		$.ajax({
			url: '/pgcrowd/employee/delete/' + userId,
			type: 'DELETE',
			dataType: 'json',
			headers: {
				[header]: token
			},
			success: function() {
				layer.msg('削除済み');
				toSelectedPg(pageNum, keyword);
			},
			error: function(result) {
				layer.msg(result.responseJSON.message);
			}
		});
	}
});
$("#tableBody").on('click', '.edit-btn', function() {
	let editId = $(this).attr("editId");
	$.ajax({
		url: '/pgcrowd/employee/to/edition?editId=1',
		type: 'GET',
		dataType: 'json',
		success: function() {
			window.location.replace('/pgcrowd/employee/to/edition?editId=' + editId);
		},
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
});
$("#usernameEdit").change(function() {
	let editUsername = this.value;
	if (editUsername === "") {
		showValidationMsg("#usernameEdit", "error", "ユーザ名称を空になってはいけません。");
		$("#editInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#usernameEdit", "success", "√");
		$("#editInfoBtn").attr("ajax-va", "success");
	}
});
$("#passwordEdit").change(function() {
	let editPassword = this.value;
	let regularPassword = /^[a-zA-Z-\d]{8,23}$/;
	if (editPassword === "") {
		showValidationMsg("#passwordEdit", "error", "パスワードを空になってはいけません。");
		$("#editInfoBtn").attr("ajax-va", "error");
	} else if (!regularPassword.test(editPassword)) {
		showValidationMsg("#passwordEdit", "error", "入力したパスワードが8桁から23桁までの英数字にしなければなりません。");
		$("#editInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#passwordEdit", "success", "√");
		$("#editInfoBtn").attr("ajax-va", "success");
	}
});
$("#emailEdit").change(function() {
	let editEmail = this.value;
	let regularEmail = /^^[a-zA-Z-\d._%+-]+@[a-zA-Z-\d.-]+\.[a-zA-Z]{2,}$/;
	if (editEmail === "") {
		showValidationMsg("#emailEdit", "error", "メールアドレスを空になってはいけません。");
		$("#editInfoBtn").attr("ajax-va", "error");
	} else if (!regularEmail.test(editEmail)) {
		showValidationMsg("#emailEdit", "error", "入力したメールアドレスが正しくありません。");
		$("#editInfoBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#emailEdit", "success", "√");
		$("#editInfoBtn").attr("ajax-va", "success");
	}
});
$("#editInfoBtn").on('click', function() {
	let editId = $("#editId").text();
	let editLoginAccount = $("#loginAccountEdit").text();
	let editUsername = $("#usernameEdit").val().trim();
	let editPassword = $("#passwordEdit").val().trim();
	let editEmail = $("#emailEdit").val().trim();
	let editRole = $("#roleEdit option:selected").val();
	if ($(this).attr("ajax-va") === "error") {
		return false;
	} else {
		if (editPassword === "**************************************") {
			editPassword = null;
		}
		let header = $('meta[name=_csrf_header]').attr('content');
		let token = $('meta[name=_csrf_token]').attr('content');
		$.ajax({
			url: '/pgcrowd/employee/infoupd',
			type: 'PUT',
			dataType: 'json',
			data: JSON.stringify({
				'id': editId,
				'loginAccount': editLoginAccount,
				'username': editUsername,
				'password': editPassword,
				'email': editEmail,
				'roleId': editRole
			}),
			headers: {
				[header]: token
			},
			contentType: 'application/json;charset=UTF-8',
			success: function() {
				window.location.replace('/pgcrowd/employee/to/pages?pageNum=' + pageNum);
			},
			error: function(result) {
				layer.msg(result.responseJSON.message);
			}
		});
	}
});
$("#resetBtn").on('click', function() {
	formReset($("#inputForm"));
});
$("#restoreBtn").on('click', function() {
	let userId = $("#editId").text();
	formReset($("#editForm"));
	$.ajax({
		url: '/pgcrowd/employee/inforestore',
		data: 'userId=' + userId,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			let restoredInfo = result.data;
			$("#usernameEdit").val(restoredInfo.username);
			$("#passwordEdit").val('**************************************');
			$("#emailEdit").val(restoredInfo.email);
		}
	});
});
function formReset(element) {
	$(element)[0].reset();
	$(element).find(".form-control").removeClass("is-valid is-invalid");
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
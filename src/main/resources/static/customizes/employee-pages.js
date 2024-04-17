let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#toAdmin").css('color', '#7F0020');
	toSelectedPg(1, keyword);
	$("#toAdmin").addClass('animate__animated animate__flipInY');
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
			'keyword': keyword,
			'userId': $("#toPersonal").find("p").text(),
			'authChkFlag': $("#securityPersonalAuths").text()
		},
		type: 'GET',
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
		let idTd = $("<th scope='row' class='text-center table-light' style='width:150px;vertical-align:middle;'></th>").append(item.id);
		let usernameTd = $("<td scope='row' class='text-center table-light' style='width:70px;vertical-align:middle;'></td>").append(item.username);
		let emailTd = $("<td scope='row' class='text-center table-light' style='width:100px;vertical-align:middle;'></td>").append(item.email);
		let dateTd = $("<td scope='row' class='text-center table-light' style='width:70px;vertical-align:middle;'></td>").append(item.dateOfBirth);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn")
			.append($("<i class='fa-solid fa-pencil'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='fa-solid fa-trash'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center table-light' style='width:100px;vertical-align:middle;'></td>").append(editBtn).append(" ").append(deleteBtn);
		$("<tr></tr>").append(idTd).append(usernameTd).append(emailTd).append(dateTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#loginAccountInput").change(function() {
	$.ajax({
		url: '/pgcrowd/employee/check',
		data: 'loginAcct=' + this.value,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			if (result.status === 'SUCCESS') {
				showValidationMsg(this, "success", "√");
			} else {
				showValidationMsg(this, "error", result.message);
			}
		}
	});
});
$("#passwordInput").change(function() {
	let inputPassword = this.value;
	let regularPassword = /^[a-zA-Z\d]{8,23}$/;
	if (!regularPassword.test(inputPassword)) {
		showValidationMsg(this, "error", "入力したパスワードが8桁から23桁までの英数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "√");
	}
});
$("#emailInput").change(function() {
	let inputEmail = this.value;
	let regularEmail = /^^[a-zA-Z\d._%+-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/;
	if (!regularEmail.test(inputEmail)) {
		showValidationMsg(this, "error", "入力したメールアドレスが正しくありません。");
	} else {
		showValidationMsg(this, "success", "√");
	}
});
$("#saveInfoBtn").on('click', function() {
	let inputArrays = ["#loginAccountInput", "#usernameInput", "#passwordInput", "#emailInput"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#inputForm").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let postData = JSON.stringify({
			'loginAccount': $("#loginAccountInput").val().trim(),
			'username': $("#usernameInput").val().trim(),
			'password': $("#passwordInput").val().trim(),
			'email': $("#emailInput").val().trim(),
			'dateOfBirth': $("#dateInput").val(),
			'roleId': $("#roleInput").val()
		});
		pgcrowdAjaxModify('/pgcrowd/employee/infosave', 'POST', postData, postSuccessFunction);
	}
});
$("#addInfoBtn").on('click', function(e) {
	e.preventDefault();
	let url = '/pgcrowd/employee/to/addition';
	checkPermissionAndTransfer(url);
});
$("#tableBody").on('click', '.delete-btn', function() {
	let ajaxResult = $.ajax({
		url: '/pgcrowd/employee/delete/0L',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
	let userName = $(this).parents("tr").find("td:eq(0)").text().trim();
	let userId = $(this).attr("deleteId");
	swal.fire({
		title: 'メッセージ',
		text: 'この「' + userName + '」という社員の情報を削除するとよろしいでしょうか。',
		icon: 'question',
		showCloseButton: true,
		confirmButtonText: 'はい',
		confirmButtonColor: '#7F0020'
	}).then((result) => {
		if (result.isConfirmed) {
			pgcrowdAjaxModify('/pgcrowd/employee/delete/' + userId, 'DELETE', null, normalDeleteSuccessFunction);
		} else {
			$(this).close();
		}
	});
});
$("#tableBody").on('click', '.edit-btn', function() {
	let editId = $(this).attr("editId");
	let authChkFlag = $("#securityPersonalAuths").text();
	let url = '/pgcrowd/employee/to/edition?editId=' + editId + '&authChkFlag=' + authChkFlag;
	checkPermissionAndTransfer(url);
});
$("#passwordEdit").change(function() {
	let editPassword = this.value;
	let regularPassword = /^[a-zA-Z\d]{8,23}$/;
	if (!regularPassword.test(editPassword)) {
		showValidationMsg(this, "error", "入力したパスワードが8桁から23桁までの英数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "√");
	}
});
$("#emailEdit").change(function() {
	let editEmail = this.value;
	let regularEmail = /^^[a-zA-Z\d._%+-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/;
	if (!regularEmail.test(editEmail)) {
		showValidationMsg(this, "error", "入力したメールアドレスが正しくありません。");
	} else {
		showValidationMsg(this, "success", "√");
	}
});
$("#roleEdit").change(function() {
	let ajaxResult = $.ajax({
		url: '/pgcrowd/employee/delete/0L',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		showValidationMsg(this, "error", ajaxResult.responseJSON.message);
	} else {
		showValidationMsg(this, "success", "√");
	}
});
$("#editInfoBtn").on('click', function() {
	let inputArrays = ["#usernameEdit", "#passwordEdit", "#emailEdit"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#editForm").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let rawPassword = $("#passwordEdit").val().trim();
		if (rawPassword === "---------------------------") {
			rawPassword = null;
		}
		let roleId = $("#roleEdit").attr('value');
		if (roleId === null || roleId === undefined) {
			roleId = $("#roleEdit option:selected").val();
		}
		let putData = JSON.stringify({
			'id': $("#editId").text(),
			'loginAccount': $("#loginAccountEdit").text(),
			'username': $("#usernameEdit").val().trim(),
			'password': rawPassword,
			'email': $("#emailEdit").val().trim(),
			'dateOfBirth': $("#dateEdit").val(),
			'roleId': roleId
		});
		pgcrowdAjaxModify('/pgcrowd/employee/infoupd', 'PUT', putData, putSuccessFunction);
	}
});
function postSuccessFunction() {
	window.location.replace('/pgcrowd/employee/to/pages?pageNum=' + totalRecords);
}
function putSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		window.location.replace('/pgcrowd/employee/to/pages?pageNum=' + pageNum);
	} else {
		layer.msg(result.message);
	}
}
$("#resetBtn").on('click', function() {
	formReset($("#inputForm"));
});
$("#restoreBtn").on('click', function() {
	let editId = $("#editId").text();
	formReset($("#editForm"));
	$.ajax({
		url: '/pgcrowd/employee/inforestore',
		data: 'editId=' + editId,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			let restoredInfo = result.data;
			$("#usernameEdit").val(restoredInfo.username);
			$("#passwordEdit").val(restoredInfo.password);
			$("#emailEdit").val(restoredInfo.email);
			$("#dateEdit").val(restoredInfo.dateOfBirth);
		}
	});
});
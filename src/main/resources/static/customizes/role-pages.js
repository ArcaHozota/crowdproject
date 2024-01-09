let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#toRole").css('color', 'darkgreen');
	toSelectedPg(1, keyword);
});
$("#searchBtn2").on('click', function() {
	keyword = $("#keywordInput").val();
	toSelectedPg(1, keyword);
});
function toSelectedPg(pageNum, keyword) {
	$.ajax({
		url: '/pgcrowd/role/pagination',
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
		let idTd = $("<th scope='row' class='text-center table-light' style='width:150px;vertical-align:middle;'></th>").append(item.id);
		let nameTd = $("<td scope='row' class='text-center table-light' style='width:100px;vertical-align:middle;'></td>").append(item.name);
		let fuyoBtn = $("<button></button>").addClass("btn btn-success btn-sm fuyo-btn")
			.append($("<i class='bi bi-check2-circle'></i>")).append("権限付与");
		fuyoBtn.attr("fuyoId", item.id);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn")
			.append($("<i class='bi bi-pencil-fill'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='bi bi-trash-fill'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center table-light' style='width:100px;vertical-align:middle;'></td>").append(fuyoBtn).append(" ").append(editBtn).append(" ").append(deleteBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#addRoleBtn").on('click', function() {
	formReset("#roleAddModal form");
	let addModal = new bootstrap.Modal($("#roleAddModal"), {
		backdrop: 'static'
	});
	addModal.show();
});
$("#nameInput").on('change', function() {
	$.ajax({
		url: '/pgcrowd/role/check',
		data: 'name=' + this.value,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			if (result.status === 'SUCCESS') {
				showValidationMsg(this, "success", "");
			} else {
				showValidationMsg(this, "error", result.message);
			}
		}
	});
});
$("#roleInfoSaveBtn").on('click', function() {
	let inputArrays = ["#nameInput"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#roleAddModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let postData = JSON.stringify({
			'name': $("#nameInput").val().trim()
		});
		pgcrowdAjaxModify('pgcrowd/role/infosave', 'POST', postData, normalPostSuccessFunction("#roleAddModal"));
	}
});
$("#tableBody").on('click', '.edit-btn', function() {
	formReset("#roleEditModal form");
	let editId = $(this).attr("editId");
	$("#roleInfoChangeBtn").val(editId);
	$("#idEdit").text(editId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	$("#nameEdit").val(nameVal);
	let editModal = new bootstrap.Modal($("#roleEditModal"), {
		backdrop: 'static'
	});
	editModal.show();
});
$("#roleInfoChangeBtn").on('click', function() {
	let inputArrays = ["#nameEdit"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#roleEditModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let putData = JSON.stringify({
			'id': this.value,
			'name': $("#nameEdit").val().trim()
		});
		pgcrowdAjaxModify('/pgcrowd/role/infoupd', 'PUT', putData, putSuccessFunction);
	}
});
$("#tableBody").on('click', '.delete-btn', function() {
	let roleName = $(this).parents("tr").find("td:eq(0)").text().trim();
	let roleId = $(this).attr("deleteId");
	if (confirm("この" + roleName + "という役割情報を削除する、よろしいでしょうか。")) {
		pgcrowdAjaxModify('/pgcrowd/role/delete/' + roleId, 'DELETE', null, normalDeleteSuccessFunction);
	}
});
$("#tableBody").on('click', '.fuyo-btn', function() {
	let fuyoId = $(this).attr("fuyoId");
	$("#authChangeBtn").attr("fuyoId", fuyoId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	$("#roleName").text(nameVal);
	let authModal = new bootstrap.Modal($("#authEditModal"), {
		backdrop: 'static'
	});
	authModal.show();
	let ajaxReturn = $.ajax({
		url: '/pgcrowd/role/authlists',
		type: 'GET',
		dataType: 'json',
		async: false
	});
	if (ajaxReturn.status !== 200) {
		layer.msg('リクエスト処理異常。' + ajaxReturn.statusText);
		return;
	}
	let setting = {
		'data': {
			'simpleData': {
				'enable': true,
				'pIdKey': 'categoryId'
			},
			'key': {
				'name': 'title'
			}
		},
		'check': {
			'enable': true,
			'chkStyle': 'checkbox',
			'chkboxType': {
				'Y': 'ps',
				'N': 'ps'
			}
		}, callback: {
			'onNodeCreated': zTreeOnNodeCreated
		}
	};
	let authlist = ajaxReturn.responseJSON.data;
	$.fn.zTree.init($("#authTree"), setting, authlist);
	let zTreeObj = $.fn.zTree.getZTreeObj("authTree");
	zTreeObj.expandAll(true);
	ajaxReturn = $.ajax({
		url: '/pgcrowd/role/getAssigned',
		data: 'fuyoId=' + fuyoId,
		type: 'GET',
		dataType: 'json',
		async: false
	});
	let authIdList = ajaxReturn.responseJSON.data;
	for (const element of authIdList) {
		let authId = element;
		let treeNode = zTreeObj.getNodeByParam('id', authId);
		zTreeObj.checkNode(treeNode, true, true);
	}
});
$("#authChangeBtn").on('click', function() {
	let fuyoId = $(this).attr("fuyoId");
	let authIdArray = [];
	let zTreeObj = $.fn.zTree.getZTreeObj("authTree");
	let checkedNodes = zTreeObj.getCheckedNodes();
	for (const element of checkedNodes) {
		let checkedNode = element;
		let authId = checkedNode.id;
		authIdArray.push(authId);
	}
	let putData = JSON.stringify({
		'authIdArray': authIdArray,
		'roleId': [fuyoId]
	});
	pgcrowdAjaxModify('/pgcrowd/role/do/assignment', 'PUT', putData, authPutSuccessFunction);
});
function putSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		$("#roleEditModal").modal('hide');
		layer.msg('更新済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}
function authPutSuccessFunction() {
	$("#authEditModal").modal('hide');
	layer.msg('権限付与成功！');
}
function zTreeOnNodeCreated(event, treeId, treeNode) { // 设置节点创建时的回调函数
	let iconObj = $("#" + treeNode.tId + "_ico"); // 获取图标元素
	iconObj.removeClass("button ico_docu ico_open ico_close");
	iconObj.append("<i class='bi'></i>"); // 添加bootstrap图标的基础类名
	let iconObjectId = Number(iconObj.attr("id").substring(9, 10));
	if ($.isNumeric(iconObj.attr("id").substring(10, 11))) {
		iconObjectId = Number(iconObj.attr("id").substring(9, 11));
	}
	let pIdArrays = [1, 5, 9, 12];
	let deleteIdArrays = [2, 6, 13];
	let retrieveIdArrays = [3, 7, 10, 14];
	if (pIdArrays.includes(iconObjectId)) {
		iconObj.find("i").addClass("bi bi-amd");
	} else if (deleteIdArrays.includes(iconObjectId)) {
		iconObj.find("i").addClass("bi bi-android2");
	} else if (retrieveIdArrays.includes(iconObjectId)) {
		iconObj.find("i").addClass("bi bi-amazon");
	} else {
		iconObj.find("i").addClass("bi bi-apple");
	}
}
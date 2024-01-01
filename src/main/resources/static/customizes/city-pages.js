let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#businessKanri").removeClass('collapsed');
	$("ul", $("#businessKanri")).show('fast');
	$("#toCategory").css('color', 'darkred');
	toSelectedPg(1, keyword);
});
$("#searchBtn2").on('click', function() {
	keyword = $("#keywordInput").val();
	toSelectedPg(1, keyword);
});
function toSelectedPg(pageNum, keyword) {
	$.ajax({
		url: '/pgcrowd/city/pagination',
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
		let nameTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:bottom;'></td>").append(item.name);
		let districtTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:bottom;'></td>").append(item.districtName);
		let populationTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:bottom;'></td>").append(item.population);
		let stationTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:bottom;'></td>").append(item.stationNo);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn")
			.append($("<i class='bi bi-pencil-fill'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='bi bi-trash-fill'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center' style='width:120px;vertical-align:bottom;'></td>").append(editBtn).append(" ").append(deleteBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(districtTd).append(populationTd).append(stationTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#addCityBtn").on('click', function() {
	formReset("#cityAddModal form");
	getDistricts("#districtInput", null);
	let addModal = new bootstrap.Modal($("#cityAddModal"), {
		backdrop: 'static'
	});
	addModal.show();
});
$("#nameInput").on('change', function() {
	checkCityName("#nameInput", "#districtInput", $("#cityInfoSaveBtn"));
});
$("#populationInput").on('change', function() {
	let populationVal = this.value;
	let regularPopulation = /^\d{3,12}$/;
	if (populationVal === "") {
		showValidationMsg("#populationInput", "error", "人口数量を空になってはいけません。");
		$("#cityInfoSaveBtn").attr("ajax-va", "error");
	} else if (!regularPopulation.test(populationVal)) {
		showValidationMsg("#populationInput", "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
		$("#cityInfoSaveBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#populationInput", "success", "");
		$("#cityInfoSaveBtn").attr("ajax-va", "success");
	}
});
$("#districtInput").on('change', function() {
	checkCityName("#nameInput", "#districtInput", $("#cityInfoSaveBtn"));
});
$("#cityInfoSaveBtn").on('click', function() {
	let inputName = $("#nameInput").val().trim();
	let inputPopulation = $("#populationInput").val().trim();
	let inputDistrict = $("#districtInput").val();
	if ($(this).attr("ajax-va") === "error") {
		return false;
	} else if (inputName === "" || inputPopulation === "") {
		if (inputName === "" && inputPopulation === "") {
			showValidationMsg("#nameInput", "error", "名称を空になってはいけません。");
			showValidationMsg("#populationInput", "error", "人口数量を空になってはいけません。");
		} else if (inputName === "") {
			showValidationMsg("#nameInput", "error", "名称を空になってはいけません。");
		} else {
			showValidationMsg("#populationInput", "error", "人口数量を空になってはいけません。");
		}
	} else {
		let postData = JSON.stringify({
			'name': inputName,
			'districtId': inputDistrict,
			'population': inputPopulation
		});
		pgcrowdAjaxModify('/pgcrowd/city/infosave', 'POST', postData, postSuccessFunction);
	}
});
$("#tableBody").on('click', '.edit-btn', function() {
	formReset("#cityEditModal form");
	let editId = $(this).attr("editId");
	$("#cityInfoChangeBtn").attr("editId", editId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	let populationVal = $(this).parent().parent().find("td:eq(2)").text();
	$("#nameEdit").val(nameVal);
	$("#populationEdit").val(populationVal);
	getDistricts("#districtEdit", editId);
	let addModal = new bootstrap.Modal($("#cityEditModal"), {
		backdrop: 'static'
	});
	addModal.show();
});
$("#nameEdit").on('change', function() {
	checkCityName("#nameEdit", "#districtEdit", $("#cityInfoChangeBtn"));
});
$("#populationEdit").on('change', function() {
	let populationVal = this.value;
	let regularPopulation = /^\d{3,12}$/;
	if (populationVal === "") {
		showValidationMsg("#populationEdit", "error", "人口数量を空になってはいけません。");
		$("#cityInfoChangeBtn").attr("ajax-va", "error");
	} else if (!regularPopulation.test(populationVal)) {
		showValidationMsg("#populationEdit", "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
		$("#cityInfoChangeBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#populationEdit", "success", "");
		$("#cityInfoChangeBtn").attr("ajax-va", "success");
	}
});
$("#districtEdit").on('change', function() {
	checkCityName("#nameEdit", "#districtEdit", $("#cityInfoChangeBtn"));
});
$("#cityInfoChangeBtn").on('click', function() {
	let editName = $("#nameEdit").val().trim();
	let editPopulation = $("#populationEdit").val().trim();
	let editDistrict = $("#districtEdit").val();
	if ($(this).attr("ajax-va") === "error") {
		return false;
	} else if (editName === "" || editPopulation === "") {
		if (editName === "" && editPopulation === "") {
			showValidationMsg("#nameEdit", "error", "名称を空になってはいけません。");
			showValidationMsg("#populationEdit", "error", "人口数量を空になってはいけません。");
		} else if (editName === "") {
			showValidationMsg("#nameEdit", "error", "名称を空になってはいけません。");
		} else {
			showValidationMsg("#populationEdit", "error", "人口数量を空になってはいけません。");
		}
	} else {
		let putData = JSON.stringify({
			'id': $(this).attr("editId"),
			'name': editName,
			'districtId': editDistrict,
			'population': editPopulation
		});
		pgcrowdAjaxModify('/pgcrowd/city/infoupd', 'PUT', putData, putSuccessFunction);
	}
});
function postSuccessFunction() {
	$("#cityAddModal").modal('hide');
	layer.msg('追加処理成功');
	toSelectedPg(totalRecords, keyword);
}
function putSuccessFunction() {
	$("#cityEditModal").modal('hide');
	layer.msg('更新済み');
	toSelectedPg(pageNum, keyword);
}
function checkCityName(cityName, district, button) {
	let nameVal = $(cityName).val().trim();
	let districtVal = $(district).val();
	if (nameVal === "") {
		showValidationMsg(cityName, "error", "名称を空になってはいけません。");
		button.attr("ajax-va", "error");
	} else {
		$.ajax({
			url: '/pgcrowd/city/check',
			data: {
				'name': nameVal,
				'districtId': districtVal
			},
			type: 'GET',
			dataType: 'json',
			success: function(result) {
				if (result.status === 'SUCCESS') {
					showValidationMsg(cityName, "success", "√");
					button.attr("ajax-va", "success");
				} else {
					showValidationMsg(cityName, "error", result.message);
					button.attr("ajax-va", "error");
				}
			}
		});
	}
}
function getDistricts(element, districtId) {
	$(element).empty();
	$.ajax({
		url: '/pgcrowd/city/districtlist',
		data: 'districtId=' + districtId,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			$.each(result.data, (index, item) => {
				let optionElement = $("<option></option>").attr('value', item.id).text(item.name);
				optionElement.appendTo(element);
			});
		}
	});
}
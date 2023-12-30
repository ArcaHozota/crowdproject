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
$("#nameInput").on('change', function() {
	let nameVal = this.value;
	if (nameVal === "") {
		showValidationMsg("#nameInput", "error", "名称を空になってはいけません。");
		$("#cityInfoSaveBtn").attr("ajax-va", "error");
	} else {
		showValidationMsg("#nameInput", "success", "");
		$("#cityInfoSaveBtn").attr("ajax-va", "success");
	}
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
$("#cityInfoSaveBtn").on('click', function() {
	let inputName = $("#nameInput").val().trim();
	let inputPopulation = $("#populationInput").val().trim();
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
		let header = $('meta[name=_csrf_header]').attr('content');
		let token = $('meta[name=_csrf_token]').attr('content');
		$.ajax({
			url: '/pgcrowd/city/infosave',
			type: 'POST',
			dataType: 'json',
			data: JSON.stringify({
				'name': inputName,
				'districtId': $("#districtInput option:selected").val(),
				'population': inputPopulation
			}),
			headers: {
				[header]: token
			},
			contentType: 'application/json;charset=UTF-8',
			success: function() {
				$("#cityAddModal").modal('hide');
				layer.msg('追加処理成功');
				toSelectedPg(pageNum, keyword);
			},
			error: function(result) {
				layer.msg(result.responseJSON.message);
			}
		});
	}
});
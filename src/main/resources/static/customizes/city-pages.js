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
		let patternedPop = Number(item.population).toLocaleString('en-US');
		let idTd = $("<th scope='row' class='text-center' style='width:150px;vertical-align:middle;'></th>").append(item.id);
		let nameTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.name);
		let districtTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.districtName);
		let populationTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(patternedPop);
		let stationTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.stationNo);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn")
			.append($("<i class='bi bi-pencil-fill'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='bi bi-trash-fill'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center' style='width:100px;vertical-align:middle;'></td>").append(editBtn).append(" ").append(deleteBtn);
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
	checkCityName(this, "#districtInput");
});
$("#populationInput").on('change', function() {
	let populationVal = Number(this.value.replace(/,/g, ''));
	let regularPopulation = /^\d{3,12}$/;
	if (!regularPopulation.test(populationVal)) {
		showValidationMsg(this, "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "");
	}
});
$("#districtInput").on('change', function() {
	checkCityName("#nameInput", this);
});
$("#cityInfoSaveBtn").on('click', function() {
	let inputArrays = ["#nameInput", "#populationInput"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#cityAddModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let postData = JSON.stringify({
			'name': $("#nameInput").val().trim(),
			'districtId': $("#districtInput").val(),
			'population': Number($("#populationInput").val().trim().replace(/,/g, ''))
		});
		pgcrowdAjaxModify('/pgcrowd/city/infosave', 'POST', postData, normalPostSuccessFunction("#cityAddModal"));
	}
});
$("#tableBody").on('click', '.edit-btn', function() {
	formReset("#cityEditModal form");
	let editId = $(this).attr("editId");
	$("#cityInfoChangeBtn").val(editId);
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
	checkCityName(this, "#districtEdit");
});
$("#populationEdit").on('change', function() {
	let populationVal = Number(this.value.replace(/,/g, ''));
	let regularPopulation = /^\d{3,12}$/;
	if (!regularPopulation.test(populationVal)) {
		showValidationMsg(this, "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "");
	}
});
$("#districtEdit").on('change', function() {
	checkCityName("#nameEdit", this);
});
$("#cityInfoChangeBtn").on('click', function() {
	let inputArrays = ["#nameEdit", "#populationEdit"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#cityEditModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let putData = JSON.stringify({
			'id': this.value,
			'name': $("#nameEdit").val().trim(),
			'districtId': $("#districtEdit").val(),
			'population': Number($("#populationEdit").val().trim().replace(/,/g, ''))
		});
		pgcrowdAjaxModify('/pgcrowd/city/infoupd', 'PUT', putData, putSuccessFunction);
	}
});
function checkCityName(cityName, district) {
	let nameVal = $(cityName).val().trim();
	let districtVal = $(district).val();
	if (nameVal === "") {
		showValidationMsg(cityName, "error", "名称を空になってはいけません。");
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
				} else {
					showValidationMsg(cityName, "error", result.message);
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
function putSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		$("#cityEditModal").modal('hide');
		layer.msg('更新済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}
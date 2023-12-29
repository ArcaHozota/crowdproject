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
function getDistricts(element, cityId) {
	$(element).empty();
	$.ajax({
		url: '/pgcrowd/city/districtlist',
		data: 'cityId=' + cityId,
		type: 'GET',
		dataType: 'json',
		success: function(result) {
			$.each(result.data, function() {
				let optionElement = $("<option></option>").attr('value', this.id).attr('text', this.name);
				optionElement.appendTo(element);
			});
		}
	});
}
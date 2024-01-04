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
		url: '/pgcrowd/district/pagination',
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
		let shutoTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.shutoName);
		let chihoTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.chiho);
		let populationTd = $("<td scope='row' class='text-center' style='width:70px;vertical-align:middle;'></td>").append(patternedPop);
		let editBtn = $("<button style='width:100px;'></button>").addClass("btn btn-success btn-sm edit-btn")
			.append($("<i class='bi bi-pencil-square'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let btnTd = $("<td class='text-center' style='width:100px;vertical-align:middle;'></td>").append(editBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(shutoTd).append(chihoTd).append(populationTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#tableBody").on('click', '.edit-btn', function() {
	formReset("#districtEditModal form");
	let editId = $(this).attr("editId");
	$("#districtInfoChangeBtn").val(editId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	let shutoVal = $(this).parent().parent().find("td:eq(1)").text();
	let chihoVal = $(this).parent().parent().find("td:eq(2)").text();
	let populationVal = $(this).parent().parent().find("td:eq(3)").text();
	$("#nameEdit").val(nameVal);
	$("#chihoEdit").val(chihoVal);
	$("#shutoEdit").text(shutoVal);
	$("#populationEdit").text(populationVal);
	let addModal = new bootstrap.Modal($("#districtEditModal"), {
		backdrop: 'static'
	});
	addModal.show();
});
$("#districtInfoChangeBtn").on('click', function() {
	let inputArrays = ["#nameEdit", "#chihoEdit"];
	let inputForm = $("#districtEditModal form");
	let putData = JSON.stringify({
		'id': this.value,
		'name': $("#nameEdit").val().trim(),
		'chiho': $("#chihoEdit").val().trim()
	});
	commonUpdateMethod(inputArrays, inputForm, '/pgcrowd/district/infoupd', 'PUT', putData, putSuccessFunction);
});
function putSuccessFunction() {
	$("#districtEditModal").modal('hide');
	layer.msg('更新済み');
	toSelectedPg(pageNum, keyword);
}
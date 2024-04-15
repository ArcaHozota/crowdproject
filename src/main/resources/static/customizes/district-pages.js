let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#toDistrict").css('color', '#7F0020');
	toSelectedPg(1, keyword);
	$("#toDistrict").addClass('animate__animated animate__flipInY');
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
		let idTd = $("<th scope='row' class='text-center table-light' style='width:150px;vertical-align:middle;'></th>").append(item.id);
		let nameTd = $("<td scope='row' class='text-center table-light' style='width:70px;vertical-align:middle;'></td>").append(item.name);
		let shutoTd = $("<td scope='row' class='text-center table-light' style='width:70px;vertical-align:middle;'></td>").append(item.shutoName);
		let chihoTd = $("<td scope='row' class='text-center table-light' style='width:70px;vertical-align:middle;'></td>").append(item.chiho);
		let populationTd = $("<td scope='row' class='text-center table-light' style='width:50px;vertical-align:middle;'></td>").append(patternedPop);
		let flagImg = $("<img>").attr('src', '/static/image/flags/prefectures/' + item.districtFlag + '.svg').attr('alt', '').height(27).width(40);
		let flagTd = $("<td scope='row' class='text-center table-light' style='width:50px;vertical-align:middle;'></td>").append(flagImg);
		let editBtn = $("<button style='width:100px;'></button>").addClass("btn btn-success btn-sm edit-btn")
			.append($("<i class='fa-solid fa-pen-to-square'></i>")).append(" 編集");
		editBtn.attr("editId", item.id);
		let btnTd = $("<td class='text-center table-light' style='width:100px;vertical-align:middle;'></td>").append(editBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(shutoTd).append(chihoTd).append(populationTd).append(flagTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#tableBody").on('click', '.edit-btn', function() {
	let ajaxResult = $.ajax({
		url: '/pgcrowd/district/checkEdition',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
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
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#districtEditModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正。');
	} else {
		let putData = JSON.stringify({
			'id': this.value,
			'name': $("#nameEdit").val().trim(),
			'chiho': $("#chihoEdit").val().trim()
		});
		pgcrowdAjaxModify('/pgcrowd/district/infoupd', 'PUT', putData, putSuccessFunction);
	}
});
function putSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		$("#districtEditModal").modal('hide');
		layer.msg('更新済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}
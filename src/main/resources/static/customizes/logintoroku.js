$(document).ready(function() {
	let flag = 0;
	$("#eyeIcons2").on('click', function() {
		if (flag === 0) {
			$("#passwordIpt1").attr('type', 'text');
			$("#passwordIpt2").attr('type', 'text');
			$(this).attr('name', 'eye-off-outline');
			flag = 1;
		} else {
			$("#passwordIpt1").attr('type', 'password');
			$("#passwordIpt2").attr('type', 'password');
			$(this).attr('name', 'eye-outline');
			flag = 0;
		}
	});
	$("#eyeIcons").on('click', function() {
		if (flag === 0) {
			$("#passwordIpt").attr('type', 'text');
			$(this).attr('name', 'eye-off-outline');
			flag = 1;
		} else {
			$("#passwordIpt").attr('type', 'password');
			$(this).attr('name', 'eye-outline');
			flag = 0;
		}
	});
	let message1 = $("#errorMsg").text();
	if (message1 !== '') {
		layer.msg(message1);
	}
	let message2 = $("#torokuMsg").text();
	if (message2 !== '') {
		layer.msg(message2);
	}
	let email = $("#emailAddress").text();
	if (email !== '') {
		$("#accountIpt").val(email);
	}
});
$("#torokuBtn").on('click', function() {
	let inputArrays = ["#emailIpt", "#passwordIpt1", "#passwordIpt2", "#dateOfBirthIpt"];
	for (const element of inputArrays) {
		if ($(element).val().trim() === "") {
			layer.msg('入力しなかった情報があります。');
			return;
		}
	}
	let password01 = $("#passwordIpt1").val();
	let password02 = $("#passwordIpt2").val();
	if (password01 !== password02) {
		layer.msg('入力したパスワードが不一致です。');
	} else {
		$("#torokuForm").submit();
	}
});
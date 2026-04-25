$(function() {
	$("#loginBtn").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		if (username.trim() == '') {
			$.toast('请输入用户名','text');
			return;
		}
		if (password.trim() == '') {
			$.toast('密码不能为空','text');
			return;
		}
		var params = {
			"username" : username,
			"password" : password
		}
		execFunc("login.userlogin", params, function(result) {
			if (result.code == '0000') {
				setCookie("jssessionid",result.data,840000);
				setTimeout(function(){
					location.href = "../index.html";
				},200)		
			} else {
				$.toast(result.msg,"text");
			}
		})
	})
});
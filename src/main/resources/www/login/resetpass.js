window.onload = function() {
	$("#resetbtn").on('click',function(){
		var password=$("#password").val();
		var confirmpass=$("#confirmpass").val();
		var userguid=getUrlParam('userguid');
		if(password.trim()==confirmpass.trim()){
			execFunc('login.resetpass',{"password":password,"userguid":userguid},function(result){
				if(Number(result.data)==1){
					alert("重置成功");
					setTimeout(() => {
						location.href="login_init";
					}, 2000);
				}
			})
		}else{
			alert("两次输入的密码不一致");
		}
		
	})
}
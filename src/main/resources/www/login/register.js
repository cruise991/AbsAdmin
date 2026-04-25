$(function() {
	
	
	
	$("#gobtn").click(function(){
		location.href='login_init';
	})
	$('input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' // optional
	});
	$("#zcbtn").on('click',function(){
		var params=getParamsByClass("register-box-body","form-control");
		console.log(params);
		execFunc("login.userregister",params,function(result){
			if(Number(result.data)==1){
				$("#alertModel").modal('show').css({top:"10%",background:"none"});
				setTimeout(() => {
					location.href="login_init";
				}, 4000);
			}
			else{
				alert(result.msg);
			}
		})
	})
});
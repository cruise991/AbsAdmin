window.onload = function() {
	$("#hqyzm").on('click',function(){
		var issend=$(this).attr('value');
		if(Number(issend)==0){
			$(this).attr('value',1);
			   var email=$("#email").val();
			    execFunc('email.sendemail',{"email":email},function(result){
				    if(result.data=='1'){
				    	alert("已经发送");
				    }
			})
		    var num=60;
		    var timer=setInterval(function () {
					num--;
					if(Number(num)==1){
					    window.clearInterval(timer);   
					    $("#hqyzm").html("发送验证码");
					    $("#hqyzm").attr('value',0);
					}else{
					   $("#hqyzm").html("剩余"+num+"s");
					}
			}, 1000)
		}else{
			alert("请稍后");
		}
	});
	$("#confirmbtn").on('click',function(){
		var email=$("#email").val();
		var code=$("#hcode").val();
		execFunc('email.confirmcode',{"email":email,"code":code},function(result){
			if(Number(result.data)==1){
				location.href="resetpass_init?userguid="+result.msg;
			}else{
				alert(result.msg);
			}
		})
	})
}
$(function() {
	$("#subbtn").click(function() {
		var params = getParams();
		if (params == "") {
			return;
		} else {
			execFunc("role.addrole", params, function(result) {
			    if(result.code=="0000"){
			    	location.href="ht_rolelist.html";
			    }
			    else{
			    	alert(result.msg);
			    }
			})
		}
	})
	
	$("#cancelbtn").click(function(){
		location.href="ht_rolelist.html";
	})
	
});
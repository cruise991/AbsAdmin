$(function() {
	/**
	 * 新增公司
	 */
	$("#subbtn").click(function() {
		var params = getParams();
		params.rolesid=getRolesid();
		if (params == "") {
			return;
		} else {
			execFunc("user.adduser", params, function(result) {
			    if(result.code=="0000"){
			    	location.href="userlist.html";
			    }
			    else{
			    	alert(result.msg);
			    }
			})
		}
	})
	
	$("#cancelbtn").click(function(){
		location.href="userlist.html"
	})
	function getRolesid() {
			var arr = new Array();
			$("#rolelist :checkbox:checked").each(function(index) {
				arr[index] = $(this).attr("value");
			})
			var vals = arr.join(";");
			return vals;
	}
	
	
	function getRolesname() {
		var arr = new Array();
		$("#rolelist :checkbox:checked").each(function(index) {
			arr[index] = $(this).attr("data");
		})
		var vals = arr.join(";");
		return vals;
    }
	/**
	 * 获取到对应的可选角色
	 */
	execFunc("role.getrolelist",{},function(result){
		console.log(result);
		var vu = new Vue({
			el : '#rolelist',
			data : {
				data : []
			}
		});
		vu.data = result.data;
	})
	
	
	
	execFunc("ouinfo.getoulist", {}, function(result) {
		var vucom = new Vue({
			el : '#oulist',
			data : {
				data : []
			}

		});
		vucom.data = result.data;
	})
});
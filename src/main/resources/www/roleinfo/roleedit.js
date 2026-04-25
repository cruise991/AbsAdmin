
$(function() {
	/**
	 * 公司新增
	 */
	
	initPage();
	var rowguid = getUrlParam("rowguid");
	$("#subbtn").click(function() {
		var params = getParams();
		params.rowguid=rowguid;
		if (params == "") {
			return;
		} else {
			execFunc("role.editrole", params, function(result) {
				if (result.code == "0000") {
					location.href = "ht_rolelist.html";
				} else {
					alert(result.msg);
				}
			})
		}
	})
	$("#cancelbtn").click(function() {
		location.href = "ht_rolelist.html";
	})
});

function initPage(){
	var rowguid= getUrlParam("rowguid");
	execFunc('role.getrolebyguid',{"rowguid":rowguid},function(result){
		if(result.code=='0000'){
			$("#rolename").val(result.data.rolename);
			$("#ordernum").val(result.data.ordernum);
			$("#remark").val(result.data.remark);
		}else{
			alert(result.msg);
		}
	})
}
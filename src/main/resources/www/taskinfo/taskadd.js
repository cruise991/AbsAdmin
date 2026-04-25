$(function() {
	/**
	 * 新增视图
	 */
	$("#subbtn").click(function() {
		var params = getParams();
		if (params == "") {
			return;
		} else {
			execFunc("sysconfig.addconfig", params, function(result) {
				if (result.code == "0000") {
					location.href = "configlist.html";
				} else {
					$.alert(result.msg);
				}
			})
		}
	})

	
	$("#cancelbtn").click(function(){
		location.href="configlist.html";
	})
});
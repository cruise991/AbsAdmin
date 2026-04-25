$(function() {
	/**
	 * 新增公司
	 */





	init();

	$("#subbtn").click(
		function() {
			var params = getParams();
			console.log(params);
			if (params == "") {
				return;
			} else {
				execFunc("ouinfo.editou", params, function(result) {
					if (result.code == "0000") {
						location.href = "oulist.html";
					} else {
						$.toast(result.msg,"text");
					}
				})
			}
		})

	$("#cancelbtn").click(function() {
		location.href = "oulist.html"
	})
});


function init() {
	var rowguid = getUrlParam("rowguid");
	execFunc("ouinfo.getoubyguid", { "rowguid": rowguid }, function(result) {
		console.log(result);
		if (result.code == "0000") {
			initParams(result);
		} else {
			$.alert(result.msg);
		}
	})


}
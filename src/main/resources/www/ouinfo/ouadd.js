$(function() {
	$("#subbtn").click(function() {
		var params = getParams();
		if (params == "") {
			return;
		} else {
			execFunc("ouinfo.addou", params, function(result) {
				if (result.code == "0000") {
					location.href = "oulist.html";
				} else {
					$.toast(result.msg,'text');
				}
			})
		}
	})

	$("#cancelbtn").click(function() {
		location.href = "oulist.html"
	})
});
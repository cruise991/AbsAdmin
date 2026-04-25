$(function() {

	var hrowguid = getUrlParam("rowguid");
    function initPage(){
		execFunc("sysconfig.getconfigbyguid", {"rowguid":hrowguid}, function(result) {
			if (result.code == "0000") {
				absForm.parse(result);
			} else {
				alert(result.msg);
			}
		})
    }

    initPage();

	$("#subbtn").click(function() {
		var params = absForm.form();
		if (params == "") {
			return;
		} else {
			params.rowguid = hrowguid;
			execFunc("sysconfig.editconfig", params, function(result) {
				if (result.code == "0000") {
					location.href = "configlist.html";
				} else {
					alert(result.msg);
				}
			})
		}
	})

	$("#cancelbtn").click(function() {
		location.href = "configlist_init";
	});

});
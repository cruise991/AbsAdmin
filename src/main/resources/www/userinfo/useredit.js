$(function() {
	/**
	 * 新增公司
	 */

	var hcheck=$("#hcheck").attr('value');
	var rowguid = getUrlParam("rowguid");
	$("#subbtn").click(
			function() {
				var params = getParams();
				params.rolesid = $("input[type='radio'][name='checkRaidos']:checked").val();
				params.rowguid = rowguid;
				if (params == "") {
					return;
				} else {
					execFunc("user.edituser", params, function(result) {
						if (result.data == "1") {
							location.href = "userlist_init";
						} else {
							alert(result.msg);
						}
					})
				}
			})

	$("#cancelbtn").click(function() {
		location.href = "userlist_init"
	})
	function getRolesid() {
		var arr = new Array();
		$("#rolelist :checkbox:checked").each(function(index) {
			arr[index] = $(this).attr("value");
		})
		var vals = arr.join(";");
		return vals;
	}

	setTimeout(function() {
		console.log(111)
		var userrole = $("#userrole").val();
		$("#rolelist :radio").each(function(index, item) {
			if ($(item).attr("value") == userrole) {
				$(item).attr("checked", true);
			}
		})
	}, 1000);

	/**
	 * 获取到对应的可选角色
	 */
	execFunc("role.getlist", {}, function(result) {
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
			created(){
		        this.couponSelected = hcheck;
		    },
			data : {
				data : []
			}

		});
		vucom.data = result.data;
	})

});
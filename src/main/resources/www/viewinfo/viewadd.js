var vu = new Vue({
	el : '#rolelist',
	data : {
		data : []
	}
});

$(function() {
	/**
	 * 新增视图
	 */
	$("#subbtn").click(function() {
		var params = getParams();
		params.isroot = $("input[name='optionsRadios']:checked").val();
		params.parentname = $('#parentguid option:selected').text();
		params.rolesid = getRolesid();
		params.rolesname = getRolesname();
		if (params == "") {
			return;
		} else {
			execFunc("view.addview", params, function(result) {
				if (result.code == "0000") {
					location.href = "viewlist_init";
				} else {
					alert(result.msg);
				}
			})
		}
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
	execFunc("view.getrootview", {}, function(result) {
		var vucom = new Vue({
			el : '#pviewlist',
			data : {
				data : []
			}

		});
		vucom.data = result.data;
	})

	/**
	 * 检测单选按钮的值的变动
	 */
	$("#parentroot").hide();
	$("input[name='optionsRadios']").on('change', function() {
		if ($("input[name='optionsRadios']:checked").val() == 2) {
			$("#parentroot").show();
		} else {
			$("#parentroot").hide();
		}

	})

	$("#cancelbtn").click(function() {
		location.href = "viewlist.html";
	})
	initPage();
});

function initPage() {
	execFunc("role.getlist", {}, function(result) {
		console.log(result);
		vu.data = result.data;
	})

}
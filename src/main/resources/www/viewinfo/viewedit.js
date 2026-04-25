var vu_role = new Vue({
	el : '#rolelist',
	data : {
		checkedIds : [],
		data : []
	}
});

var vucom = new Vue({
	el : '#pviewlist',
	data : {
		couponSelected:[],
		data : []
	}

});

$(function() {
	var hisroot = $("#hisroot").val();
	var hrolesid = $("#hrolesid").val();
	var hrowguid = $("#hrowguid").val();
	var hparentguid=$("#hparentguid").val();
	var hissub=$("#hissub").val();

	/**
	 * 保存信息
	 */
	$("#subbtn").click(function() {
		var params = getParams();
		if (params == "") {
			return;
		} else {
			params.isroot = $("input[name='optionsRadios']:checked").val();
			params.rolesid = getRolesid();
			params.rolesname = getRolesname();
			params.rowguid=getUrlParam("rowguid");
			params.parentname = $('#parentguid option:selected').text();
			params.parentguid = $('#parentguid option:selected').val();
			execFunc("view.saveview", params, function(result) {
				if (result.code == "0000") {
					location.href = "viewlist.html";
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

	$("#cancelbtn").click(function() {
		location.href = "viewlist.html";
	});
	
	
	
	/**
	 * 获取到对应的可选角色
	 */
	execFunc("role.getlist", {}, function(result) {
		vu_role.data = result.data;
	})

	/**
	 * 获取到根模块
	 */
	execFunc("view.getrootview", {}, function(result) {

		for (var i = 0; i < result.data.length; ++i) {
			if (result.data[i].rowguid == hrowguid) {
				result.data.splice(i,1);
			}
		}
		vucom.data = result.data;
	})

	/**
	 * 检测单选按钮的值的变动
	 */

	$("input[name='optionsRadios']").on('change', function() {
		if ($("input[name='optionsRadios']:checked").val() == 2) {
			$("#parentroot").show();
		} else {
			$("#parentroot").hide();
		}

	})
});
function initPage(){
	var rowguid=getUrlParam("rowguid");
	execFunc("view.initPage", {"rowguid":rowguid}, function(result) {
		if(result.code=='0000'){
			console.log(result);
			if(result.data.isroot=='1'){
				$("#parentroot").hide();
				$("input[name='optionsRadios']").eq(0).prop("checked", "checked");
			}else{
				$("input[name='optionsRadios']").eq(1).prop("checked", "checked");
			}
			$("#viewname").val(result.data.viewname);
			$("#url").val(result.data.url);
			$("#sortnum").val(result.data.sortnum);
			setTimeout(function(){
				var strs = result.data.hroleguids.split(";");
				vu_role.checkedIds = strs;
				vucom.couponSelected=result.data.parentguid;	
			}, 500);
		}else{
			alert(reuslt.msg);
		}
	})
}
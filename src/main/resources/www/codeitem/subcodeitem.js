$(function() {

	initItem();
	$("#addcodeitem").click(function() {
		var params = getParams();
		params.itemorder = $("#itemguid").val();
		params.rowguid = $("#rowguid").val();
		params.itemorder=getUrlParam("itemorder");
		params.itemname=$("#itemname").html();
		execFunc(ajaxUrl.addsubcodeitem, params, function(data) {
			if (data.msg) {
				layer.alert(data.msg, function(index) {
					layer.close(index);
					$("#itemtext").val("");
					$("#itemvalue").val("")
					$("#isupdate").val("0");
					$("itemguid").val(guid);
					initItem();
				});
			}
		})
	})
});
var vutable = new Vue({
	el : ".subtable",
	data : {
		data : []
	}
})
function initItem() {
	var itemorder = getUrlParam("itemorder");
	var param = {
		"itemorder" : itemorder
	};
	$("#itemguid").val(itemorder);
	execFunc("codeitem.getcodeitembyorder", param, function(result) {
		$("#itemname").html(result.msg);
		vutable.data = result.data;
	})
}
function mod(rowguid) {
	var param = {
		"guid" : rowguid
	}
	$("#rowguid").val(rowguid);
	execFunc(ajaxUrl.getiteminfobyguid, param, function(result) {
		console.log(result);
		if(result.code=='0000'){
		   $("#itemtext").val(result.data.itemtext);
		   $("#itemvalue").val(result.data.itemvalue);
		}
	
	});
}
function del(rowguid) {

	var parame = {
		"guid" : rowguid
	}
	layer.confirm('确定删除吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		execFunc(ajaxUrl.delsubitem, parame, function(data) {
			if (data.msg) {
				layer.msg(data.msg, {
					icon : 1
				});
			}
			initItem();
		});
	}, function() {
	});

}
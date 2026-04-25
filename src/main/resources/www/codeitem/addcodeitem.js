$(function() {
	//新增代码项
	$("#addcodeitem").on('click',function(){
		var params=getParams();
		execFunc(ajaxUrl.addcodeitem,params,function(result){
			if(result.code=='0000'){
				layer.alert(result.msg,function(){
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.close(index);
				});
			}
		})
	})
});
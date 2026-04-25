var key = "";
var currentPage = 1
var pageSize = 15;
var vu = new Vue({
	el: '.table',
	data: {
		data: []
	}
});
$(function() {

	$("#addbtn").click(function() {
		location.href = "ouadd.html";
	});

	/**
	 * 点击搜索的功能
	 */
	$("#searchbtn").click(function() {
		key = $("#table_search").val();
		render(currentPage, pageSize, key);
	})
	/**
	 * 回车的事件
	 */
	$("#table_search").keyup(function(event) {
		if (event.keyCode == 13) {
			key = $("#table_search").val();
			render(currentPage, pageSize, key);
		}
	});

	render(currentPage, pageSize, key);


	$("#selectsize").on('change', function() {
		pageSize = $(this).val();
		render(currentPage, pageSize, key);
	})


});
function del(rowguid) {
	$.confirm("你确定要删除这条记录吗","确定删除?", function() {
		execFunc("ouinfo.delou", {
			"rowguid": rowguid
		}, function(result) {
			if (result.code == "0000") {
				$.toast("删除成功","text");
				render(currentPage, pageSize, key);
			} else {
				$.toast(result.msg,"text");
			}
		})
	})
}


/**
 * 修改
 * 
 * @param id
 * @returns
 */
function mod(rowguid) {
	location.href = "ouedit.html?rowguid=" + rowguid;
}


function render(currentPage, pageSize, key) {
	execFunc("ouinfo.getoulist", {
		"ouname": key,
		"pagenum": currentPage,
		"pagesize": pageSize
	}, function(result) {
		if (result.code == '0000') {
			vu.data = result.data;
		    $("#pagesize").html(pageSize);
		    $("#totalnum").html(result.totalcount);
	        setPage(currentPage, Math.ceil(result.totalcount / pageSize), render);
            
		} else {
			$.toast(result.msg,"text");
		}

	})
}


/**
 * 
 * @param pageCurrent
 *            当前所在页
 * @param pageSum
 *            总页数
 * @param callback
 *            调用ajax
 */
function setPage(pageCurrent, pageSum, callback) {
	$("#page").bootstrapPaginator({
		// 设置版本号
		bootstrapMajorVersion: 1,
		// 显示第几页
		currentPage: pageCurrent,
		numberOfPages: 6, // 最多显示Page页
		// 总页数
		totalPages: pageSum,
		// 当单击操作按钮的时候, 执行该函数, 调用ajax渲染页面
		onPageClicked: function(event, originalEvent, type, page) {
			// 把当前点击的页码赋值给currentPage, 调用ajax,渲染页面
			currentPage = page
			render(currentPage, pageSize, key);
		}
	})
}


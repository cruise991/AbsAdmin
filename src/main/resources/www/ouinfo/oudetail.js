$(function() {
	/**
	 * 新增公司
	 */

	var rowguid = getUrlParam("rowguid");
	$("#subbtn").click(
			function() {
				var params = getParams();
				params.userrole = $(
						"input[type='radio'][name='checkRaidos']:checked")
						.val();
				params.rowguid = rowguid;
				if (params == "") {
					return;
				} else {
					execFunc("user.edituserinfo", params, function(result) {
						if (result.data == "1") {
							alert("保存成功");
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

	var $sourceImg = $('#sourceImg');
	var xhr = new XMLHttpRequest(); // XMLHttpRequest 对象
	function dealImg() {
		var $target = $("#result");
		var data = $sourceImg.cropper('getCroppedCanvas', {
			width : 400, // 裁剪后的长宽
			height : 200
		}).toBlob(function(blob) {
			$target.attr('src', getObjectURL(blob));
		});
	}

	var options = {
		aspectRatio : 3 / 2,
		viewMode : 1,
		responsive : true,
		minContainerWidth : 800,
		minContainerHeight : 600,
		maxCanvasHeight : 800,
		maxCanvasWidth : 600,
		ready : function() {
			dealImg();
		},
		crop : function(e) {
			dealImg();
		}
	}
	$("#chooseImg").on('change', function() {
		var objUrl = getObjectURL(this.files[0]);
		console.log(objUrl);
		if (objUrl) {
			$("#sourceImg").attr("src", objUrl);
			$sourceImg.cropper('destroy').attr('src', objUrl).cropper(options);
			$sourceImg.css({
				"width" : "800px",
				"height" : "600px"
			})
		}
	});
	$sourceImg.cropper(options);

	$("#showmodal").on('click', function() {
		$("#cropperModel").modal('show');
		$sourceImg.cropper(options);
	})
	
	
	$("#uploadbtn").on('click', function() {
		var serviceUrl = getProjectURL();
		var articleguid = $("#rowguid").val();
		var url = serviceUrl + "/user/uploadtx";
		var form = new FormData();
		var img = $("#result").attr("src");
		var image = new Image();
		image.src = img;
		var base64 = getBase64Image(image);
		var file = dataURLtoBlob(base64);
		form.append("myfile", file);
		xhr.open("post", url, true); // po
		xhr.upload.onloadstart = function() {// 上传开始执行方法
			ot = new Date().getTime(); // 设置上传开始时间
			oloaded = 0;// 设置上传开始时，以上传的文件大小为0
		};
		xhr.send(form); // 开始上传，发送form数据
		xhr.responseText = function(res) {
			console.log(res);
		}
		xhr.onreadystatechange = function(response) {
			if (response.target.readyState == '4') {
				var result = JSON.parse(response.target.response);
				if (Number(result.data) == 0) {
					alert(result.msg);
				} else {
					alert("头像上传成功");
					$("#cropperModel").modal('hide');
				}
			}
		}
	});

});


/**
 * 图像转Base64
 */
function getBase64Image(img) {
	var canvas = document.createElement("canvas");
	canvas.width = img.width;
	canvas.height = img.height;
	var ctx = canvas.getContext("2d");
	ctx.drawImage(img, 0, 0, img.width, img.height);
	var ext = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase();
	var dataURL = canvas.toDataURL("image/" + ext);
	return dataURL;
}
function dataURLtoBlob(dataurl) {
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(
			n);
	while (n--) {
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new Blob([ u8arr ], {
		type : mime
	});
}
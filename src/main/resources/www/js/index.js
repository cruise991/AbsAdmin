 $(function() {
	var vu = new Vue({
		el : '.sidebar-menu',
		data : {
			data : []
		}
	});
	execFunc("indexframe.getviewjson", {}, function(result) {
		  vu.data = result.data;
	});
	
	execFunc("user.getuserinfo",{"token":getCookie("jssessionid")},function(result){
		if(result.code=='0000'){
			$("#realname").html(result.username);
            $("#username").html(result.username);	
		}
	})
	

	$("#signoutbtn").on('click', function() {
		execFunc('login.loginout', {}, function(result) {
			if (result.code == '0000') {
				setCookie("jssessionid","");
				location.href = "login/ht_login.html";
			}
		})
	})
	
	$("#rolebtn").on('click', function() {
		var url = $(this).attr('value');
		$(".iframec").attr('src', url);
	})
	$("#userbtn").on('click', function() {
		var url = $(this).attr('value');
		$(".iframec").attr('src', url);
	});

	$("#viewbtn").on('click', function() {
		var url = $(this).attr('value');
		$(".iframec").attr('src', url);
	});

});
function addClass(e, havesub, url, viewname) {
	$(e.currentTarget.parentNode.parentNode).children("li").not(e.currentTarget.parentNode).removeClass('active');
	$(e.currentTarget.parentNode).toggleClass('active');
	if (havesub != 1) {
		$("#moudlename").html(viewname);
		$(".iframec").attr('src', url);
	}
}
function changeIframe(url) {
	$(".iframec").attr('src', url);
}
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除用户</title>

<script type="text/javascript">
	function sub(){
		var userId = document.getElementsByName("userId")[0].value;
		//改一下用户id
		document.forms[0].action="user/"+userId;
		//手动提交
		document.forms[0].submit();
	}
</script>

</head>
<body>
	删除用户----------------------
	<!-- 添加隐藏表单域 -->
	<form action="user" method="post">
		<input type="hidden" name="_method" value="delete">
		用户id：<input name="userId"/>
		<input type="button" onclick="sub()" value="提交"/>
	</form>
</body>
</html>
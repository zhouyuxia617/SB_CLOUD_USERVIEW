<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户</title>

<script type="text/javascript">
	function sub(){
		var userId = document.getElementsByName("userId")[0].value;
		//改一下用户id
		document.forms[0].action="user/"+userId;
		//改完之后手动提交一下
		document.forms[0].submit();
	}
</script>

</head>
<body>
	<!-- 加一个隐藏表单域 -->
	<form action="" method="post">
	<!-- 加一个_method -->
	<input type="hidden" name="_method" value="put">
		用户id:<input name="userId"/>
		用户名:<input name="userName"/>
		<input type="button" onclick="sub()" value="提交"/>		
	</form>
</body>
</html>
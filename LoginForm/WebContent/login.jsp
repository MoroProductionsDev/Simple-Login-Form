<!-- 
	@author: Raul Rivero Rubio
	View Login it will has the login form for the user to login. 
	It will display warning message if the user name is empty or does not exists in the database.
	It will display warning message if the password is empty or if is it incorrect.
 -->
<%@include file="header.jsp"%>
<%-- <%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<title>Login page</title>
</head>
<body>
<%! private String username = ""; %>
<%

	//get username to display in the textfield
	if(session.getAttribute("username") != null) { 
		if(session.getAttribute("username").toString().trim().isEmpty())
			username = "";
		else {
			username = session.getAttribute("username").toString().trim();
		}
	}

	String userErrorMsg = "";
	// get username error if exists
	if(request.getParameter("userErrorMsg") != null) {
		// check if the password is empty and prompt the user to filled it.
		if(!request.getParameter("userErrorMsg").isEmpty()) { 
			userErrorMsg = request.getParameter("userErrorMsg"); 
		}
	}
	
	String pwdErrorMsg = "";
	// get password error if exists
	if(request.getParameter("pwdErrorMsg") != null) {
		// check if the password is empty and prompt the user to filled it.
		if(!request.getParameter("pwdErrorMsg").isEmpty()) {
			pwdErrorMsg = request.getParameter("pwdErrorMsg");
		}
	}
%>
 <div class="wrapper">
 <% 
 	// If an registration have been created, display a message that the account have been created
	if(request.getAttribute("new_registration") != null) {
		System.out.println(request.getAttribute("new_registration").equals(true));
		if(request.getAttribute("new_registration").equals(true)) { %> 
			<h1 style="color:green">Account created</h1> 
		<%}
	}
%>
 	<h2>Login</h2>
 	<p>Please fill in your credentials to login.</p>
 	<form action="login_authentication" method="post">
 		<div class="form-group">
 			<label>Username</label>
 			<input type=text name="username" class="form-control" value="<%= username %>"> 
 			<span style="color:red;" class="help-block"><%=userErrorMsg%> </span>
		</div>
		<div class="form-group">
 			<label>Password</label>
 			<input type="password" name="password" class="form-control"  minlength="8">
			<span style="color:red;" class="help-block"><%=pwdErrorMsg%> </span>
 		</div>
 		<div class="form-group">
         	<input type="submit" class="btn btn-primary" value="Login">
        </div>
        <p>Don't have an account? <a href="register.jsp">Sign up now</a>.</p>
 	</form>
 </div> <!-- end wrapper -->
<%@include file="footer.jsp"%>
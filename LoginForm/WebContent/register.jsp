<!-- 
	@author: Raul Rivero Rubio
	View Register it has the register form for the user to register.
	It will display warning message if the user name is empty or exists in the database.
	It will display warning message if the password is empty or incorrectly formatted.
	It will display warning message if the confirm password is empty or does not match the password.
 -->
<%@include file="header.jsp"%>
<title>Sign Up</title>
</head>
<body>
<%! private String username = ""; %>
<%
	// get username to display in the textfield
	if(session.getAttribute("username") != null) { 
		if(session.getAttribute("username").toString().trim().isEmpty())
			username = "";
		else 
			username = session.getAttribute("username").toString().trim();
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
	
	String confirm_pwdErrorMsg = "";
	// get confirm password error if exists
	if(request.getParameter("confirm_pwdErrorMsg") != null) {
		// check if the password is empty and prompt the user to filled it.
		if(!request.getParameter("confirm_pwdErrorMsg").isEmpty()) {
			confirm_pwdErrorMsg = request.getParameter("confirm_pwdErrorMsg");
		}
	}
%>
	<div class="wrapper">
        <h2>Sign Up</h2>
        <p>Please fill this form to create an account.</p>
        <form action="RegisterServlet" method="post">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username"class="form-control" value="<%= username %>">
                <span style="color:red;" class="help-block"><%=userErrorMsg%> </span>
            </div>    
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" value="">
                <span style="color:red;" class="help-block"><%=pwdErrorMsg%> </span>
            </div>
            <div class="form-group>">
                <label>Confirm Password</label>
                <input type="password" name="confirm_password" class="form-control" value="">
                <span style="color:red;" class="help-block"><%=confirm_pwdErrorMsg%> </span>
            </div>
            <div class="form-group">
                <input type="submit" class="btn btn-primary" value="Submit">
                <input type="reset" class="btn btn-default" value="Reset">
            </div>
            <p>Already have an account? <a href="login.jsp">Login here</a>.</p>
        </form>
    </div>    
<%@include file="footer.jsp"%>
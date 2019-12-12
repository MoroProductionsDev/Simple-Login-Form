<!-- 
	@author: Raul Rivero Rubio
	View Index  page is the controller session. It will redirect you to login page if the user hasen't
	been granted. It will send the user to welcome page if the user has it access granted.
 -->
<%@include file="header.jsp"%>
<title>index page</title>
</head>
<body>
	<%
		// If the "granted" attribute is null, redirect to the login page
		if(session.getAttribute("granted") == null) {
			response.sendRedirect("login.jsp");
		} else {
			// if the user have been "granted" go to the user welcome page 
			if(session.getAttribute("granted").equals(true)) {
				response.sendRedirect("welcome.jsp");	
			// else redirect it to the login page.
			} else {
				response.sendRedirect("login.jsp");
			}
		}
	%>
<%@include file="footer.jsp"%>


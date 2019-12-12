<!-- 
	@author: Raul Rivero Rubio
	View Welcome has the user name welcome page.
 -->
<%@include file="header.jsp"%>
<title>Welcome page</title>
</head>
<body>
 <div class="wrapper">
		<% // Display dispage only if the user has access to it
		if(session.getAttribute("granted") == null ) 
 			response.sendRedirect("login.jsp");
 	  		else if(session.getAttribute("granted").equals(true))  {
 				%><h2>Welcome <span style="color:green;"><%= session.getAttribute("username").toString()%></span></h2>
 	   		<!-- Logout out form -->
	 	   <form action="LogoutServlet" method="post"> 	   
		 	   <div class="form-group" style="margin-top: 10px">
		         	<input type="submit" class="btn btn-primary" name="logout" value="logout">
		        </div>
	 	   </form>
 	   <% // Redirect if the user does not have access
 	   } else {
 	   		response.sendRedirect("index.jsp");
 	   }%>
 </div> <!-- end wrapper -->
<%@include file="footer.jsp"%>
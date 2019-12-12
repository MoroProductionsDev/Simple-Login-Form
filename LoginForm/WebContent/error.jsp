<!-- 
	@author: Raul Rivero Rubio
	View Error page display any error related with database connection or something else.
 -->
<%@include file="header.jsp"%>
<title>error page</title>
</head>
<body>
	<div class="wrapper">
		<div>
			<h3 style="color:red;">Error: </h3>
			<p>
			<% 
				// Display only if their is an error message in the session
				if(!session.getAttribute("exception").toString().isEmpty()) {
					out.println(session.getAttribute("exception").toString()); 
			}%>
			</p>
		</div>
	</div>
<%@include file="footer.jsp"%>

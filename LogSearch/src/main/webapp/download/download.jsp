<%@page import="java.net.InetAddress"%>
<%@page import="java.net.URL"%>
<%@page import="com.log.analyzer.commons.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
	<div id="_container">
		<div><h5> Configuring Charon is very easy, follow following steps to
			install Charon on nodes where you want to search. Charon will
			automatically connect back to Pluto.</h5></div>
		<div>
			<ol>
				<li>Download the client jar file by <a href="download/charon.jar">clicking
						here.</a></li>
				<li>You can also download using following command on Linux/
					Unix systems. <span style="font-weight: bold; color: blue;">`wget
						http://<%=InetAddress.getLocalHost().getCanonicalHostName()%>:<%=request.getLocalPort()%><%=request.getContextPath()%>/download/charon.jar`
				</span>
				</li>
				<li>Place the client jar preferably at users root. e.g. if
					users' root is <b>&lt;/home/myuser&gt;</b>, put the jar at
					<b>&lt;/home/myuser&gt;/search-client/charon.jar</b></li>
				<li>Go into the directory where charon.jar is put.</li>
				<li>Make sure java is present on the PATH.</li>
				<li>Start the satellite of Pluto using <span
					style="font-weight: bold; color: blue;">nohup java -jar -Dlog.master.url=http://<%=InetAddress.getLocalHost().getCanonicalHostName()%>:<%=request.getLocalPort()%><%=request.getContextPath()%>
						charon.jar &
				</span>
				</li>
				<li>
				 If you want to change the logging level of charon, use flag <b>-D<%=Constants.LOG_LEVEL%>=&lt;error,info,debug,trace&gt;</b> as startup option. Recommended production level is &lt;error&gt;.
				</li>
				<li>Wait for one minute, Charon the satellite(client) will
					register it itself with Pluto(server). Once registered it will
					become available in node administration section.</li>
				<li>
				 Charon can be made to log on console using  <b>-D<%=Constants.LOG_ON_CONSOLE%>=true</b> as startup option. This is however not recommended.
				</li>
				<li>
				 Charon can be remotely terminated using administration module. This will only shutdown the charon and remote its configuration. Charon.jar file will not be deleted on given node.
				</li>
			</ol>
		</div>
	</div>
</body>
</html>
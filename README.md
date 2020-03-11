# Pluto 
<Need Help to make it grow>
log search tool, aggregation, sorting by date

Introduction

There are several log correlation, digestion, search tools are available in the market. All of them collect data from various systems, store it in the data store (big data) and then charge the customers on volume or number of transactions. Several tools like Splunk™, Logstah™ which are available commercially for log analysis and there are inarguably very good in their domain.
Cost is a big factor for medium size and small scale IT organizations. Some time applications budget may be dwarfed by the monetary requirements of these good softwares. Do one really want to use all the services provided by these tools or just want to use a limited subset, wasting money on all other goodies that were paid upfront? Do your Test and Development organizations can also me made available the functions log search and correlation for better visibility on a lower cost?
The answer to all these questions is Pluto® developed by Rationalminds® available at www.rationalminds.net. This software provides for the needs of small-scale IT organizations production support, development and testing teams. In this blog, I am going to discuss the features and installation process of Pluto®.

Architecture and Requirements

To start with following is the high-level architecture of Pluto® setup. As any typical software, it is based on 2 tier model accessible by the browser. Pluto server contains the main processor software performing correlation and Charon does the gathering of data on nodes for a particular search request.


pluto server-client architecture			





To run Pluto® following are the software requirements for both tiers.

Server side
Java 1.7+
Apache Tomcat 8 or equivalent Java application server.
Cient Side 
Java 1.7+ available in system path.
You see no database requirements are there. Pluto uses embedded database as it stores data for mostly authentication and its minimal profile maintenance purpose.
To start, you can download the software in the format of war file from www.rationalminds.net and copy the war file inside your webapps in tomcat or deploy it in any application server of your choice.

Once you deploy the software type in the following URL in your browser window http://<MACHINE>:<PORT>/pluto. Here MACHINE is the name or IP of the server on which Pluto is deployed and PORT is the port number on which your application server or tomcat is running.

Operations

You must see the following screen as your landing screen.


Pluto login screen
 There is one default login designed for first time login into this account. Follow the instructions presented on this page.

After logging in you will find with the main search screen.


Pluto Search screen
Hover over question mark signs to find the on page tutorial to see what inputs are required. Field "Labels" is the field which helps you to group the nodes and do a parallel search.
Use screen "Configure Agent" to find help for installing the agent on servers which you want to search for logs. The screen content will look like following.


Agent Configurations Help
All the commands and links can be used without modifications and the parameters are tuned to your environment requirements.

The application is divided into two sections "Search" and "Administration". Click on Administration screen and it will let you perform administrative functions like user creation, role assignments, and node property management. The most important feature is labeling nodes, if two nodes have the same label, they can be searched parallel using that label. Node management screen looks something like follows:


Pluto Administration - Node Management
Using search screen I ran some search on '/var/log' to find 'info' keyword on my 'infra' label grouped nodes (which contains 4 nodes), and I was presented with the follwoing screen.


Search result screen
Notice 'info' is highlighted with yellow color to show the matches. The hover over log text shows you the line number of the log file where the match was found. The dates are automatically fetched from text using custom date parser algorithm developed specifically for this project (The source code of date parser is present at https://github.com/vbhavsingh/DateParser and algorithm description is present at https://coffeefromme.blogspot.com/2015/10/utility-to-extract-date-from-text-with.html). All the events are chronologically arranged.

Now in case one wants to read more lines from the log file where the match was found, they can right click on the text area to present them options to choose more lines.


Search result screen - show more lines on right click
If this option is used, one will be presented with a dialogue box to enter the to and from line numbers, this field is auto-populated for previous ten and trailing ten lines.


Search result screen - show more lines dialogue box	
 You can change the range as per your requirement. There is no limit applied by the system, but of course one cannot fetch more than what's there.
Once you choose your options and submit to get more lines, following dialogue box will show you the associated details and log lines.


Search result - fetch more lines in wait

Search result - more lines from file
 This screen apart from file contents show you some other details like file size and created and modified date. In many cases created and modified date are same as operating systems don't capture that data.


Pluto version 4.0 now contains time-based search that has been added through a proprietary solution to perform the time-based search on log text without indexing or ingesting the logs into another system or file. The proprietary algorithm identifies the file date patterns and generates a search expression for the provided interval to accomplish what other major software's of this field accomplish by ingesting the log files into big data systems. The system allows you to search in any time zone from any time zone, up to the accuracy of a second.


now search with time interval
Pluto version 4.0 also contains rest based API to invoke search by the third party applications. The search can now be integrated with third-party monitoring, dashboarding and other functions. The API can be made available by assigning the role 'robot' to users. The output information is in JSON and contains search and search metadata as present in the Pluto GUI

An interesting aspect has been added as a separate tab on the result page to visualize frequency of search by day, node and server.


frequency visualization of the results

Security

Security is paramount as agents may open doors to unauthorized access into servers. The communication channels between agent and server are encrypted using dynamic key exchange between them.
Search is allowed only on limited file sets (which can be changed using node administration) and the result is limited to line counts and data limits to save network, memory and CPU utilization of nodes.

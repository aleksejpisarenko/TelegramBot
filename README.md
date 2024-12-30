<h1>Introduction</h1>
<p style="font-size: 14px">Welcome to "Telegram Schedule Bot" project. This project is designed to send updates of schedule of school, universities
or any other institution that has schedule.</p>
<p style="font-size: 14px">The goal of this project is to let students or people get the new schedule of theirs institution as soon as it is posted on web or any other platform,
 using the one of the most comfortable ways of receiving notifications - a Telegram bot.</p>
<p style="font-size: 14px">Whether you are a developer looking to extend its functionality or a user interested in deploying it for your personal needs,
this documentation will guide you through every step of the process.</p>
<h2> Key Features:</h2>
        <li style="font-size: 20px"><b>Receiving Notifications Of Schedule Updates</b></li>
            <ul style="font-size: 16px">Receive notifications of the new schedule arrival.</ul>
        <li style="font-size: 20px"><b>Scalable Architecture</b></li>
            <ul style="font-size: 16px">Designed to handle increasing load or future expansion without significant changes.</ul>
        <li style="font-size: 20px"><b>Customizable Configuration</b></li>
            <ul style="font-size: 16px">A flexible config file allowing users to set up the bot's behavior, APIs, or data sources easily.</ul>
        <li style="font-size: 20px"><b>User-Friendly Setup</b></li>
            <ul style="font-size: 16px">Straightforward installation process with minimal technical requirements.</ul>
        <li style="font-size: 20px"><b>Multi-Thread Performance</b></li>
            <ul style="font-size: 16px">Optimized for handling multiple tasks or user request <b>concurrently</b> 
            without impacting performance.</ul>

<h1 id="gettingStarted">Getting Started</h1>
<p style="font-size: 14px"> To get started with "Telegram Schedule Bot", you'll need the following tools and prerequisites.</p>
<ol>
<li style="font-size: 16px"><b>Java Development Kit</b>. Ensure you have JDK version of 23 or higher installed</li>
<li style="font-size: 16px"><b>Configurational File</b>. Prepare a configurational file containing:
<br> "bot_token" as for bot token, that you get in a BotFather bot(@BotFather in telegram), 
<br> "bot_name" as for bot name, that you get in a BotFather bot(@BotFather in telegram), 
<br> "db_pass" as for your database password, 
<br> "db_user" as for your database name, 
<br> "db_url" as for your database URL.</li>
<li style="font-size: 16px"><b>Loggers</b>. The logging framework that is used is SLF4J, which you can configure based on your needs. Here's my configuration of it.</li>

        <?xml version="1.0" encoding="UTF-8"?>
        <Configuration status="WARN">
            <Appenders>
                <Console name="LogToConsole" target="SYSTEM_OUT">
                    <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
                </Console>
                <File name="LogToFile" fileName="C:/logger/log.txt">
                    <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
                </File>
            </Appenders>
            <Loggers>
                <Logger name="org.example.TelegramBot.DatabaseService" level="debug" additivity="false">
                    <AppenderRef ref="LogToConsole"/>
                    <AppenderRef ref="LogToFile"/>
                </Logger>
                <Logger name="org.example.TelegramBot.Main" level="debug" additivity="false">
                    <AppenderRef ref="LogToConsole"/>
                    <AppenderRef ref="LogToFile"/>
                </Logger>
                <Logger name="org.example.TelegramBot.TelegramBot" level="debug" additivity="false">
                    <AppenderRef ref="LogToConsole"/>
                    <AppenderRef ref="LogToFile"/>
                </Logger>
                <Logger name="org.example.TelegramBot.ThreadManager" level="debug" additivity="false">
                    <AppenderRef ref="LogToConsole"/>
                    <AppenderRef ref="LogToFile"/>
                </Logger>
                <Root level="error">
                    <AppenderRef ref="LogToConsole"/>
                </Root>
            </Loggers>
        </Configuration>

<li style="font-size: 16px"><b>Maven Dependencies</b>

    <dependencies>
        <!-- Telegram Bot Engine -->
        <dependency>
            <groupId>org.telegram</groupId>
            <artifactId>telegrambots</artifactId>
            <version>6.0.1</version>
        </dependency>

        <!-- Loggers -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.20.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.20.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.20.0</version>
        </dependency>

        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.4</version>
        </dependency>
    </dependencies>

</ol>

<h1>Technical Requirements</h1>
<ul>
    <li style="font-size: 16px"><b>Operating System:</b> Any OS that could run JDK 23, host a PostgreSQL server, and work with a GIT.</li>
    <li style="font-size: 16px"><b>Memory:</b> At least 1GB of RAM(Depends on user count, 'cause 1 user = 1 thread)</li>
    <li style="font-size: 16px"><b>Storage:</b> Around 5 MB for Application, and around 1GB for logger.</li>
    
</ul>
<h1 id="gitSetup">Installation Steps</h1>
<ol> 
    <h3>IMPORTANT: Ensure you have specified JAVA_HOME,MAVEN_HOME and already have experience with compiling, starting JAR applications through CLI.</h3>
    <li style="font-size: 16px">Clone the repository from GitHub: <br> <pre><code>git clone https://github.com/aleksejpisarenko/TelegramBot.git</code></pre></li>
    <li style="font-size: 16px">Navigate to the project directory(In CMD or similar CLI): <br> <pre><code>cd [project folder name]</code></pre></li>
    <li style="font-size: 16px">Run the Build Script(package compiles your code and creates a JAR file in the target/ directory: <br> <pre><code>mvn clean package</code></pre></li>
    <li style="font-size: 16px">Now you have your JAR file: target/TelegramBot-1.0.0</li>
    <li style="font-size: 16px">To run this JAR file locally: <pre><code>java -jar target/TelegramBot-1.0.0</code></pre></li>
</ol>

<h2>Step-by-Step setup</h2>
<ol>
    <li><h2 style="font-size: 18px">Prerequisites</h2></li>
    <ul>
        <li style="font-size: 16px"><b>JDK</b></li>
        <ul>
            <li style="font-size: 16px"> Ensure JDK 23+ is installed</li>
            <li style="font-size: 16px">Set the JAVA_HOME and MAVEN_HOME environment variable</li>
        </ul>
        <li style="font-size: 16px"><b>Maven</b></li>
        <ul>
            <li style="font-size: 16px">Ensure Maven is installed<pre><code>mvn --version</code></pre></li>
        </ul>
        <li style="font-size: 16px"><b>Database</b></li>
        <ul>
            <li style="font-size: 16px">You can use any relational database.</li>
            <li style="font-size: 16px">You just need to create 2 databases, "users" 
            and "schedule" with following column names: <br>
            for users: chatid(varchar 100 not null), isschenabled(bool not null) <br>
            for schedule: lastmodified(BIGINT not null).</li>
            <li style="font-size: 16px">That's it for database configuration.</li>
        </ul>
        <li style="font-size: 16px"><b>Configurational file</b></li>
        <ul>
            <li style="font-size: 16px">Create a file called "Config.properties" in the project folder</li>
            <li style="font-size: 16px"><a href="#gettingStarted">Here's description how to setup the config. file</a> </li>
        </ul>
        <li style="font-size: 16px"><b>Other tools</b></li>
        <ul>
            <li style="font-size: 16px">Ensure that GIT is installed, and set up.</li>
         </ul>
    </ul>
    <li><h2 style="font-size: 18px">Cloning the Repository</h2></li>    
    <ul>
        <li style="font-size: 16px"><a href="#gitSetup">Here's a cloning tutorial.</a></li>
    </ul>
    <li><h2 style="font-size: 18px">Building the Project</h2></li>
    <ul>
        <li style="font-size: 16px">Use Maven to build the porject and generate a JAR file. <pre><code>mvn clean package</code></pre></li>
    </ul>
    <li style="font-size: 16px"><b>Running the Application Locally</b></li>
    <ul>
        <li style="font-size: 16px">Locate the generated JAR file, typically in the target folder
        (e.g., target/TelegramBot-1.0.0.jar</li>
        <li style="font-size: 16px">Run the JAR file with: <pre><code>java -jar target/TelegramBot-1.0.0.jar</code></pre></li>
    </ul>
</ol>
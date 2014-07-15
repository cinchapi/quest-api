#quest
Quest is a JVM framework for quickly defining both api endpoints and view controllers in a web application. Each Quest application runs on its own embedded web server.

## Proxy Setup
You can configure a web server like Apache to act as a proxy for your Quest application.

### Apache
<VirtualHost *:80>
        ServerName $SERVER_NAME
        ServerAlias $SERVER_ALIASES
        ProxyPreserveHost On
        ProxyPass         /  http://localhost:$QUEST_PORT/
        ProxyPassReverse  /  http://localhost:$QUEST_PORT/
        <Proxy http://localhost:$QUEST_PORT/*>
        Order deny,allow
        Allow from all
        </Proxy>
</VirtualHost>

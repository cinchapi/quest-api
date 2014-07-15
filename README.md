#quest
Quest is a JVM framework for quickly defining both api endpoints and view controllers in a web application. Each Quest application runs on its own embedded web server.

## Proxy Setup
You can configure a web server like Apache to act as a proxy for your Quest application. For example, here is how you would setup a proxy for a Quest application hosted on a server named cinchapi.org and listening on port 8090. 

### Apache

        <VirtualHost *:80>
                ServerName cinchapi.org
                ServerAlias cinchapi.org. *.cinchapi.org
                ProxyPreserveHost On
                ProxyPass         /  http://localhost:8090/
                ProxyPassReverse  /  http://localhost:8090/
                <Proxy http://localhost:8090/*>
                Order deny,allow
                Allow from all
                </Proxy>
        </VirtualHost>

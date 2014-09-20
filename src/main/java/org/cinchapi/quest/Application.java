package org.cinchapi.quest;

import java.lang.reflect.Method;
import java.util.Set;

import org.cinchapi.quest.router.Router;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

import ch.qos.logback.classic.Level;
import spark.Spark;

/**
 * This is the base class for all Quest applications. Each {@link Application}
 * runs within an embedded web server and listens for requests to serve using
 * the appropriate routers.
 * <p>
 * All application logic should be defined within a {@link Router} so it isn't
 * necessary to define anything with a {@link Application} subclass. Typical
 * instantiation and running happens from the main method of some class like:
 * 
 * <pre>
 * public class MyApp extends Application {
 * 
 *     public static void main(String... args) {
 *         MyApp app = new MyApp();
 *         app.start();
 *     }
 * 
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * <em><strong>Warning:</strong> You can only run one Quest application at a time
 * within a single JVM process.</em>
 * </p>
 * 
 * @author jnelson
 */
public abstract class Application {

    /**
     * The default port for the application.
     */
    private static final int DEFAULT_PORT = 8090;

    /**
     * The port on which the application runs and serves all requests. This
     * value can be changed <strong>before</strong> the application starts
     * running using the {@link #start()} method.
     */
    private int port = DEFAULT_PORT;

    /**
     * A flag that indicates whether the application has beenm started or not.
     */
    private boolean running = false;

    /**
     * Start the application. This method is typically called from a main method.
     */
    public synchronized void start() {
        if(!running) {
            // Configure application logging
            ((ch.qos.logback.classic.Logger) LoggerFactory
                    .getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.ERROR);
            Reflections.log = null; // turn off logging

            // Configure spark
            Spark.setPort(port);
            Spark.staticFileLocation("public");

            // Register all of the routers and listen for any requests
            Reflections reflections = new Reflections();
            Set<Class<? extends Router>> routers = reflections
                    .getSubTypesOf(Router.class);
            for (Class<? extends Router> router : routers) {
                try {
                    System.out.println("Registering routes from " + router);
                    router.newInstance().routes();
                }
                catch (Exception e) {
                    continue;
                }
            }
            running = true;
        }
    }

    /**
     * Stop the application from running.
     */
    public synchronized void stop() {
        if(running) {
            try {
                Method stop = Spark.class.getDeclaredMethod("stop");
                Method clearRoutes = Spark.class
                        .getDeclaredMethod("clearRoutes");
                stop.setAccessible(true);
                clearRoutes.setAccessible(true);
                stop.invoke(null);
                clearRoutes.invoke(null);
                running = false;
            }
            catch (ReflectiveOperationException e) {
                throw Throwables.propagate(e);
            }
        }
    }

    /**
     * Set the listener port for this application. By default, the application
     * will try to use port {@value #DEFAULT_PORT}, however you can change that
     * by
     * calling this method <strong>before</strong> calling {@link #start()}.
     * 
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

}

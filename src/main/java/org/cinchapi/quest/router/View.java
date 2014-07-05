package org.cinchapi.quest.router;

import java.io.File;
import java.util.Map;

/**
 * A {@link View} is processed by a {@link Router} to display some front end template.
 * @author jeffnelson
 *
 */
public abstract class View extends AbstractRewritableRoute{

    /**
     * Construct a new instance
     * @param relativePath
     */
    public View(String relativePath) {
        super(relativePath);
    }
    
    @Override
    public Object handle() {
        return template("templates" + File.separator + template()).render(
                data());
    }

    /**
     * The name of the template to display. The View will look in the
     * {@link templates} directory at the root of the working directory for the
     * template with the returned name.
     * 
     * @return the name of the view template (e.g. index.mustache)
     */
    protected abstract String template();

    /**
     * The data to use when populating the template. This method should return a
     * mapping from a String "key" to an Object or collection of Objects with
     * the data that corresponds to the key.
     * 
     * @return the view data
     */
    protected abstract Map<String, Object> data();

}

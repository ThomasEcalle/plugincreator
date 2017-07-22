package utils;

import java.net.URISyntaxException;

/**
 * Created by Thomas Ecalle on 15/07/2017.
 */
public class Utils
{
    /**
     * get the resource's path, having its name
     *
     * @param resourceName
     * @param myClass
     * @return
     */
    public static String getResourceUrl(final String resourceName, final Class myClass)
    {
        try
        {
            return myClass.getResource("/" + resourceName).toURI().toString();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

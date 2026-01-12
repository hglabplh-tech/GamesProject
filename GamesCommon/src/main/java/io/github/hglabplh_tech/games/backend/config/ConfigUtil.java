/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.hglabplh_tech.games.backend.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Properties;

public class ConfigUtil {

    public static final String VER_KEY = "GPROP_VERKEY";
    public static final String VER_VAL = "1.00";

    public static String USER_DIR;
    public static String USER_PROPS;

    public static File PROP_FILE;

    public static File LOG_PROP_FILE;


    private static boolean userPropsLoaded = false;

    private static Properties userProperties = null;

    static {
        USER_DIR = new StringBuilder()
                .append(System.getProperty("user.home"))
                .append('/')
                .append(".hgpgames/").toString();
        USER_PROPS = new StringBuilder()
                .append(USER_DIR)
                .append("games.properties")
                .toString();
        PROP_FILE = new File(USER_PROPS);
        String logPropFileFull = USER_DIR + "tinylog.properties";
        LOG_PROP_FILE = new File(logPropFileFull);
        if (!LOG_PROP_FILE.exists()) {
            URL logPropURL = ConfigUtil.class.getResource("/logging/tinylog.properties");
            try {
                File logPropFile = new File(logPropURL.toURI());
                FileInputStream in = new FileInputStream(logPropFile);
                ByteBuffer buffer = ByteBuffer.allocateDirect(8192 * 10);
                buffer.clear();
                in.getChannel().read(buffer);
                in.getChannel().close();
                in.close();
                FileOutputStream out = new FileOutputStream(LOG_PROP_FILE);
                buffer.rewind();
                out.getChannel().write(buffer);
                out.getChannel();
                out.getChannel().close();
                out.flush();
                out.close();
                buffer.clear();
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Optional<String> getConfigValue(String configKey, String defaultValue) {
        Optional<Properties> userPropsOpt = loadUserProps();
        Optional<String> valOpt = Optional.empty();
        if (userPropsOpt.isPresent()) {
            Properties userProps = userPropsOpt.get();
            valOpt = (userProps.getProperty(configKey) != null)
                    ? Optional.of(userProps.getProperty(configKey))
                    : Optional.empty();
        }
        if (System.getProperty(configKey) != null && valOpt.isEmpty()) {
            valOpt = Optional.of(System.getProperty(configKey));
        }
        if (System.getenv(configKey) != null && valOpt.isEmpty()){
                valOpt = Optional.of(System.getenv(configKey));
        }
        if (valOpt.isPresent()) {
            return valOpt;
        } else {
            if (defaultValue != null) {
                return Optional.of(defaultValue);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<Properties> loadUserProps() {
        Properties tempProperties = new Properties();
        try {
            if (!userPropsLoaded) {
                tempProperties.load(new FileInputStream(PROP_FILE));
                userProperties = tempProperties;
                userPropsLoaded = true;
                return Optional.of(userProperties);
            } else {
                if (userProperties != null) {
                    return Optional.of(userProperties);
                } else {
                    saveUserProps(tempProperties);
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Properties getUserProperties () {
        return userProperties;
    }

    public static Boolean getUserPropertiesLoaded () {
        return userPropsLoaded;
    }

    public static void saveUserPropsIfVerChanged(Properties props) {
        if (PROP_FILE.exists()) {
            Optional<Properties> loaded = loadUserProps();
            Properties toSave = loaded.orElse(props);
            String version = toSave.getProperty(VER_KEY);
            boolean store = false;
            if (version != null) {
                if (!version.equals(VER_VAL)) {
                    store = true;
                    toSave.putAll(props);
                    toSave.setProperty(VER_KEY, VER_VAL);

                }
            } else {
                store = true;
                toSave.putAll(props);
                toSave.setProperty(VER_KEY, VER_VAL);
            }
            if (store) {
                saveUserProps(toSave);
            }
        } else {
            props.setProperty(VER_KEY, VER_VAL);
            saveUserProps(props);
        }

    }

    public static void saveUserProps(Properties props) {
        try  {
            File theDir = new File(USER_DIR);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(PROP_FILE);
            props.store(out, "");
            userProperties = props;
        } catch(IOException ex) {
            throw new IllegalStateException("User properties cannot be stored");
        }
    }
}

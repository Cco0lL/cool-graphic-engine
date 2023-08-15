package cool.kolya.engine.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import cool.kolya.engine.Properties;

import java.io.File;

public class DefaultConfiguration {

    private static final ConfigParseOptions READ_OPTS = ConfigParseOptions.defaults()
            .setSyntax(ConfigSyntax.CONF);
    private static Config CONFIG;

    /* TODO if config path is valid but there isn't a file, then copies default config from resources to
    given path
     */
    public static Config get() {
        if (CONFIG == null) {
            CONFIG = Properties.CONFIG_PATH == null ? ConfigFactory.parseResources("сonfig.conf", READ_OPTS)
                    : ConfigFactory.parseFile(new File(Properties.CONFIG_PATH), READ_OPTS);
        }
        return CONFIG;
    }
}

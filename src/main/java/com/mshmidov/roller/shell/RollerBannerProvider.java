package com.mshmidov.roller.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.shell.support.util.FileUtils;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public final class RollerBannerProvider implements BannerProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollerBannerProvider.class);

    @Override
    public String getBanner() {
//        final StringBuilder sb = new StringBuilder();
//
//        try (final InputStream banner = ClassLoader.getSystemResourceAsStream("banner")) {
//            try (final InputStreamReader reader = new InputStreamReader(banner)) {
//                sb.append(FileUtils.readBanner(reader));
//            }
//        } catch (IOException e) {
//           LOGGER.error(e.getMessage(), e);
//        }
//
//        sb.append(getVersion()).append(OsUtils.LINE_SEPARATOR);
//        sb.append(OsUtils.LINE_SEPARATOR);
        return "";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome to Roller Shell.";
    }

    @Override
    public String getProviderName() {
        return "Roller Shell";
    }
}

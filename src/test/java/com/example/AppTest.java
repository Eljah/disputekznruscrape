package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    void pageTitleShouldBePresent() {
        HtmlUnitDriver driver = new HtmlUnitDriver(false);
        String proxyUrl = System.getenv("HTTP_PROXY");
        if (proxyUrl == null || proxyUrl.isEmpty()) {
            proxyUrl = System.getenv("http_proxy");
        }
        if (proxyUrl != null && !proxyUrl.isEmpty()) {
            try {
                URL u = proxyUrl.contains("://") ? new URL(proxyUrl) : new URL("http://" + proxyUrl);
                String host = u.getHost();
                int port = u.getPort() == -1 ? u.getDefaultPort() : u.getPort();
                if (host != null && port != -1) {
                    Proxy p = new Proxy();
                    String hp = host + ":" + port;
                    p.setHttpProxy(hp);
                    p.setSslProxy(hp);
                    driver.setProxySettings(p);
                }
            } catch (MalformedURLException ignored) {}
        }
        try {
            driver.get("https://dispute.kzn.ru/disputes/1");
            assertEquals("ИС ОО", driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}

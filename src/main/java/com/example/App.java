package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class App {
    private static Proxy detectProxy() {
        String url = System.getenv("HTTP_PROXY");
        if (url == null || url.isEmpty()) {
            url = System.getenv("http_proxy");
        }
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            URL u = url.contains("://") ? new URL(url) : new URL("http://" + url);
            String host = u.getHost();
            int port = u.getPort();
            if (port == -1) {
                port = u.getDefaultPort();
            }
            if (host == null || port == -1) {
                return null;
            }
            Proxy p = new Proxy();
            String hp = host + ":" + port;
            p.setHttpProxy(hp);
            p.setSslProxy(hp);
            return p;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String browser = System.getProperty("browser", "htmlunit");
        Proxy proxy = detectProxy();
        WebDriver driver;
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            if (proxy != null) {
                options.setProxy(proxy);
            }
            driver = new FirefoxDriver(options);
        } else if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            // run in visible mode (no --headless flag)
            if (proxy != null) {
                options.setProxy(proxy);
            }
            driver = new ChromeDriver(options);
        } else {
            HtmlUnitDriver hud = new HtmlUnitDriver(false);
            if (proxy != null) {
                hud.setProxySettings(proxy);
            }
            driver = hud;
        }
        try {
            int maxId = 3;
            if (args.length > 0) {
                try {
                    maxId = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {}
            }
            for (int id = 1; id <= maxId; id++) {
                driver.get("https://dispute.kzn.ru/disputes/" + id);
                WebElement body = driver.findElement(By.tagName("body"));
                String text = body.getText();
                Files.writeString(Path.of("dispute-" + id + ".txt"), text, StandardCharsets.UTF_8);
            }
        } finally {
            driver.quit();
        }
    }
}

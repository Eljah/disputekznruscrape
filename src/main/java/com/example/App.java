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
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static List<String> extractLines(WebDriver driver, String[] ignore) {
        WebElement body = driver.findElement(By.tagName("body"));
        String text = body.getText();
        String[] lines = text.split("\\r?\\n");
        List<String> filtered = new ArrayList<>();
        outer: for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            for (String ig : ignore) {
                if (trimmed.equals(ig)) continue outer;
            }
            filtered.add(trimmed);
        }
        return filtered;
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
            HtmlUnitDriver hud = new HtmlUnitDriver(true);
            hud.getWebClient().getOptions().setThrowExceptionOnScriptError(false);
            if (proxy != null) {
                hud.setProxySettings(proxy);
            }
            driver = hud;
        }
        Path csvPath = Path.of("disputes.csv");
        boolean exists = Files.exists(csvPath);
        try (BufferedWriter out = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (!exists) {
                out.write("id,text1,text2,date\n");
            }
            int maxId = 3;
            if (args.length > 0) {
                try {
                    maxId = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {}
            }
            Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
            String[] ignore = {
                "ИСОО",
                "Войти",
                "Вернуться на главную страницу",
                "архив",
                "Пользовательское соглашение",
                "Разработано в соответствии с правилами дизайна Единого портала Госуслуг",
                "ИСOO2018-2025",
                "Информационная система «Общественные обсуждения» © 2018",
                "ИС Мнение включена в Единый реестр российских программ для электронных вычислительных машин и баз данных под реестровым номером 7575"
            };
            for (int id = 1; id <= maxId; id++) {
                System.out.println("Processing page " + id);
                driver.get("https://dispute.kzn.ru/disputes/" + id);
                if (driver instanceof HtmlUnitDriver) {
                    ((HtmlUnitDriver) driver).getWebClient().waitForBackgroundJavaScript(5000);
                }

                List<String> filtered = extractLines(driver, ignore);
                String t1 = filtered.size() > 0 ? filtered.get(0) : "";
                String t2 = filtered.size() > 1 ? filtered.get(1) : "";

                int attempts = 0;
                while ("Идет загрузка".equals(t1) && "Это может занять некоторое время".equals(t2)) {
                    if (attempts++ >= 12) { // ~1 minute passed
                        driver.get("https://dispute.kzn.ru/disputes/" + id);
                        attempts = 0;
                        if (driver instanceof HtmlUnitDriver) {
                            ((HtmlUnitDriver) driver).getWebClient().waitForBackgroundJavaScript(5000);
                        }
                    } else if (driver instanceof HtmlUnitDriver) {
                        ((HtmlUnitDriver) driver).getWebClient().waitForBackgroundJavaScript(5000);
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ignored) {}
                    }
                    filtered = extractLines(driver, ignore);
                    t1 = filtered.size() > 0 ? filtered.get(0) : "";
                    t2 = filtered.size() > 1 ? filtered.get(1) : "";
                }
                Matcher m = datePattern.matcher(t1 + " " + t2);
                String date = m.find() ? m.group() : "";
                out.write(id + ",\"" + t1.replace("\"", "\"\"") + "\",\"" +
                          t2.replace("\"", "\"\"") + "\"," + date + "\n");
                out.flush();
            }
        } finally {
            driver.quit();
        }
    }
}

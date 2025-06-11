package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws IOException {
        String browser = System.getProperty("browser", "htmlunit");
        WebDriver driver;
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        } else {
            driver = new HtmlUnitDriver(true);
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

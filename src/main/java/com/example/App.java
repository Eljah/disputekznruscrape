package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws IOException {
        // Download a fresh ChromeDriver in case a mismatched version is cached
        WebDriverManager.chromedriver().clearDriverCache().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");

        WebDriver driver = new ChromeDriver(options);
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

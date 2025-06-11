package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    void pageTitleShouldBePresent() {
        WebDriver driver = new HtmlUnitDriver(true);
        try {
            driver.get("https://dispute.kzn.ru/disputes/1");
            assertEquals("ИС ОО", driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}

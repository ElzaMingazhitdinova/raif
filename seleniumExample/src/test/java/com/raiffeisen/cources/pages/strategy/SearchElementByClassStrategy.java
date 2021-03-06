package com.raiffeisen.cources.pages.strategy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchElementByClassStrategy extends SearchElementStrategy {
    public SearchElementByClassStrategy(WebDriver driver) {
        super(driver);
    }

    public WebElement searchElementByLocator(String locator) {
        return driver.findElement(By.className(locator));
    }
}

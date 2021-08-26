package com.phantasment.uiautomationtest2.ui;

import com.phantasment.uiautomationtest2.TheInternetTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage
{
    private WebDriver webDriver;

    public MainPage(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }

    public void gotoPage(String urlEndpoint)
    {
        webDriver.get(TheInternetTests.completeURL() + urlEndpoint);
    }

    public WebElement getButtonByName(String name)
    {
        for (WebElement element : webDriver.findElements(By.tagName("button")))
        {
            if (element.getText().equals(name))
            {
                return element;
            }
        }

        return null;
    }

    public void waitForElementClickable(WebElement e)
    {
        new WebDriverWait(webDriver, 5L).until(ExpectedConditions.elementToBeClickable(e));
    }
}

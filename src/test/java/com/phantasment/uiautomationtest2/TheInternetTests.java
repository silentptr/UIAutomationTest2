package com.phantasment.uiautomationtest2;

import com.phantasment.uiautomationtest2.ui.MainPage;
import com.phantasment.uiautomationtest2.util.ImageChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.logging.Logger;

public class TheInternetTests
{
    public static final String BASE_URL = "the-internet.herokuapp.com/";

    public static String completeURL()
    {
        return "https://" + BASE_URL;
    }

    private WebDriver webDriver;
    private MainPage mainPage;

    @BeforeEach
    public void testSetup()
    {
        webDriver = new ChromeDriver();
        gotoMainPage();
        webDriver.manage().window().maximize();
        mainPage = new MainPage(webDriver);
    }

    public void gotoMainPage()
    {
        webDriver.get(completeURL());
    }

    @Test
    public void addRemoveElementsTests()
    {
        gotoMainPage();
        mainPage.gotoPage("add_remove_elements/");
        WebElement addElement = mainPage.getButtonByName("Add Element");
        Assertions.assertNotNull(addElement);
        addElement.click();
        WebElement deleteElement = mainPage.getButtonByName("Delete");
        mainPage.waitForElementClickable(deleteElement);
        deleteElement.click();
    }

    @Test
    public void basicAuthTest()
    {
        gotoMainPage();
        webDriver.get("https://admin:admin@" + BASE_URL + "/basic_auth/");
        WebElement message = webDriver.findElement(By.className("example")).findElement(By.tagName("p"));
        Assertions.assertEquals("Congratulations! You must have the proper credentials.", message.getText());
    }

    @Test
    public void brokenImagesTest()
    {
        gotoMainPage();
        mainPage.gotoPage("broken_images");
        List<WebElement> elements = webDriver.findElement(By.id("content")).findElements(By.tagName("img"));
        Assertions.assertEquals(3, elements.size());

        for (int i = 0; i < 3; ++i)
        {
            boolean validImage = false;

            try
            {
                validImage = ImageChecker.isValidImage(elements.get(i).getAttribute("src"));
            }
            catch (Throwable t) { t.printStackTrace(); }

            Assertions.assertEquals(i == 2 ? true : false, validImage);
        }
    }

    @Test
    public void checkboxesTest()
    {
        gotoMainPage();
        mainPage.gotoPage("checkboxes");
        WebElement form = webDriver.findElement(By.id("content")).findElement(By.tagName("form"));

        for (WebElement e : form.findElements(By.cssSelector("input[type=checkbox]")))
        {
            e.click();
        }
    }

    @Test
    public void contextMenuTest()
    {
        gotoMainPage();
        mainPage.gotoPage("context_menu");
        WebElement hotSpot = webDriver.findElement(By.id("hot-spot"));
        Actions actions = new Actions(webDriver);
        actions.contextClick(hotSpot).perform();
        webDriver.switchTo().alert().dismiss();
    }

    @Test
    public void dragAndDropTest()
    {
        gotoMainPage();
        mainPage.gotoPage("drag_and_drop");
        WebElement a = webDriver.findElement(By.id("column-a"));
        WebElement b = webDriver.findElement(By.id("column-b"));
        new Actions(webDriver).dragAndDrop(a, b).build().perform();
    }

    @AfterEach
    public void testCleanup()
    {
        //webDriver.quit();
    }
}

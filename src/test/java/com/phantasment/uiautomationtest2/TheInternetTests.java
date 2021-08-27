package com.phantasment.uiautomationtest2;

import com.phantasment.uiautomationtest2.ui.MainPage;
import com.phantasment.uiautomationtest2.util.ImageChecker;
import com.phantasment.uiautomationtest2.util.NumberChecker;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
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
        new WebDriverWait(webDriver, 2L).until(ExpectedConditions.elementToBeClickable(a));
        WebElement b = webDriver.findElement(By.id("column-b"));
        Actions action = new Actions(webDriver);
        action.clickAndHold(a).moveToElement(b).release().build().perform();
    }

    @Test
    public void dropdownTest()
    {
        gotoMainPage();
        mainPage.gotoPage("dropdown");
        WebElement dropdown = webDriver.findElement(By.id("dropdown"));
        List<WebElement> options = dropdown.findElements(By.cssSelector("option:not([disabled='disabled'])"));

        for (int i = 0; i < options.size(); ++i)
        {
            WebElement option = options.get(i);
            dropdown.click();
            option.click();
            Assertions.assertEquals(option.getText(), "Option " + (i + 1));
        }
    }

    @Test
    public void dynamicContentTest()
    {
        gotoMainPage();
        mainPage.gotoPage("dynamic_content");
        List<WebElement> rows = webDriver.findElement(By.id("content")).findElements(By.className("row"));

        for (WebElement row : rows)
        {
            WebElement image = row.findElement(By.className("large-2")).findElement(By.tagName("img"));

            try
            {
                Assertions.assertTrue(ImageChecker.isValidImage(image.getAttribute("src")));
            }
            catch (Throwable t)
            {
                Assertions.fail(t);
            }

            WebElement text = row.findElement(By.className("large-10"));
            Assertions.assertTrue(text.getText().length() > 0);
        }
    }

    @Test
    public void entryAdTest()
    {
        gotoMainPage();
        mainPage.gotoPage("entry_ad");
        WebElement modalFooter = webDriver.findElement(By.className("modal-footer"));
        new WebDriverWait(webDriver, 5L).until(ExpectedConditions.elementToBeClickable(modalFooter));
        modalFooter.findElement(By.tagName("p")).click();
        Assertions.assertEquals("Displays an ad on page load.", webDriver.findElement(By.className("example")).findElement(By.tagName("p")).getText());
    }

    @Test
    public void forgotPasswordTest()
    {
        gotoMainPage();
        mainPage.gotoPage("forgot_password");
        WebElement emailField = webDriver.findElement(By.cssSelector("input[type='text']"));
        emailField.sendKeys("testemail@email.com");
        WebElement submit = webDriver.findElement(By.id("form_submit"));
        submit.click();
        new WebDriverWait(webDriver, 10L).until(ExpectedConditions.elementToBeClickable(By.tagName("body")));
        Assertions.assertEquals("Internal Server Error", webDriver.findElement(By.tagName("h1")).getText());
    }

    @Test
    public void loginTest()
    {
        gotoMainPage();
        mainPage.gotoPage("login");
        WebElement usernameField = webDriver.findElement(By.id("username"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement submit = webDriver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("wrongusername");
        passwordField.sendKeys("wrongpassword");
        submit.click();
        WebElement flashError = webDriver.findElement(By.id("flash"));
        new WebDriverWait(webDriver, 10L).until(ExpectedConditions.visibilityOf(flashError));
        Assertions.assertTrue(flashError.getText().startsWith("Your username is invalid!"));
        usernameField = webDriver.findElement(By.id("username"));
        passwordField = webDriver.findElement(By.id("password"));
        submit = webDriver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("tomsmith");
        passwordField.sendKeys("SuperSecretPassword!");
        submit.click();

        WebElement flashSuccess = webDriver.findElement(By.cssSelector("div[class='flash success']"));
        new WebDriverWait(webDriver, 10L).until(ExpectedConditions.visibilityOf(flashSuccess));
        Assertions.assertTrue(flashSuccess.getText().startsWith("You logged into a secure area!"));
    }

    @Test
    public void nestedFramesTest()
    {
        gotoMainPage();
        mainPage.gotoPage("nested_frames");
        List<WebElement> frames = webDriver.findElements(By.tagName("frame"));

        for (int i = 0; i < frames.size(); ++i)
        {
            WebElement frame = frames.get(i);
            new WebDriverWait(webDriver, 10L).until(ExpectedConditions.visibilityOf(frame));
            webDriver.switchTo().frame(frame);
            String expectedText = null;

            switch (i)
            {
                case 0:
                    expectedText = "LEFT";
                    break;
                case 1:
                    expectedText = "MIDDLE";
                    break;
                case 2:
                    expectedText = "RIGHT";
                    break;
                case 3:
                    expectedText = "BOTTOM";
                    break;
                default:
                    Assertions.fail("too many frames");
                    break;
            }

            String text = frame.findElement(By.tagName("body")).getText();
            Logger.getLogger(getClass().getName()).info("" + i + ": " + text);
            //Assertions.assertEquals(expectedText, frame.getText());
            Assertions.assertTrue(text.contains(expectedText));
        }
    }

    @Test
    public void geolocationTest()
    {
        gotoMainPage();
        mainPage.gotoPage("geolocation");
        WebElement button = webDriver.findElement(By.tagName("button"));
        button.click();
        new WebDriverWait(webDriver, 60L).until(ExpectedConditions.elementToBeClickable(By.id("lat-value")));
        WebElement latitude = webDriver.findElement(By.id("lat-value"));
        WebElement longitude = webDriver.findElement(By.id("long-value"));
        Assertions.assertTrue(NumberChecker.isDouble(latitude.getText()).isPresent());
        Assertions.assertTrue(NumberChecker.isDouble(longitude.getText()).isPresent());
    }

    @Test
    public void horizontalSliderTest()
    {
        gotoMainPage();
        mainPage.gotoPage("horizontal_slider");
        WebElement slider = webDriver.findElement(By.cssSelector("input[type='range']"));
        Actions action = new Actions(webDriver);
        action.clickAndHold(slider).moveByOffset(10, 0).release().build().perform();
        String text = webDriver.findElement(By.cssSelector("span[id='range']")).getText();
        Optional<Double> value = NumberChecker.isDouble(text);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals(3, value.get());
    }

    @Test
    public void hoverTest()
    {
        gotoMainPage();
        mainPage.gotoPage("hovers");
        List<WebElement> figures = webDriver.findElements(By.className("figure"));

        for (WebElement figure : figures)
        {
            WebElement img = figure.findElement(By.tagName("img"));
            new Actions(webDriver).moveToElement(img).build().perform();
            WebElement caption = figure.findElement(By.className("figcaption"));
            new WebDriverWait(webDriver, 2L).until(ExpectedConditions.visibilityOf(caption));
        }
    }

    @AfterEach
    public void testCleanup()
    {
        //webDriver.quit();
    }
}

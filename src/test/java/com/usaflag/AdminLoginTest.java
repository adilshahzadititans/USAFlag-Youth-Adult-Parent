package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Admin Login Test for USA Flag Application
 */
public class AdminLoginTest {
    
    private WebDriver driver;
    
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        driver.get("https://stage-usaflag-admin-portal-hefnfdgvhdgwfbck.eastus2-01.azurewebsites.net/login");
    }
    
    @Test
    public void testAdminLogin() {
        // Find email field and enter email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("rony13ching@gmail.com");
        
        // Find password field and enter password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Flag!2025");

         // Find and click on the checkbox
         WebElement checkBox = driver.findElement(By.xpath("//div[@class='w-5 h-5 flex items-center justify-center rounded-sm border border-red bg-light-white']"));
         checkBox.click();
        
        // Click submit button
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        System.out.println("✅ Admin login test completed!");
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            //driver.quit();
        }
    }
}
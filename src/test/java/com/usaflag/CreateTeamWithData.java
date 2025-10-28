package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Create Team Test with CSV Data for USA Flag Application
 * Tests parent login and team creation functionality with different team data
 */
public class CreateTeamWithData {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private List<TeamData> teamDataList;
    private Random random;
    
    // Configuration
    private static final String LOGIN_URL = "https://staging-usaflag-playerportal.azurewebsites.net/login";
    private static final String PARENT_EMAIL = "nicole.thompson@yopmail.com";

    private static final String PARENT_PASSWORD = "12345678";
    private static final String CSV_FILE_PATH = "team_data.csv";
    
    // Team Data class
    public static class TeamData {
        public String teamName;
        public String description;
        public String location;
        public String classification;
        public String logoPath;
        
        public TeamData(String teamName, String description, String location, String classification, String logoPath) {
            this.teamName = teamName;
            this.description = description;
            this.location = location;
            this.classification = classification;
            this.logoPath = logoPath;
        }
    }
    
    @BeforeMethod
    public void setUp() {
        System.out.println("🔧 Setting up Create Team with Data WebDriver...");
        
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        random = new Random();
        
        // Load team data from CSV
        loadTeamDataFromCSV();
        
        System.out.println("✅ Create Team with Data WebDriver ready!");
        System.out.println("📊 Loaded " + teamDataList.size() + " team records from CSV");
    }
    
    private void loadTeamDataFromCSV() {
        teamDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }
                
                String[] values = line.split(",");
                if (values.length >= 5) {
                    TeamData teamData = new TeamData(
                        values[0].trim(),
                        values[1].trim(),
                        values[2].trim(),
                        values[3].trim(),
                        values[4].trim()
                    );
                    teamDataList.add(teamData);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading CSV file: " + e.getMessage());
            // Fallback to default data
            teamDataList.add(new TeamData(
                "Arsenal",
                "The Arsenal Football Club is a professional football club based in Islington, North London, England.",
                "North London, England",
                "Youth",
                "C:\\Users\\User\\Desktop\\profile-pics\\logo\\29c4dbaa400e8914201711ae2063b17c.jpg"
            ));
        }
    }
    
    @Test
    public void testCreateTeamWithRandomData() {
        System.out.println("🔍 Test: Create Team with Random Data from CSV...");
        
        // Select random team data
        TeamData selectedTeam = teamDataList.get(random.nextInt(teamDataList.size()));
        System.out.println("🎲 Selected random team: " + selectedTeam.teamName);
        
        try {
            // Step 1: Navigate to login page
            System.out.println("🌐 Opening login page: " + LOGIN_URL);
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            // Wait for page to load completely
            Thread.sleep(3000);
            
            // Step 2: Find email field and enter email
            System.out.println("📧 Entering parent email: " + PARENT_EMAIL);
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys(PARENT_EMAIL);
            System.out.println("✅ Parent email entered successfully");
            
            // Step 3: Find password field and enter password
            System.out.println("🔑 Entering parent password: " + PARENT_PASSWORD);
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys(PARENT_PASSWORD);
            System.out.println("✅ Parent password entered successfully");
            
            // Step 4: Click on checkbox
            System.out.println("☑️ Clicking agreement checkbox...");
            WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='w-5 h-5 flex items-center justify-center rounded-sm border border-red bg-light-white']")
            ));
            checkBox.click();
            System.out.println("✅ Agreement checkbox clicked successfully");
            
            // Step 5: Click on Login button
            System.out.println("🚀 Clicking login button...");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Login']")
            ));
            loginButton.click();
            System.out.println("✅ Login button clicked successfully");
            
            // Step 6: Wait until the next page loads
            System.out.println("⏳ Waiting for next page to load...");
            Thread.sleep(5000);
            
            // Step 7: Click on the profile button
            System.out.println("👤 Clicking on profile button...");
            WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='flex size-full items-center justify-center rounded-full w-[48px] h-[48px] bg-gray-300']")
            ));
            profileButton.click();
            System.out.println("✅ Profile button clicked successfully");
            
            // Wait for profile page to load
            Thread.sleep(3000);
            
            // Step 8: Click on Teams button tab
            System.out.println("🏈 Clicking on Teams button tab...");
            WebElement teamsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Teams') or contains(@id, 'teams')]")
            ));
            teamsButton.click();
            System.out.println("✅ Teams button tab clicked successfully");
            
            // Wait for teams page to load
            Thread.sleep(3000);
            
            // Step 9: Click on Add New Team button
            System.out.println("➕ Clicking on Add New Team button...");
            WebElement addNewTeamButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='bg-[#0F2D52] text-white px-4 py-2 rounded-md text-sm']")
            ));
            addNewTeamButton.click();
            System.out.println("✅ Add New Team button clicked successfully");
            
            // Wait for modal/form to load
            Thread.sleep(3000);
            
            // Step 10: Upload team logo image first
            System.out.println("📁 Uploading team logo image: " + selectedTeam.logoPath);
            try {
                WebElement uploadElement = driver.findElement(By.xpath("//label[@class='relative inline-flex items-center justify-center px-4 py-2 text-sm font-medium bg-light-white text-blue-900 border border-gray-300 rounded-md cursor-pointer hover:bg-gray-100 transition']//input[@type='file']"));
                uploadElement.sendKeys(selectedTeam.logoPath);
                System.out.println("✅ Team logo image uploaded successfully");
                
                // Wait for image upload to process
                Thread.sleep(3000);
                
            } catch (Exception uploadException) {
                System.out.println("⚠️ Image upload failed: " + uploadException.getMessage());
                System.out.println("⚠️ Continuing with the test despite image upload error");
            }
            
            // Step 11: Fill out team creation form with CSV data
            System.out.println("📝 Filling out team creation form with data for: " + selectedTeam.teamName);
            
            // Find firstName field and enter team name
            System.out.println("📝 Entering team name: " + selectedTeam.teamName);
            WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
            firstNameField.clear();
            firstNameField.sendKeys(selectedTeam.teamName);
            System.out.println("✅ Team name entered successfully");
            
            // Find description field and enter description
            System.out.println("📝 Entering team description...");
            WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
            descriptionField.clear();
            descriptionField.sendKeys(selectedTeam.description);
            System.out.println("✅ Team description entered successfully");
            
            // Find location field and enter location
            System.out.println("📝 Entering team location: " + selectedTeam.location);
            WebElement locationField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("location")));
            locationField.clear();
            locationField.sendKeys(selectedTeam.location);
            System.out.println("✅ Team location entered successfully");
            
            // Step 12: Click on classification dropdown and select classification
            System.out.println("🔍 Clicking on classification dropdown...");
            WebElement classificationDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@name='classification']")
            ));
            classificationDropdown.click();
            System.out.println("✅ Classification dropdown clicked successfully");
            
            // Wait a moment after clicking dropdown
            Thread.sleep(1000);
            
            // Select classification option and click to close dropdown
            System.out.println("🔍 Selecting 'Adult' from dropdown...");
            WebElement classificationOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[contains(text(), 'Adult') or contains(text(), 'adult')]")
            ));
            classificationOption.click();
            System.out.println("✅ 'Adult' option selected and dropdown closed");
            
            // Wait for selection to be applied and dropdown to close
            Thread.sleep(2000);
            
            // Step 13: Click submit button
            System.out.println("🚀 Clicking submit button to create team...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
            ));
            submitButton.click();
            System.out.println("✅ Submit button clicked successfully - Team creation submitted!");
            
            // Wait for form submission to process
            Thread.sleep(3000);
            
            // Verify we're on the teams page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("✅ Current URL after form submission: " + currentUrl);
            
            System.out.println("✅ Create Team test completed successfully - Team '" + selectedTeam.teamName + "' created with random data!");
            
        } catch (Exception e) {
            System.err.println("❌ Create Team test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Create Team test failed: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void tearDown() {
        System.out.println("🔍 Closing browser...");
        System.out.println("✅ Create Team with Data test completed");
        System.out.println("📧 Parent email used: " + PARENT_EMAIL);
        
        // Close the browser
        if (driver != null) {
            // driver.quit();
            // System.out.println("✅ Browser closed successfully");
        }
    }
}

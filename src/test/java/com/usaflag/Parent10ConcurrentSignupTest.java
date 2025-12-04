package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Parent 10 Concurrent Signup Test
 * - Opens 10 browsers concurrently
 * - Each browser consumes a fresh, unique row from ParentConcurrentSignup.csv
 * - Records successful signups to ParentConcurrentSignup_Results.csv
 */
public class Parent10ConcurrentSignupTest {

    private static final String LOGIN_URL = "https://staging-usaflag-playerportal.azurewebsites.net/login"; // stage URL
    // private static final String LOGIN_URL = "https://prod-usaflag-player-portal.azurewebsites.net/"; // production URL
    private static final String CSV_FILE_PATH = "ParentConcurrentSignup.csv";
    private static final String SUCCESS_CSV_FILE_PATH = "ParentConcurrentSignup_Results.csv";

    private static class UserData {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;
        private final String dateOfBirth;

        public UserData(String firstName, String lastName, String email, String phone, String dateOfBirth) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.dateOfBirth = dateOfBirth;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDateOfBirth() { return dateOfBirth; }
    }

    private static List<UserData> loadUserDataFromCSV() {
        List<UserData> userDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                String[] c = line.split(",");
                if (c.length >= 5) {
                    userDataList.add(new UserData(c[0].trim(), c[1].trim(), c[2].trim(), c[3].trim(), c[4].trim()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV: " + e.getMessage(), e);
        }
        return userDataList;
    }

    private static synchronized void appendSuccessfulEmail(String email, int index, String threadName) {
        PrintWriter writer = null;
        try {
            java.io.File file = new java.io.File(SUCCESS_CSV_FILE_PATH);
            boolean exists = file.exists();
            writer = new PrintWriter(new FileWriter(file, true));
            if (!exists) {
                writer.println("Email,Timestamp,Index,Thread");
            }
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println(email + "," + ts + "," + index + "," + threadName);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write success CSV: " + e.getMessage());
        } finally {
            if (writer != null) writer.close();
        }
    }

    private static void initializeCsv(String path, String header) {
        PrintWriter writer = null;
        try {
            java.io.File file = new java.io.File(path);
            if (!file.exists()) {
                writer = new PrintWriter(new FileWriter(file, true));
                writer.println(header);
            }
        } catch (IOException ignored) {
        } finally {
            if (writer != null) writer.close();
        }
    }

    private static WebDriver newDriver() {
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    /**
     * Select the country "United States" using the specific className selector.
     */
    private static void selectCountryUnitedStates(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        System.out.println("🌍 Selecting country: United States");
        
        try {
            // Find country field by className css-19bb58m
            WebElement countryField = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("css-19bb58m"))
            );
            
            // Scroll into view to ensure it's visible
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", countryField);
            Thread.sleep(300);
            
            // Click on the country field
            countryField.click();
            System.out.println("✅ Country field clicked");
            Thread.sleep(1500); // Wait for dropdown to open and input to appear
            
            // Find the input field - it appears after clicking, might be in the dropdown menu
            WebElement countryInput = null;
            try {
                // First try to find input with id containing react-select
                countryInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id,'react-select')]")
                ));
            } catch (Exception e1) {
                try {
                    // Try any visible input that appears after dropdown opens
                    List<WebElement> inputs = driver.findElements(By.xpath("//input[@type='text']"));
                    for (WebElement input : inputs) {
                        try {
                            if (input.isDisplayed() && input.isEnabled()) {
                                countryInput = input;
                                break;
                            }
                        } catch (Exception ignore) {}
                    }
                } catch (Exception e2) {
                    // Last resort: find any input
                    countryInput = driver.findElement(By.xpath("//input[@type='text']"));
                }
            }
            
            // Send keys "United States"
            if (countryInput != null) {
                countryInput.click();
                Thread.sleep(200);
                countryInput.clear();
                Thread.sleep(200);
                countryInput.sendKeys("United States");
                System.out.println("✅ Typed 'United States' in country input");
                Thread.sleep(2000); // Wait for dropdown options to appear and filter
            } else {
                throw new RuntimeException("Could not find input field after clicking country dropdown");
            }
            
            // Select "United States" from dropdown (exclude "Virgin Islands" variants)
            WebElement usOption = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space(text())='United States' and not(contains(text(),'Virgin'))]"))
            );
            
            // Scroll option into view
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", usOption);
            Thread.sleep(300);
            
            usOption.click();
            System.out.println("✅ Selected 'United States' from dropdown");
            Thread.sleep(500);
            
        } catch (Exception e) {
            System.err.println("❌ Failed to select country: " + e.getMessage());
            throw new RuntimeException("Failed to select country 'United States'", e);
        }
    }

    private static void signupFlow(UserData u, int index) {
        WebDriver driver = null;
        WebDriverWait wait = null;
        try {
            driver = newDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Navigate to login page
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            // Wait for page to load completely
            Thread.sleep(3000);
            
            // Find and click "Create An Account" button
            System.out.println("🔍 [Thread " + Thread.currentThread().getName() + "] Looking for 'Create An Account' button...");
            WebElement createAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Create An Account']"))
            );
            createAccountButton.click();
            System.out.println("✅ 'Create An Account' button clicked successfully");
            
            // Wait for page navigation
            Thread.sleep(2000);

            // Find and click "Parental Sign Up" div
            System.out.println("🔍 Looking for 'Parental Sign Up' div...");
            WebElement parentalSignUpDiv = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space()='Parental Sign Up']"))
            );
            parentalSignUpDiv.click();
            System.out.println("✅ 'Parental Sign Up' div clicked successfully");
            
            // Wait for form to load
            Thread.sleep(2000);

            // Fill first name
            System.out.println("📝 Entering first name: " + u.getFirstName());
            WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
            firstNameField.clear();
            firstNameField.sendKeys(u.getFirstName());
            System.out.println("✅ First name entered successfully");

            // Fill last name
            System.out.println("📝 Entering last name: " + u.getLastName());
            WebElement lastNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
            lastNameField.clear();
            lastNameField.sendKeys(u.getLastName());
            System.out.println("✅ Last name entered successfully");

            // Fill email
            System.out.println("📧 Entering email: " + u.getEmail());
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys(u.getEmail());
            System.out.println("✅ Email entered successfully");

            // Fill phone number
            System.out.println("📞 Entering phone: " + u.getPhone());
            WebElement phoneField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='+xx xxxx xxxxxx']"))
            );
            phoneField.clear();
            phoneField.sendKeys(u.getPhone());
            System.out.println("✅ Phone number entered successfully");
            
            // Wait a bit before country selection to ensure form is ready
            Thread.sleep(500);

            // Select country (robust exact match)
            selectCountryUnitedStates(driver, wait);

            // Fill date of birth
            System.out.println("📅 Entering date of birth: " + u.getDateOfBirth());
            WebElement dateOfBirthField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dateOfBirth")));
            dateOfBirthField.clear();
            dateOfBirthField.sendKeys(u.getDateOfBirth());
            System.out.println("✅ Date of birth entered successfully");

            // Select Male radio button
            System.out.println("👤 Selecting Male radio button...");
            WebElement maleRadioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Male']")));
            maleRadioButton.click();
            System.out.println("✅ Male radio button selected successfully");

            // Click agreement checkbox
            System.out.println("☑️ Clicking agreement checkbox...");
            WebElement agreementCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='agreement']")));
            
            // Scroll to checkbox to ensure it's visible
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", agreementCheckbox);
            Thread.sleep(500);
            
            agreementCheckbox.click();
            System.out.println("✅ Agreement checkbox clicked successfully");
            
            // Wait a moment for the checkbox state to be processed and button to become enabled
            Thread.sleep(1000);
            
            // Click Create An Account submit button
            System.out.println("🚀 Clicking Create An Account submit button...");
            
            // Try multiple approaches to find the submit button
            WebElement submitButton = null;
            try {
                // Approach 1: Look for button with specific classes and text
                submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='shadow-md bg-dark-red text-gray-25 mt-3 w-full undefined' and @type='submit']")
                ));
                System.out.println("✅ Found submit button using specific class selector");
            } catch (Exception e1) {
                try {
                    // Approach 2: Look for button with text "Create An Account"
                    submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'shadow-md') and contains(@class, 'bg-dark-red') and contains(text(), 'Create An Account')]")
                    ));
                    System.out.println("✅ Found submit button using text and partial class selector");
                } catch (Exception e2) {
                    try {
                        // Approach 3: Look for any button with "Create An Account" text
                        submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[normalize-space()='Create An Account']")
                        ));
                        System.out.println("✅ Found submit button using text selector");
                    } catch (Exception e3) {
                        // Approach 4: Fallback to generic submit button
                        submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@type='submit']")
                        ));
                        System.out.println("✅ Found submit button using generic submit selector");
                    }
                }
            }
            
            // Scroll to button to ensure it's visible
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
            Thread.sleep(500);
            
            // Try to click the button, with JavaScript fallback
            try {
                submitButton.click();
                System.out.println("✅ Create An Account submit button clicked successfully using regular click");
            } catch (Exception clickError) {
                System.out.println("⚠️ Regular click failed, trying JavaScript click...");
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
                    System.out.println("✅ Create An Account submit button clicked successfully using JavaScript click");
                } catch (Exception jsClickError) {
                    System.out.println("❌ Both regular and JavaScript click failed: " + jsClickError.getMessage());
                    throw jsClickError;
                }
            }
            
            // Wait for form submission
            Thread.sleep(3000);

            // YOPmail navigation to get OTP
            String verificationEmail = u.getEmail();
            String verificationCode = null;
            
            try {
                // Open new tab and navigate to YOPmail
                System.out.println("🌐 Opening YOPmail in new tab...");
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open('https://yopmail.com/', '_blank');");
                
                // Switch to the new tab
                java.util.Set<String> tabs = driver.getWindowHandles();
                String[] tabArray = tabs.toArray(new String[0]);
                driver.switchTo().window(tabArray[1]);
                System.out.println("✅ Switched to YOPmail tab");
                
                // Wait for YOPmail to load
                Thread.sleep(3000);
                
                // Enter email in YOPmail login field
                System.out.println("📧 Entering email in YOPmail: " + verificationEmail);
                WebElement yopmailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='login']")));
                yopmailInput.clear();
                yopmailInput.sendKeys(verificationEmail);
                System.out.println("✅ Email entered in YOPmail");
                
                // Click the check email button
                System.out.println("🔍 Clicking check email button...");
                WebElement checkEmailButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='material-icons-outlined f36']")));
                checkEmailButton.click();
                System.out.println("✅ Check email button clicked");
                
                // Wait for inbox to load
                Thread.sleep(3000);
                
                // Find the 6-digit verification code
                System.out.println("🔢 Looking for 6-digit verification code...");
                
                // Wait for the email iframe to load
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ifmail")));
                
                // Switch to the iframe containing the email content
                driver.switchTo().frame("ifmail");
                System.out.println("✅ Switched to email iframe");
                
                // Wait for email content to load
                Thread.sleep(2000);
                
                // Look for the OTP element with specific styling
                WebElement otpElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(@style, 'font-size: 24px') and contains(@style, 'font-weight: bold')]"))
                );
                
                String otpText = otpElement.getText();
                System.out.println("📧 OTP element text: " + otpText);
                
                // Extract 6-digit code using regex
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{6}");
                java.util.regex.Matcher matcher = pattern.matcher(otpText);
                if (matcher.find()) {
                    verificationCode = matcher.group();
                    System.out.println("✅ Found 6-digit code: " + verificationCode);
                }
                
                // Switch back from iframe
                driver.switchTo().defaultContent();
                System.out.println("✅ Switched back from email iframe");
                
                // Navigate back to verification page
                System.out.println("🔄 Navigating back to verification page...");
                driver.switchTo().window(tabArray[0]);
                System.out.println("✅ Switched back to verification page");
                
            } catch (Exception e) {
                System.out.println("⚠️ Could not extract OTP from YOPmail: " + e.getMessage());
                // verificationCode = "111111"; // Commented out - use actual OTP from email
                throw new RuntimeException("Failed to extract verification code from email", e);
            }
            
            if (verificationCode == null) {
                throw new RuntimeException("Failed to extract verification code from email");
            }

            // Verification page OTP input
            WebElement otpInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp-input-0")));
            otpInput.clear();
            otpInput.sendKeys(verificationCode);

            WebElement otpSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            otpSubmit.click();

            Thread.sleep(3000);

            // Password creation
            WebElement pwd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            pwd.clear();
            pwd.sendKeys("12345678");
            WebElement cpwd = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirmPassword")));
            cpwd.clear();
            cpwd.sendKeys("12345678");
            WebElement pwdSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            pwdSubmit.click();

            Thread.sleep(2000);

            appendSuccessfulEmail(u.getEmail(), index, Thread.currentThread().getName());
        } catch (Throwable t) {
            System.err.println("Concurrent signup failed for index " + index + ": " + t.getMessage());
        } finally {
            try { if (driver != null) driver.quit(); } catch (Exception ignore) {}
        }
    }

    @Test
    public void runTenConcurrentSignups() throws InterruptedException {
        List<UserData> users = loadUserDataFromCSV();
        if (users.isEmpty()) throw new RuntimeException("No users found in CSV");

        // Prepare CSV header
        initializeCsv(SUCCESS_CSV_FILE_PATH, "Email,Timestamp,Index,Thread");

        final int batchSize = 10;
        final int totalUsers = users.size();
        
        System.out.println("🚀 Starting batch processing for " + totalUsers + " users in batches of " + batchSize);
        
        // Process users in batches of 10
        for (int batchStart = 0; batchStart < totalUsers; batchStart += batchSize) {
            int batchEnd = Math.min(batchStart + batchSize, totalUsers);
            int currentBatch = (batchStart / batchSize) + 1;
            int totalBatches = (int) Math.ceil((double) totalUsers / batchSize);
            
            System.out.println("============= 📦 Batch " + currentBatch + " / " + totalBatches + " (Users " + (batchStart + 1) + "-" + batchEnd + ") =============");
            
            ExecutorService pool = Executors.newFixedThreadPool(batchSize);
            
            // Submit tasks for current batch
            for (int i = 0; i < batchSize && (batchStart + i) < totalUsers; i++) {
                final int userIndex = batchStart + i;
                pool.submit(() -> {
                    try {
                        signupFlow(users.get(userIndex), userIndex);
                    } catch (Exception e) {
                        System.err.println("❌ Error in batch " + currentBatch + " for user index " + userIndex + ": " + e.getMessage());
                    }
                });
            }
            
            // Wait for current batch to complete
            pool.shutdown();
            boolean finished = pool.awaitTermination(60, TimeUnit.MINUTES);
            
            if (!finished) {
                System.err.println("⚠️ Batch " + currentBatch + " did not complete within timeout");
                pool.shutdownNow();
            } else {
                System.out.println("✅ Batch " + currentBatch + " completed successfully");
            }
            
            // Small delay between batches to avoid overwhelming the system
            if (batchEnd < totalUsers) {
                Thread.sleep(2000);
            }
        }
        
        System.out.println("✅ All batches completed! Processed " + totalUsers + " users in total.");
    }
}


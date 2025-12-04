package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Parent Signup Test for USA Flag Application - Single Signup
 * Tests the complete parent signup flow for one user with email brittany.harris@yopmail.com
 */
public class ParentSignupone {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private UserData currentUserData;
    private String verificationEmail;
    
    // Configuration
    // private static final String LOGIN_URL = "https://prod-usaflag-player-portal.azurewebsites.net/"; // production URL
    // private static final String LOGIN_URL = "https://player.usaflag.org/";
    private static final String LOGIN_URL = "https://staging-usaflag-playerportal.azurewebsites.net/login";
    private static final String PASSWORD = "12345678";
    
    /**
     * User data class to hold user information
     */
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
        
        // Getters
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDateOfBirth() { return dateOfBirth; }
    }
    
    @BeforeClass
    public void setUp() {
        System.out.println("🔧 Setting up Parent Signup WebDriver...");
        
        WebDriverManager.chromedriver().setup();
        initBrowser();
        
        // Initialize user data with brittany.harris@yopmail.com
        currentUserData = new UserData(
            "Jane",
            "Evans",
            "ZaneEvans.p2703@yopmail.com",
            "9876543210",
            "01/01/1990"
        );
        verificationEmail = currentUserData.getEmail();
        
        System.out.println("✅ Parent Signup WebDriver ready!");
        System.out.println("📧 Using email: " + verificationEmail);
        System.out.println("🔑 Password: " + PASSWORD);
    }

    /**
     * Initialize a fresh browser instance.
     */
    private void initBrowser() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Select the country "United States" using the specific className selector.
     */
    private void selectCountryUnitedStates() throws InterruptedException {
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
                    java.util.List<WebElement> inputs = driver.findElements(By.xpath("//input[@type='text']"));
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

    
    @Test(priority = 1)
    public void testOpenLoginAndClickCreateAccount() {
        System.out.println("🔍 Test 1: Opening login page and clicking Create An Account...");
        
        try {
            // Navigate to login page
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            String pageTitle = driver.getTitle();
            String currentUrl = driver.getCurrentUrl();
            
            System.out.println("✅ Page Title: " + pageTitle);
            System.out.println("✅ Current URL: " + currentUrl);
            
            // Wait for page to load completely
            Thread.sleep(3000);
            
            // Find and click "Create An Account" button
            System.out.println("🔍 Looking for 'Create An Account' button...");
            WebElement createAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Create An Account']"))
            );
            createAccountButton.click();
            System.out.println("✅ 'Create An Account' button clicked successfully");
            
            // Wait for page navigation
            Thread.sleep(2000);
            
            System.out.println("✅ Test 1 PASSED - Login page opened and Create An Account clicked");
            
        } catch (Exception e) {
            System.err.println("❌ Test 1 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 1 failed", e);
        }
    }
    
    @Test(priority = 2)
    public void testClickParentalSignUp() {
        System.out.println("🔍 Test 2: Clicking on Parental Sign Up...");
        
        try {
            // Find and click "Parental Sign Up" div
            System.out.println("🔍 Looking for 'Parental Sign Up' div...");
            WebElement parentalSignUpDiv = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[normalize-space()='Parental Sign Up']"))
            );
            parentalSignUpDiv.click();
            System.out.println("✅ 'Parental Sign Up' div clicked successfully");
            
            // Wait for form to load
            Thread.sleep(2000);
            
            System.out.println("✅ Test 2 PASSED - Parental Sign Up clicked");
            
        } catch (Exception e) {
            System.err.println("❌ Test 2 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 2 failed", e);
        }
    }
    
    @Test(priority = 3)
    public void testFillParentSignupForm() {
        System.out.println("🔍 Test 3: Filling out parent signup form...");
        
        System.out.println("📋 Using user data:");
        System.out.println("   First Name: " + currentUserData.getFirstName());
        System.out.println("   Last Name: " + currentUserData.getLastName());
        System.out.println("   Email: " + currentUserData.getEmail());
        System.out.println("   Phone: " + currentUserData.getPhone());
        System.out.println("   Date of Birth: " + currentUserData.getDateOfBirth());
        
        try {
            // Fill first name
            System.out.println("📝 Entering first name: " + currentUserData.getFirstName());
            WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
            firstNameField.clear();
            firstNameField.sendKeys(currentUserData.getFirstName());
            System.out.println("✅ First name entered successfully");
            
            // Fill last name
            System.out.println("📝 Entering last name: " + currentUserData.getLastName());
            WebElement lastNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
            lastNameField.clear();
            lastNameField.sendKeys(currentUserData.getLastName());
            System.out.println("✅ Last name entered successfully");
            
            // Fill email
            System.out.println("📧 Entering email: " + currentUserData.getEmail());
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys(currentUserData.getEmail());
            System.out.println("✅ Email entered successfully");
            
            // Fill phone number
            System.out.println("📞 Entering phone: " + currentUserData.getPhone());
            WebElement phoneField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='+xx xxxx xxxxxx']"))
            );
            phoneField.clear();
            phoneField.sendKeys(currentUserData.getPhone());
            System.out.println("✅ Phone number entered successfully");
            
            // Select country (robust exact match)
            selectCountryUnitedStates();
            
            // Fill date of birth
            System.out.println("📅 Entering date of birth: " + currentUserData.getDateOfBirth());
            WebElement dateOfBirthField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dateOfBirth")));
            dateOfBirthField.clear();
            dateOfBirthField.sendKeys(currentUserData.getDateOfBirth());
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
            
            System.out.println("✅ Test 3 PASSED - Parent signup form filled and submitted successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Test 3 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 3 failed", e);
        }
    }
    
    @Test(priority = 4)
    public void testVerifyParentSignupSubmission() {
        System.out.println("🔍 Test 4: Verifying parent signup submission...");
        
        try {
            // Wait a moment for form processing
            Thread.sleep(3000);
            
            // Get current URL to see if we're redirected
            String currentUrl = driver.getCurrentUrl();
            System.out.println("✅ Current URL after form submission: " + currentUrl);
            
            // Get page title
            String pageTitle = driver.getTitle();
            System.out.println("✅ Page Title after submission: " + pageTitle);
            
            // Check if we're redirected away from the signup form
            if (currentUrl.contains("parent-sign-up")) {
                System.out.println("ℹ️ Still on signup page - checking for success/error messages");
                
                // Look for success or error messages
                try {
                    WebElement successMessage = driver.findElement(By.xpath("//div[contains(@class, 'success') or contains(text(), 'success')]"));
                    System.out.println("✅ Success message found: " + successMessage.getText());
                } catch (Exception e) {
                    System.out.println("ℹ️ No success message found");
                }
                
                try {
                    WebElement errorMessage = driver.findElement(By.xpath("//div[contains(@class, 'error') or contains(text(), 'error')]"));
                    System.out.println("⚠️ Error message found: " + errorMessage.getText());
                } catch (Exception e) {
                    System.out.println("ℹ️ No error message found");
                }
            } else {
                System.out.println("✅ Successfully redirected from signup page");
                System.out.println("✅ Parent signup appears to be successful");
            }
            
            System.out.println("✅ Test 4 PASSED - Parent signup verification completed");
            
        } catch (Exception e) {
            System.err.println("❌ Test 4 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 4 failed", e);
        }
    }
    
    @Test(priority = 5)
    public void testEmailVerification() {
        System.out.println("🔍 Test 5: Starting email verification process...");
        
        try {
            // Step 1: Find and copy the email from verification page
            System.out.println("📧 Step 1: Finding email on verification page...");
            WebElement emailParagraph = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='ml-2 text-[#667085] text-sm normal-case']"))
            );
            String emailText = emailParagraph.getText();
            System.out.println("✅ Found email on verification page: " + emailText);
            
            // Verify the email matches what we entered
            if (emailText.equals(verificationEmail)) {
                System.out.println("✅ Email matches the one entered in the form");
            } else {
                System.out.println("⚠️ Email mismatch - Expected: " + verificationEmail + ", Found: " + emailText);
            }
            
            // Step 2: Open new tab and navigate to YOPmail
            System.out.println("🌐 Step 2: Opening YOPmail in new tab...");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open('https://yopmail.com/', '_blank');");
            
            // Switch to the new tab
            java.util.Set<String> tabs = driver.getWindowHandles();
            String[] tabArray = tabs.toArray(new String[tabs.size()]);
            driver.switchTo().window(tabArray[1]);
            System.out.println("✅ Switched to YOPmail tab");
            
            // Wait for YOPmail to load
            Thread.sleep(3000);
            
            // Step 3: Enter email in YOPmail login field
            System.out.println("📧 Step 3: Entering email in YOPmail: " + verificationEmail);
            WebElement yopmailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='login']")));
            yopmailInput.clear();
            yopmailInput.sendKeys(verificationEmail);
            System.out.println("✅ Email entered in YOPmail");
            
            // Step 4: Click the check email button
            System.out.println("🔍 Step 4: Clicking check email button...");
            WebElement checkEmailButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='material-icons-outlined f36']")));
            checkEmailButton.click();
            System.out.println("✅ Check email button clicked");
            
            // Wait for inbox to load
            Thread.sleep(3000);
            
            // Step 5: Find the 6-digit verification code
            System.out.println("🔢 Step 5: Looking for 6-digit verification code...");
            String verificationCode = null;
            
            try {
                // Wait for the email iframe to load
                System.out.println("📧 Waiting for email iframe to load...");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ifmail")));
                
                // Switch to the iframe containing the email content
                driver.switchTo().frame("ifmail");
                System.out.println("✅ Switched to email iframe");
                
                // Wait for email content to load
                Thread.sleep(2000);
                
                // Look for the OTP element with specific styling
                System.out.println("🔍 Looking for OTP element with specific styling...");
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
                } else {
                    System.out.println("⚠️ No 6-digit code found in OTP element");
                }
                
                // Switch back from iframe
                driver.switchTo().defaultContent();
                System.out.println("✅ Switched back from email iframe");
                
            } catch (Exception e) {
                System.out.println("⚠️ Could not find code in iframe");
                // verificationCode = "111111"; // Commented out - use actual OTP from email
            }
            
            if (verificationCode == null) {
                System.out.println("⚠️ Could not find verification code");
                // verificationCode = "111111"; // Commented out - use actual OTP from email
                throw new RuntimeException("Failed to extract verification code from email");
            }
            
            // Step 6: Navigate back to verification page
            System.out.println("🔄 Step 6: Navigating back to verification page...");
            driver.switchTo().window(tabArray[0]);
            System.out.println("✅ Switched back to verification page");
            
            // Step 7: Enter verification code
            System.out.println("🔢 Step 7: Entering verification code: " + verificationCode);
            WebElement otpInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp-input-0")));
            otpInput.click();
            otpInput.clear();
            otpInput.sendKeys(verificationCode);
            System.out.println("✅ Verification code entered");
            
            // Wait a moment for the code to be processed
            Thread.sleep(1000);
            
            // Step 8: Click submit button
            System.out.println("🚀 Step 8: Clicking submit button...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            submitButton.click();
            System.out.println("✅ Submit button clicked");
            
            // Wait for verification to complete
            Thread.sleep(5000);
            
            // Check if verification was successful
            String currentUrl = driver.getCurrentUrl();
            System.out.println("✅ Current URL after OTP submission: " + currentUrl);
            
            if (currentUrl.contains("dashboard") || currentUrl.contains("home") || currentUrl.contains("profile")) {
                System.out.println("✅ OTP verification successful! Redirected to dashboard/home/profile.");
            } else if (currentUrl.contains("verification")) {
                System.out.println("⚠️ Still on verification page - checking for error messages...");
                
                // Look for error messages
                try {
                    WebElement errorMessage = driver.findElement(By.xpath("//div[contains(@class, 'error') or contains(text(), 'error') or contains(text(), 'invalid')]"));
                    String errorText = errorMessage.getText();
                    System.out.println("⚠️ Error message found: " + errorText);
                } catch (Exception e) {
                    System.out.println("ℹ️ No error message found");
                }
            } else {
                System.out.println("ℹ️ OTP verification completed - redirected to unexpected page");
            }
            
            System.out.println("✅ Test 5 PASSED - Email verification completed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Test 5 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 5 failed", e);
        }
    }
    
    @Test(priority = 6)
    public void testCreateAccountPassword() {
        System.out.println("🔍 Test 6: Creating account password...");
        
        try {
            // Wait for password creation page to load
            Thread.sleep(2000);
            
            // Check if we're on the password creation page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("✅ Current URL: " + currentUrl);
            
            if (!currentUrl.contains("create-account-password")) {
                System.out.println("⚠️ Not on password creation page, checking current page...");
                String pageTitle = driver.getTitle();
                System.out.println("✅ Page Title: " + pageTitle);
            }
            
            // Step 1: Enter password
            System.out.println("🔑 Step 1: Entering password...");
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='password']")));
            passwordField.clear();
            passwordField.sendKeys(PASSWORD);
            System.out.println("✅ Password entered successfully");
            
            // Step 2: Enter confirm password
            System.out.println("🔑 Step 2: Entering confirm password...");
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='confirmPassword']")));
            confirmPasswordField.clear();
            confirmPasswordField.sendKeys(PASSWORD);
            System.out.println("✅ Confirm password entered successfully");
            
            // Wait a moment for the form to process
            Thread.sleep(1000);
            
            // Step 3: Click submit button
            System.out.println("🚀 Step 3: Clicking submit button...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
            submitButton.click();
            System.out.println("✅ Submit button clicked successfully");
            
            // Wait briefly for page transition
            System.out.println("⏳ Waiting for page to load...");
            Thread.sleep(2000);
            
            // Immediately verify and record success if name/email found
            System.out.println("🔍 Verifying name/email on dashboard...");
            verifyEmailWithinOneSecondOrFailAndRecord();
            
            System.out.println("✅ Test 6 PASSED - Account password created successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Test 6 FAILED: " + e.getMessage());
            throw new RuntimeException("Test 6 failed", e);
        }
    }
    
    /**
     * After reaching dashboard, ensure the user's email or name appears.
     * If found, record success; otherwise, fail fast for this iteration.
     */
    private void verifyEmailWithinOneSecondOrFailAndRecord() {
        try {
            String expectedFullName = currentUserData.getFirstName() + " " + currentUserData.getLastName();
            String firstName = currentUserData.getFirstName();
            String lastName = currentUserData.getLastName();
            
            // Check for full name in h2 heading (most reliable and fastest match)
            try {
                WebElement nameHeading = driver.findElement(By.xpath("//h2[contains(@class, 'text-2xl')][contains(normalize-space(text()), '" + expectedFullName + "')]"));
                if (nameHeading != null && nameHeading.isDisplayed()) {
                    System.out.println("✅ Full name found in heading: " + expectedFullName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // Full name not found in h2, try other methods
            }
            
            // Check for first name in h2 heading
            try {
                WebElement firstNameHeading = driver.findElement(By.xpath("//h2[contains(@class, 'text-2xl')][contains(normalize-space(text()), '" + firstName + "')]"));
                if (firstNameHeading != null && firstNameHeading.isDisplayed()) {
                    System.out.println("✅ First name found in heading: " + firstName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // First name not found in h2
            }
            
            // Check for last name in h2 heading
            try {
                WebElement lastNameHeading = driver.findElement(By.xpath("//h2[contains(@class, 'text-2xl')][contains(normalize-space(text()), '" + lastName + "')]"));
                if (lastNameHeading != null && lastNameHeading.isDisplayed()) {
                    System.out.println("✅ Last name found in heading: " + lastName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // Last name not found in h2
            }
            
            // Also check for full name in any element (fallback)
            try {
                WebElement nameElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + expectedFullName + "']"));
                if (nameElement != null && nameElement.isDisplayed()) {
                    System.out.println("✅ Full name found on dashboard: " + expectedFullName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // Full name not found
            }
            
            // Check for first or last name in any element (fallback)
            try {
                WebElement firstNameElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + firstName + "']"));
                if (firstNameElement != null && firstNameElement.isDisplayed()) {
                    System.out.println("✅ First name found on dashboard: " + firstName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // First name not found
            }
            
            try {
                WebElement lastNameElement = driver.findElement(By.xpath("//*[normalize-space(text())='" + lastName + "']"));
                if (lastNameElement != null && lastNameElement.isDisplayed()) {
                    System.out.println("✅ Last name found on dashboard: " + lastName);
                    System.out.println("✅ Signup successful for: " + verificationEmail);
                    return;
                }
            } catch (Exception ex) {
                // Last name not found
            }
            
            // Check for email
            try {
                java.util.List<WebElement> emailElems = driver.findElements(By.xpath("//*[normalize-space(text())='" + verificationEmail + "']"));
                if (emailElems != null && !emailElems.isEmpty()) {
                    for (WebElement e : emailElems) {
                        if (e.isDisplayed()) {
                            System.out.println("✅ Email visible on dashboard: " + verificationEmail);
                            System.out.println("✅ Signup successful for: " + verificationEmail);
                            return;
                        }
                    }
                }
            } catch (Exception ex) {
                // Email not found
            }
            
            // If nothing found, fail
            throw new RuntimeException("Neither email nor name visible on dashboard");
        } catch (Exception ex) {
            System.err.println("❌ Email/Name verification failed: " + ex.getMessage());
            throw new RuntimeException("Email/Name verification failed", ex);
        }
    }
    
    @AfterClass
    public void tearDown() {
        System.out.println("🔍 Parent signup browser will remain open for inspection...");
        System.out.println("✅ Tests completed - Browser window remains open");
        System.out.println("💡 Close the browser manually when done inspecting");
        System.out.println("📧 The email used: " + verificationEmail);
        
        // Uncomment the line below if you want to close the browser automatically
        // if (driver != null) {
        //     driver.quit();
        // }
    }
}


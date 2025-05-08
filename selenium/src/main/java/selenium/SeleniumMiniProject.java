package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import java.io.FileInputStream;
import java.util.Properties;

public class SeleniumMiniProject {
	
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait wait;
	private FileInputStream fis;
	private ExcelUtils data;

	
	public void initializations() throws Exception {
		Properties properties = new Properties();
		fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties");
		properties.load(fis);
		String browser = properties.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			System.out.println("Initializing the driver. Please wait...");
			// Create ChromeOptions instance to customize browser settings
	        ChromeOptions options = new ChromeOptions();
	        // Disable the "Chrome is being controlled by automated software" message
	        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
			// Create a new WebDriver instance
	        driver = new ChromeDriver(options);
		} else if (browser.equalsIgnoreCase("edge")) {
			System.out.println("Initializing the driver. Please wait...");
			// Create EdgeOptions instance to customize browser settings
	        EdgeOptions options = new EdgeOptions();
	        // Disable the "Chrome is being controlled by automated software" message
	        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
			// Create a new WebDriver instance
	        driver = new EdgeDriver(options);
		} else {
			System.out.println("Sorry, "+browser+" browser is not supportive yet or its an invalid browser");
			System.exit(0);
		}
        // Maximize the window
        driver.manage().window().maximize();
        // Open the target website
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        // Create JS Executor
        js = (JavascriptExecutor) driver;
        // Set ExplicitWait for 50 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        data = new ExcelUtils(System.getProperty("user.dir")+"\\src\\main\\resources\\EmployeeDetails.xlsx");
	}
	
	public void signIn() {
		// Wait until username field is detected
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        // Pass 'Admin' in the username field
        username.sendKeys("Admin");
        System.out.println("Username was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // Find password field and pass 'admin123' in the field
        driver.findElement(By.name("password")).sendKeys("admin123");
        System.out.println("Password was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // Click on the 'Login' button
        driver.findElement(By.tagName("button")).click();
        System.out.println("Login button was clicked");
	}
	
	public void navigateToAddEmp() {
		// Click on PIM on the left sidebar
		WebElement pim = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("PIM")));
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        pim.click();
        System.out.println("Chose 'PIM' in the Menu");
        // Click on 'Add Employee' button
        WebElement addEmployee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add Employee")));
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        addEmployee.click();
        System.out.println("'Add Employee' section was opened");
	}
	
	public void saveDetails() {
		// Wait until 'First Name' is visible and pass the value
		WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));
		js.executeAsyncScript("setTimeout(arguments[0], 500);");
        fname.sendKeys(data.getCellData(1, 0));
        System.out.println("first name was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Middle Name' and pass the value
        driver.findElement(By.name("middleName")).sendKeys(data.getCellData(1, 1));
        System.out.println("middle name was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Last Name' and pass the value
        driver.findElement(By.name("lastName")).sendKeys(data.getCellData(1, 2));
        System.out.println("last name was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Create Login Details' toggle and enable it
        driver.findElement(By.className("oxd-switch-input")).click();
        System.out.println("'Create Login Details' toggle was enabled");
        
        // Wait for the visibility of 'UserName' field and pass the value
        WebElement uname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//*[contains(text(),'Username')]/following::input)[1]")));
        js.executeScript("window.scrollBy(0, 200);");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        uname.sendKeys(data.getCellData(1, 3));
        System.out.println("username was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Password' and pass the value
        driver.findElement(By.xpath("(//*[contains(text(),'Password')]/following::input)[1]")).sendKeys(data.getCellData(1, 4));
        System.out.println("password was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Re-enter Password' and pass the value
        driver.findElement(By.xpath("(//*[contains(text(),'Confirm Password')]/following::input)[1]")).sendKeys(data.getCellData(1, 4));
        System.out.println("confirm password was passed");
        js.executeAsyncScript("setTimeout(arguments[0], 500);");
        // locate 'Save' button and click it
        driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();
        js.executeAsyncScript("setTimeout(arguments[0], 3000);");
        // handling 'Employee Id already exists' issue
        try {
	        if (driver.findElement(By.xpath("//*[text()='Employee Id already exists']")).isDisplayed()) {
	        	driver.findElement(By.xpath(("(//*[text()='Employee Id']/following::input)[1]"))).sendKeys("123");
	        	js.executeAsyncScript("setTimeout(arguments[0], 500);");
	        	driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();
	        }
        } catch (Exception e) {}
        System.out.println("employee details were saved");
	}
	
	public void editDetails() {
		// Wait until nationality dropdown comes to visibility and click it
        WebElement nationality = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//label[text()='Nationality']/following::i)[1]")));
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        js.executeScript("window.scrollBy(0, 250);");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        nationality.click();
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // choose 'Indian' option for nationality dropdown
        driver.findElement(By.xpath("//*[text()='"+data.getCellData(1, 5)+"']")).click();
        System.out.println("Nationality was selected");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // locate 'Martial Status' dropdown and click it
        driver.findElement(By.xpath("(//label[text()='Marital Status']/following::i)[1]")).click();
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // choose 'Single' option for Martial Status dropdown
        driver.findElement(By.xpath("//*[text()='"+data.getCellData(1, 6)+"']")).click();
        System.out.println("Marital status was selected");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // Locate 'Male' radio button for Gender and click it
        driver.findElement(By.xpath("//*[text()='"+data.getCellData(1, 7)+"']")).click();
        System.out.println("Gender was selected");
        js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        // locate 'Save' button and click it
        driver.findElement(By.xpath("(//*[text()='Gender']/following::button)[1]")).click();
        System.out.println("Edited details were saved");
        
        // Wait until successful popup vanishes
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div")));
        wait.until(ExpectedConditions.invisibilityOf(success));
	}
	
	public void logOut() {
		// locate profile dropdown and click it
		js.executeAsyncScript("setTimeout(arguments[0], 1000);");
        driver.findElement(By.className("oxd-userdropdown-img")).click();
        js.executeAsyncScript("setTimeout(arguments[0], 2000);");
        // locate 'Logout' option and click it
        driver.findElement(By.linkText("Logout")).click();
        System.out.println("Logged out from the account");
        js.executeAsyncScript("setTimeout(arguments[0], 2000);");
	}
	
	public void closeResources() throws Exception {
		fis.close();
		driver.quit();
	}
	
    public static void main(String[] args) throws Exception {
    	// Create an instance of SeleniumMiniProject Class
    	SeleniumMiniProject project = new SeleniumMiniProject();
    	// Initialize the driver, JS Executor, WebDriverWait
    	project.initializations();
        // Login to OrangeHRM
    	project.signIn();
        // Navigate to 'Add Employee'
    	project.navigateToAddEmp();
    	// Save Details of an Employee
        project.saveDetails();
        // Edit Details of the Employee
        project.editDetails();
        // Logout from the site
        project.logOut();
        // Close the used resources
        project.closeResources();
    }
}

import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import javax.xml.ws.Action;


public class TestExample {
    static WebDriver driver;

    @Before //metoda
        public void init(){
        System.out.println("test");
        String systemName = System.getProperty("os.name").toLowerCase();
        if (systemName.contains("windows")) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\bin\\chromedriver.exe");
        } else if (systemName.contains("linux")) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/bin/chromedriver_linux");
        } else {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/bin/chromedriver_osx");
        }

        driver = new ChromeDriver();
        driver.get("https://opensource-demo.orangehrmlive.com");
        String pageTitle = driver.getTitle();
        System.out.println(pageTitle);
        driver.manage().window().maximize();

    }

    @Test
    public void ActionTest() {
        LoginWithCorrectCredentials1();
        WebElement pimButton = driver.findElement(By.id("menu_pim_viewPimModule"));
        WebElement addemployeeButton = driver.findElement(By.id("menu_pim_addEmployee"));

        Actions navigateAction = new Actions(driver);
        navigateAction.moveToElement(pimButton)
                .moveToElement(addemployeeButton)
                .click()
                .perform();
    }

    @Test
    public void AddDisabledEmployee() {
        Random rand = new Random();
        int IDnumber = rand.nextInt(9999);
        String id = Integer.toString(IDnumber);

        LoginWithCorrectCredentials1();
        WebElement pimButton = driver.findElement(By.id("menu_pim_viewPimModule"));
        WebElement addemployeeButton = driver.findElement(By.id("menu_pim_addEmployee"));

        Actions navigateAction = new Actions(driver);
        navigateAction.moveToElement(pimButton)
                .moveToElement(addemployeeButton)
                .click()
                .perform();

        driver.findElement(By.id("chkLogin")).click();
        driver.findElement(By.id("firstName")).sendKeys("Karolina"+id);
        driver.findElement(By.id("middleName")).sendKeys("Barbara"+id);
        driver.findElement(By.id("lastName")).sendKeys("Kowalska"+id);
        driver.findElement(By.id("user_name")).sendKeys("Karolina123"+id);
        driver.findElement(By.id("user_password")).sendKeys("Karolina_123"+id);
        driver.findElement(By.id("re_password")).sendKeys("Karolina_123"+id);

        Select userStatus = new Select(driver.findElement(By.id("status")));
        userStatus.selectByIndex(1);
        //userStatus.selectByVisibleText("Disabled"); - druga opcja do dropdown list

        WebElement idField = driver.findElement(By.id("employeeId"));
        idField.clear();



        //idField.sendKeys("1234");

        driver.findElement(By.id("btnSave")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement addUserCheck = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Personal Details")));

        //WebElement addUserCheck = driver.findElement(By.linkText("Personal Details"));
        String result3 = addUserCheck.getText();
        System.out.println(result3);
        Assertions.assertEquals("Personal Details",result3);

    }

    @Test
    public void LoginWithIncorrectCredentials1() {
        driver.findElement(By.id("txtUsername")).sendKeys("Admin");
        //Thread.sleep(2000);
        WebElement passwordInput = driver.findElement(By.name("txtPassword"));
        passwordInput.sendKeys("x1x1x1");
        //Thread.sleep(2000);
        driver.findElement(By.id("btnLogin")).click();
       //Thread.sleep(2000);
        WebElement errormessagespan = driver.findElement(By.id("spanMessage"));
        String result1 = errormessagespan.getText();
        System.out.println(result1);
        Assertions.assertEquals("Invalid credentials",result1);

    }
    @Test
    public void LoginWithIncorrectCredentials2(){
        driver.findElement(By.id("txtUsername")).sendKeys("");
        // Thread.sleep(2000);
        WebElement passwordInput = driver.findElement(By.name("txtPassword"));
        passwordInput.sendKeys("admin123");
        //Thread.sleep(2000);
        driver.findElement(By.id("btnLogin")).click();
        //Thread.sleep(2000);

        //przypadek_negatywny
        WebElement element2 = driver.findElement(By.id("spanMessage"));
        String result2 = element2.getText();
        System.out.println(result2);
        Assertions.assertEquals("Username cannot be empty",result2);

    }

    @Test
    public void LoginWithCorrectCredentials1(){
        driver.findElement(By.id("txtUsername")).sendKeys("Admin");
        //Thread.sleep(1000);
        WebElement passwordInput = driver.findElement(By.name("txtPassword"));
        passwordInput.clear();
        passwordInput.sendKeys("admin123");
        //Thread.sleep(1000);
        driver.findElement(By.id("btnLogin")).click();
        //Thread.sleep(1000);

        WebElement noerror = driver.findElement(By.id("welcome"));
        String welcome = noerror.getText();
        System.out.println(welcome);
        Assertions.assertEquals("Welcome Admin",welcome);
    }
    @Test
    public void LoginWithCorrectCredentials2(){
        driver.findElement(By.id("txtUsername")).sendKeys("Admin");
        //Thread.sleep(1000);
        WebElement passwordInput = driver.findElement(By.name("txtPassword"));
        passwordInput.clear();
        passwordInput.sendKeys("admin123");
        //Thread.sleep(1000);
        driver.findElement(By.id("btnLogin")).click();
        //Thread.sleep(1000);

        WebElement noerror = driver.findElement(By.id("welcome"));
        String welcome = noerror.getText();
        System.out.println(welcome);
        Assertions.assertEquals("Welcome Admin",welcome);

        driver.findElement(By.className("firstLevelMenu")).click();

        //String welcomeText = welcomeLink.getText();
        //Assertions.assertTrue(welcomeText.contains("Admin"));
        driver.findElement(By.id("menu_admin_viewAdminModule")).click();
        driver.findElement(By.id("searchSystemUser_userName")).sendKeys("Admin");
        driver.findElement(By.id("searchBtn")).click();
        WebElement searchResult = driver.findElement(By.xpath("//table[@id='resultTable']//a[contains(.,'Admin')]"));
        searchResult.click();
    }

    @Test
    //logout
            public void LogoutTest () {
        LoginWithCorrectCredentials1();
        WebElement welcomeText = driver.findElement(By.id("welcome"));
        welcomeText.click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/index.php/auth/logout']")));
        logoutLink.click();

        WebElement usernameInput = driver.findElement(By.id("txtUsername"));
        //Asert
        Assert.assertTrue(usernameInput.isDisplayed());
    }
    @Test
    public void alertTest() throws InterruptedException {
        driver.get("https://www.seleniumeasy.com/test/javascript-alert-box-demo.html");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@onclick=\"myConfirmFunction()\"]")));
        driver.findElement(By.xpath("//button[@onclick=\"myConfirmFunction()\"]")).click();
        Thread.sleep(3000);
        driver.switchTo().alert().accept();
        Thread.sleep(3000);
        driver.switchTo().defaultContent();


    }

    @After

    public void closeDriver (){
        driver.close();
    }


    }





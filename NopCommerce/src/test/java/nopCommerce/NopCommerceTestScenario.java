package nopCommerce;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class NopCommerceTestScenario
{
	static WebDriver driver;
	public static void main(String[] args)
	{
		System.setProperty("webdriver.chrome.driver", "E:\\SQA\\Practical\\Selnium\\WebDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		
		////////////////////////////////////////////////////////////////////////////////
		
		String email = "admin@yourstore.com";
		String password = "admin";
		
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		
		////////// Login Page //////////
		String baseUrl = "https://admin-demo.nopcommerce.com/login?ReturnUrl=%2Fadmin%2F";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		
		WebElement emailInput = driver.findElement(By.id("Email"));
		emailInput.clear();
		emailInput.sendKeys(email);
		
		WebElement passwordInput = driver.findElement(By.id("Password"));
		passwordInput.clear();
		passwordInput.sendKeys(password);
		
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		
		////////////////////////////////////////////////////////////////////////////////
		
		////////// Dashboard Page //////////
		WebElement body = driver.findElement(By.tagName("body"));		
		if(body.getAttribute("class").contains("sidebar-collapse"))
		{
			WebElement sideBarPusher = driver.findElement(By.id("nopSideBarPusher"));
			sideBarPusher.click();
		}
		
		// Expanding Catalog NavItem
		WebElement catalogNavItem = driver.findElements(By.cssSelector("aside nav > ul > li")).get(1); 
		catalogNavItem.click();
		// Navigating to products
		catalogNavItem.findElement(By.linkText("Products")).click();
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Products Page //////////
		WebElement addNewBtn = driver.findElement(By.cssSelector("div.content-header a[href='/Admin/Product/Create']"));		
		addNewBtn.click();
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Add a new product Page //////////
		body = driver.findElement(By.tagName("body"));
		if(body.getAttribute("class").contains("advanced-settings-mode"))
		{
			driver.findElement(By.className("onoffswitch")).click();
		}
		
		//////////
		
		expandCard("product-info");
		
		
		expandCard("product-price");
		
		
		expandCard("product-inventory");
		
		

		
		
	}
	
	static void expandCard(String id)
	{
//		WebElement nopCard = driver.findElement(By.cssSelector("div[data-card-name='product-info']"));
		WebElement nopCard = driver.findElement(By.id(id));
		
		if(nopCard.getAttribute("class").contains("collapsed-card"))
		{
			System.out.println("XXXXXXXXXXXXXXXXXX");
//			nopCard.click();
			nopCard.findElement(By.className("card-header")).click();
		}
		else
		{
			System.out.println("OOOOOOOOOOOOOOO");
			nopCard.findElement(By.className("card-header")).click();
		}
	}
}
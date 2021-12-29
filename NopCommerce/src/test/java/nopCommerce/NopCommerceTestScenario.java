package nopCommerce;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class NopCommerceTestScenario
{
	static WebDriver driver;
	public static void main(String[] args) throws Exception
	{
		System.setProperty("webdriver.chrome.driver", "E:\\SQA\\Practical\\Selnium\\WebDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		
		////////////////////////////////////////////////////////////////////////////////
		
		String email = "admin@yourstore.com";
		String password = "admin";
		
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
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
		//// Assert url
		Assert.assertTrue(driver.getCurrentUrl().contains("admin"));
		
		expandSideBar();
		//// Assert sidebar expanded 
		Assert.assertFalse(driver.findElement(By.tagName("body")).getAttribute("class").contains("sidebar-collapse"));
		
		
		// Expanding Catalog NavItem
		WebElement catalogNavItem = driver.findElements(By.cssSelector("aside nav > ul > li")).get(1); 
		catalogNavItem.click();
		
		// Navigating to products page
//		catalogNavItem.findElement(By.linkText("Products")).click();
		wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(catalogNavItem, By.linkText("Products"))).click();
		        
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Products Page //////////
		//// Assert url
		Assert.assertTrue(driver.getCurrentUrl().contains("Product/List"));
		Assert.assertEquals("Products", driver.findElement(By.cssSelector("form h1")).getText());
		
		WebElement addNewBtn = driver.findElement(By.cssSelector("div.content-header a[href='/Admin/Product/Create']"));		
		addNewBtn.click();
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Add a new product Page //////////
		WebElement body = driver.findElement(By.tagName("body"));
		if(body.getAttribute("class").contains("advanced-settings-mode"))
		{
			driver.findElement(By.className("onoffswitch")).click();
		}
		//// Assert settings mode is basic
		Assert.assertFalse(body.getAttribute("class").contains("advanced-settings-mode"));
		
		
		// Filling Product info
		Thread.sleep(3000);
		expandCard("product-info");
		//// Assert card expanded
		Assert.assertFalse(driver.findElement(By.id("product-info")).getAttribute("class").contains("collapsed-card"));
		
		WebElement nameInput = driver.findElement(By.id("Name"));
		nameInput.sendKeys("Joop Perfume");
		//// Assert the name is right
		String currentName = nameInput.getAttribute("value");
		Assert.assertEquals("Joop Perfume", currentName);
		
		
		WebElement shortDescriptionTA = driver.findElement(By.id("ShortDescription"));
		shortDescriptionTA.sendKeys("Nice perfume..");
		
		driver.switchTo().frame(driver.findElement(By.id("FullDescription_ifr")));
		WebElement fullDescription = driver.findElement(By.cssSelector("#tinymce > p"));
		fullDescription.sendKeys("Very Nice perfume..");
		//// Assert the name is right
		Assert.assertEquals("Very Nice perfume..", fullDescription.getText());
		driver.switchTo().parentFrame();
		
		WebElement skuInput = driver.findElement(By.id("Sku"));
		skuInput.sendKeys("Scannable bar code" + Instant.now().toEpochMilli());
		
		WebElement categoriesDiv = driver.findElements(By.cssSelector("#product-info div.k-multiselect")).get(0);  
		categoriesDiv.click();
//		WebElement categoryItem = driver.findElement(By.cssSelector("li[data-offset-index='11']"));		
		WebElement categoryItem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-offset-index='11']")));
		categoryItem.click();
		//// Assert categories has elments
		List<WebElement> addedCategories = driver.findElements(By.cssSelector("ul#SelectedCategoryIds_taglist > li"));
		Assert.assertTrue(addedCategories.size() > 0);
		Assert.assertEquals("Apparel >> Accessories", addedCategories.get(0).getText());
		
		
		// Filling Prices
		expandCard("product-price");
		WebElement productPriceSection = driver.findElement(By.id("product-price"));
		
		WebElement priceInput = productPriceSection.findElement(By.cssSelector("input[role='spinbutton']:first-child"));
		priceInput.sendKeys("5000");
		
		WebElement taxExemptInput = productPriceSection.findElement(By.id("IsTaxExempt"));
		taxExemptInput.click();
		
		Select dropdown;
//		WebElement taxCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("TaxCategoryId")));
//		Select dropdown = new Select(taxCategoryDropdown);
//		dropdown.selectByValue("5");
		//// Assert selected tax category is right 
//		Assert.assertEquals("Apparel", dropdown.getAllSelectedOptions().get(0).getText());
		
				
		// Filling Inventory
		expandCard("product-inventory");
		WebElement inventoryMethodDropdown = driver.findElement(By.id("ManageInventoryMethodId"));
		dropdown = new Select(inventoryMethodDropdown);
		dropdown.selectByValue("2");
		
		/*WebElement saveContinueBtn = */driver.findElement(By.name("save-continue")).click();
		//// Assert alert is displayed 
		Assert.assertNotNull(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success"))));
		
		// Navigating to Discounts page  
		expandSideBar();
		// Expanding Catalog NavItem
		WebElement promotionsNavItem = driver.findElement(By.linkText("Promotions")); 
		promotionsNavItem.click();
		// Navigating to Discounts page 
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Discounts"))).click();
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Discounts Page //////////
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add new"))).click();
//		WebElement promotionsNavItem = driver.findElement(By.linkText("Add new"));
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Add a new discount Page //////////
		nameInput = driver.findElement(By.id("Name"));
		nameInput.sendKeys("Vacation");
		
		WebElement typeDropdown = driver.findElement(By.id("DiscountTypeId"));
		dropdown = new Select(typeDropdown);
		dropdown.selectByValue("2");
		
		WebElement usePercentageChbox = driver.findElement(By.id("UsePercentage"));
		usePercentageChbox.click();
		
		WebElement discountAmountInput = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("div#pnlMaximumDiscountAmount input[role='spinbutton']")));
		discountAmountInput.sendKeys("10");
		
		WebElement startDateInput = driver.findElement(By.id("StartDateUtc"));
		startDateInput.sendKeys("12/31/2021 00:00 AM");
		
		WebElement endDateInput = driver.findElement(By.id("EndDateUtc"));
		endDateInput.sendKeys("02/28/2022 00:00 AM");
		
		
		WebElement saveContinueBtn = driver.findElement(By.name("save-continue"));
		saveContinueBtn.click();

		expandCard("discount-applied-to-products");		
		WebElement addNewProductBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddNewProduct")));
		addNewProductBtn.click();
		
		////////////////////////////////////////////////////////////////////////////////
				
		////////// Add a new product window //////////
		Thread.sleep(3000);
				
		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = null;
		Set<String> handles = driver.getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()){
		    subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // switch to popup window

		// Now you are in the popup window, perform necessary actions here
		WebElement productNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SearchProductName")));
		productNameInput.sendKeys("Joop Perfume");
		
		WebElement productCategoryDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SearchCategoryId")));
		dropdown = new Select(productCategoryDropdown);
		dropdown.selectByValue("12");
		
		WebElement searchBtn = driver.findElement(By.id("search-products"));
		searchBtn.click();
		
		Thread.sleep(3000);
		WebElement firstRowCell = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//table[@id='products-grid']/tbody/tr[1]/td[1]")));
		
		WebElement firstRowCellChbox = firstRowCell.findElement(By.tagName("input"));
		firstRowCellChbox.click();
		
		WebElement saveBtn = driver.findElement(By.name("save"));
		saveBtn.click();
		
//		driver.close();
		
		// switch back to parent window
//		driver.switchTo().window(parentWindowHandler);
	}
	
	static void expandSideBar()
	{
		WebElement body = driver.findElement(By.tagName("body"));
		if(body.getAttribute("class").contains("sidebar-collapse"))
		{
			WebElement sideBarPusher = driver.findElement(By.id("nopSideBarPusher"));
			sideBarPusher.click();
		}
	}
	
	static void expandCard(String id)
	{
//		WebElement nopCard = driver.findElement(By.cssSelector("div[data-card-name='product-info']"));
		WebElement nopCard = driver.findElement(By.id(id));
		
		if(nopCard.getAttribute("class").contains("collapsed-card"))
		{
			nopCard.findElement(By.className("card-header")).click();
		}
	}
}
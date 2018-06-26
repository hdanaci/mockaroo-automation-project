package mockaroo_automation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {

	WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();

		// -------------------step-2-------------------------------
		driver.get("https://mockaroo.com");

	}

	// @Ignore
	@Test(priority = 1)
	public void verifyTitle() {
		// -------------------step-3-------------------------------
		String actualTitle = driver.getTitle();
		String expectedTitle = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		assertEquals(actualTitle, expectedTitle);

		// -------------------step-4-------------------------------
		String actualText1 = driver.findElement(By.xpath("//div[@class='navbar navbar-fixed-top']/div/div/a/div[1]"))
				.getText();
		String expectedText1 = "mockaroo";
		assertEquals(actualText1, expectedText1);

		String actualText2 = driver.findElement(By.xpath("//div[@class='navbar navbar-fixed-top']/div/div/a/div[2]"))
				.getText();
		String expectedText2 = "realistic data generator";
		assertEquals(actualText2, expectedText2);

	}

	// @Ignore
	@Test(priority = 2)
	public void generateData() throws InterruptedException {
		// -------------------step-5-------------------------------
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[1]/div[5]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[2]/div[5]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[3]/div[5]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[4]/div[5]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[5]/div[5]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[6]/div[5]/a")).click();

	}

	// @Ignore
	@Test(priority = 3)
	public void verifyTagsName() {

		// -------------------step-6-------------------------------
		String actualField = driver.findElement(By.xpath("//div[@class='column column-header column-name']")).getText();
		String expectedField = "Field Name";
		assertEquals(actualField, expectedField);

		String actualType = driver.findElement(By.xpath("//div[@class='column column-header column-type']")).getText();
		String expectedType = "Type";
		assertEquals(actualType, expectedType);

		String actualOptions = driver.findElement(By.xpath("//div[@class='column column-header column-options']"))
				.getText();
		String expectedOptions = "Options";
		assertEquals(actualOptions, expectedOptions);

		// -------------------step-7-------------------------------
		assertTrue(driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']"))
				.isEnabled());

		// -------------------step-8-------------------------------
		assertEquals(driver.findElement(By.xpath("//input[@id='num_rows']")).getAttribute("value"), "1000");

		// -------------------step-9-------------------------------
		assertEquals(driver.findElement(By.xpath("//select[@id='schema_file_format']/option[1]")).getText(), "CSV");

		// -------------------step-10-------------------------------
		assertEquals(driver.findElement(By.xpath("//select[@name='schema[line_ending]']/option[1]")).getText(),
				"Unix (LF)");

		// -------------------step-11-------------------------------
		assertTrue(driver.findElement(By.xpath("//input[@id='schema_include_header']")).isSelected());

		assertFalse(driver.findElement(By.xpath("//input[@id='schema_bom']")).isSelected());

	}

	// @Ignore
	@Test(priority = 4)
	public void createRows() throws InterruptedException {
		// -------------------step-12-------------------------------
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();

		// -------------------step-13-------------------------------
		driver.findElement(By.xpath("//div[@id='fields']/div[7]/div[2]/input")).sendKeys("City");
		driver.findElement(By.xpath("//div[@class='table-body']/div/div[7]/div[3]/input[3]")).click();
		Thread.sleep(1000);

		assertEquals(driver.findElement(By.xpath("//div[@id='type_dialog']/div/div/div/h3")).getText(),
				"Choose a Type");

		// -------------------step-14-------------------------------
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("city");
		driver.findElement(By.xpath("//div[@id='type_list']/div")).click();

		// -------------------step-15-------------------------------
		driver.findElement(By.xpath("//div[@id='fields']/div[8]/div[2]/input")).sendKeys("Country");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='table-body']/div/div[8]/div[3]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='type_search_field']")).clear();
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("Country");
		driver.findElement(By.xpath("//div[@id='type_list']/div")).click();

		// -------------------step-16-------------------------------
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='download']")).click();
	}

	@Test(priority = 5)
	public void readCsvFile() throws IOException, InterruptedException {
		Thread.sleep(2000);

		// -------------------step-17-------------------------------
		FileReader fr = new FileReader("/Users/bravo/Downloads/MOCK_DATA.csv");
		BufferedReader buffer = new BufferedReader(fr);

		// -------------------step-18-------------------------------
		assertEquals(buffer.readLine(), "City,Country");

		// -------------------step-19,20,21-------------------------------
		List<String> cities = new ArrayList<>();
		List<String> countries = new ArrayList<>();

		String line = "";
		while ((line = buffer.readLine()) != null) {
			cities.add(line.substring(0, line.indexOf(",")));
			countries.add(line.substring(line.indexOf(",") + 1));
		}

		assertEquals(cities.size(), 1000);
		// -------------------step-22-------------------------------
		Collections.sort(cities);

		String shortCity = cities.get(0);
		String largeCity = cities.get(1);
		for (int i = 0; i < cities.size(); i++) {
			if (shortCity.length() > cities.get(i).length()) {
				shortCity = cities.get(i);
			}
			if (largeCity.length() < cities.get(i).length()) {
				largeCity = cities.get(i);
			}
		}
		System.out.println("Shortest City Name : " + shortCity);
		System.out.println("Longest City Name : " + largeCity);

		// -------------------step-23-------------------------------
		Set<String> mentionedCountry = new HashSet<>(countries);
		for (String each : mentionedCountry) {
			System.out.println(each + "- " + Collections.frequency(countries, each));
		}

		// -------------------step-24-------------------------------
		Set<String> citiesSet = new HashSet<>(cities);

		// -------------------step-25-------------------------------
		int uniqueCities = 0;
		for (int i = 0; i < cities.size(); i++) {
			if (i == cities.lastIndexOf(cities.get(i)))
				uniqueCities++;
		}
		assertEquals(uniqueCities, citiesSet.size());

		// -------------------step-26-------------------------------
		Set<String> countrySet = new HashSet<>(countries);

		// -------------------step-27-------------------------------
		int uniqueCounties = 0;
		for (int i = 0; i < countries.size(); i++) {
			if (i == countries.lastIndexOf(countries.get(i)))
				uniqueCounties++;
		}

		assertEquals(uniqueCounties, countrySet.size());

	}

	@AfterClass
	public void driverClosing() {
		driver.quit();
	}

}

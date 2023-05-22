package Mantis.Locators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Locators2 {
	static String tableName;
	static String rowName;
	static String columnName;
	static int columnNumber;
	static String xpath_format;
	static WebDriver driver;
	static List columnNameArrayList;
	static String columnNameValue;
	static int count = 0;
	static int n = 1;
	static List<String> elementsName;

	public static void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
//		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		driver.get("http://localhost/mantis");

		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("administrator");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("admin");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Summary']")).click();

	}

	public static void main(String args[]) throws IOException {

		Locators2.setup();

		// Using buffered reader here to take input and test this code
		//in conformiq, this bufferedreader part is not required

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Table Name: ");
		tableName = br.readLine();
		tableName = StringUtils.strip(tableName);

		System.out.print("Row Name: ");
		rowName = br.readLine();
		rowName = StringUtils.strip(rowName);

		System.out.print("Column Name: ");
		columnName = br.readLine();
		columnName = StringUtils.strip(columnName);

		/*
		 * in conformiq script generation, we'll use excel data provider and we'll use
		 * these same logic below to achieve case insensitivity
		 */
		columnName = columnName.toLowerCase();
		tableName = WordUtils.capitalizeFully(tableName);

		columnNameArrayList = new ArrayList<String>();

		if (tableName.equalsIgnoreCase("Longest open")) {
			tableName = "Longest open";
		}
		// capture names of arrayList
		List<WebElement> elements = driver
				.findElements(By.xpath("//th[contains(text(),'" + tableName + "')]/following-sibling::th"));

		count = elements.size();
		System.out.println("Total no of following columns: " + count);

		if (tableName.equalsIgnoreCase("Time Stats For Resolved Issues (days)")) {
			columnNumber = 1;
		} else {
			elementsName = new ArrayList<String>();

			while (n <= count) {
				WebElement column = driver.findElement(
						By.xpath("//th[contains(text(),'" + tableName + "')]/following-sibling::th[" + n + "]"));
				columnNameValue = column.getText();
				elementsName.add(columnNameValue);
				n++;
			}

			for (int i = 0; i < count; i++) {
				if (columnName.equalsIgnoreCase(elementsName.get(i))) {
					columnNumber = i + 1;
					System.out.println(columnNumber);
				}
			}
		}
		// this is the format for xpath generation. I used ancestor, following sibling
		// relations to locate accurate cells.
		xpath_format = "//th[contains(text(),'" + tableName
				+ "')]/ancestor::thead/following-sibling::tbody/tr/td[contains(.,'" + rowName
				+ "')]/following-sibling::td[" + columnNumber + "]";

		System.out.println(xpath_format);

	}

}

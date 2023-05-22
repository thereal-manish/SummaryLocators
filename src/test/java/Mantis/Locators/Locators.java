package Mantis.Locators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Locators {
	static String tableName;
	static String rowName;
	static String columnName;
	static int columnNumber;
	static String xpath_format;
	static WebDriver driver;

	

	public static void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		driver.get("http://localhost/mantis");

		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("administrator");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("admin");
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Summary']")).click();
		
		
		
		
	}

	public static void main(String args[]) throws IOException {

		Locators.setup();

		// Using buffered reader here to take input and test this code

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

		/*
		 * note : row name is case sensitive since most of the row names are entered by
		 * user [user name(reporter), category name, Project name, Summary text]. for
		 * ex, if we enter project name using both uppercase and lowercase letters, it
		 * is difficult to find pattern and achieve case insensitivity
		 */

		/*
		 * 'Longest open' table name has first letter as lowercase letter in 2nd word
		 * 'open', it differs from other tables, has uppercase letter as first letter of
		 * every word
		 */
		if (tableName.equalsIgnoreCase("Longest open")) {
			tableName = "Longest open";
			// created this custom method containing switch case statements to assign
			// sibling value of locator using column name
			switchCaseForCategoryTables();
			if (columnName.equalsIgnoreCase("Days")) {
				columnName = "Days";
				columnNumber = 1;
			}
		} else if (tableName.equalsIgnoreCase("Reporter Effectiveness")) {
			// created this custom method containing switch case statements to assign
			// sibling value of locator using column name
			switchCaseForCategoryTables();
			if (columnName.equalsIgnoreCase("Total")) {
				columnName = "Total";
				columnNumber = 3;
			}
		} else if (tableName.equalsIgnoreCase("Reporter By Resolution")
				|| tableName.equalsIgnoreCase("Developer by resolution")) {
			/*
			 * created this another custom method containing switch case statements.
			 * because, above two tables have 11 columns and some having same column name as
			 * others but in a different column position
			 */
			switchCaseForResolutionTable();
		}

		else {
			// all other tables works with this method and we can find accurate xpath for
			// particular cells
			switchCaseForCategoryTables();
		}

//		th[contains(text(),'By Project')]/ancestor::thead/following-sibling::tbody/tr/td[contains(text(),'Conformiq Demo')]/following-sibling::td[1]

		// this is the format for xpath generation. I used ancestor, following sibling
		// relations to locate accurate cells.
		xpath_format = "//th[contains(text(),'" + tableName
				+ "')]/ancestor::thead/following-sibling::tbody/tr/td[contains(.,'" + rowName
				+ "')]/following-sibling::td[" + columnNumber + "]";

		System.out.println(xpath_format);

	}

	// this below webelement structure is how we implement in conformiq script
	// generation
	@FindBy(xpath = "//th[contains(text(),'\"+tableName+\"')]/ancestor::thead/following-sibling::tbody/tr/td[contains(text(),'\"+rowName+\"')]/following-sibling::td[\"+columnNumber+\"]")
	WebElement cell;

	// this method assigns the sibling value for the specified column name
	public static void switchCaseForCategoryTables() {
		switch (columnName) {
		case "open":
			columnNumber = 1;
		case "opened":
			columnNumber = 1;
		case "severity":
			columnNumber = 1;
		case "score":
			columnNumber = 1;
		case "":
			columnNumber = 1;
			break;

		case "resolved":
			columnNumber = 2;
		case "false":
			columnNumber = 2;
			break;

		case "closed":
			columnNumber = 3;
		case "balance":
			columnNumber = 3;
			break;

		case "total":
			columnNumber = 4;
			break;

		case "resolved ratio":
			columnNumber = 5;
			break;

		case "ratio":
			columnNumber = 6;
			break;

		}

	}

	// this method assigns the sibling value for the specified column name [for
	// resolutions tables, these has 11 columns]
	public static void switchCaseForResolutionTable() {
		switch (columnName) {
		case "open":
			columnNumber = 1;
			break;

		case "fixed":
			columnNumber = 2;
			break;

		case "reopened":
			columnNumber = 3;
			break;

		case "unable to reproduce":
			columnNumber = 4;
			break;

		case "not fixable":
			columnNumber = 5;
			break;

		case "duplicate":
			columnNumber = 6;
			break;

		case "no change required":
			columnNumber = 7;
			break;

		case "suspended":
			columnNumber = 8;
			break;

		case "won't fix":
			columnNumber = 9;
			break;

		case "total":
			columnName = "Total";
			columnNumber = 10;
			break;

		case "% false":
			columnName = "% False";
			columnNumber = 11;
		case "% fixed":
			columnName = "% Fixed";
			columnNumber = 11;
			break;
		}
	}

}

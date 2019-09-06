package core;

import org.openqa.selenium.*;
import org.openqa.selenium.safari.*;
import java.math.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;

public class Payment {
	static String url = "";
	static String version;
	static WebDriver driver;
	static String v1;
	static String v2;
	static String v3;
	static String v4;
	static String v5;

	public static void main(String[] args) throws InterruptedException, Exception {
		Logger.getLogger("").setLevel(Level.OFF);

		if (!System.getProperty("os.name").contains("Mac")) {
			throw new IllegalArgumentException("Safari is available only on Mac");
		}
	 		
		url = System.getProperty("version");
		if (version == v1) {
			url="http://alex.academy/exe/payment/index.html";
		}else if (version == v2) {
			url="http://alex.academy/exe/payment/index2.html";
		} else if (version == v3) {
			url="http://alex.academy/exe/payment/index3.html";
		} else if (version == v4) {
			url="http://alex.academy/exe/payment/index4.html";
		} else if (version == v5) {
			url="http://alex.academy/exe/payment/indexE.html";
		}
		
		driver = new SafariDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(url);
		final long start = System.currentTimeMillis();

		String string_monthly_payment = driver.findElement(By.id("id_monthly_payment")).getText();
		String regex = "^([\\s]?[\\$]?[\\s]?[0-1]?[\\,]?[0-9]{3}[\\.]?[5-6]{2})$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string_monthly_payment);
		m.find();

		double monthly_payment = Double.parseDouble(m.group(1).replaceAll(",", "").replace("$", "").replace(" ", ""));

		double annual_payment = new BigDecimal(monthly_payment * 12).setScale(2, RoundingMode.HALF_UP).doubleValue();

		DecimalFormat df = new DecimalFormat("0.00");
		String f_annual_payment = df.format(annual_payment);
		driver.findElement(By.id("id_annual_payment")).sendKeys(String.valueOf(f_annual_payment));
		driver.findElement(By.id("id_validate_button")).click();
		String actual_result = driver.findElement(By.id("id_result")).getText();
		final long finish = System.currentTimeMillis();
		System.out.println("String: \t" + m.group(0));
		System.out.println("Annual Payment: " + f_annual_payment);
		System.out.println("Result: \t" + actual_result);
		System.out.println("Response time: \t" + (finish - start) + " milliseconds");
		driver.quit();
	}
}


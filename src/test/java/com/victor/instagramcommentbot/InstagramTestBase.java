package com.victor.instagramcommentbot;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
//import com.victor.instagramcommentbot.page.InstagramHomePage;
import com.victor.instagramcommentbot.page.InstagramLoginPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class InstagramTestBase {
  private static final int RANDOM_RANGE_LOWER_BOUND = 600;
  private static final int RANDOM_RANGE_UPPER_BOUND = 3_500;
  private static final int LONG_WAIT_BASE_VALUE = 11_500;
  protected static final int SHORT_WAIT_BASE_VALUE = 5_500;
  protected static final int SCROLL_WAIT_VALUE = 4_500;
  private final InstagramLoginPage instagramLoginPage = new InstagramLoginPage();
  //  private final InstagramHomePage instagramHomePage = new InstagramHomePage();
  private final Random random = new Random();
  protected final Properties properties = new Properties();

  @BeforeAll
  public static void setUpAll() {
    Configuration.browserSize = "1280x800";
    SelenideLogger.addListener("allure", new AllureSelenide());
  }

  @BeforeEach
  public void setUp() {
    Configuration.browserCapabilities = new EdgeOptions().addArguments("--remote-allow-origins=*");
    open("https://www.instagram.com/");
  }

  protected void instagramSetUp() throws IOException {
    loadProperties();
    login();
    // instagramHomePage.notTurnOnNotifications.click();
  }

  protected void loadProperties() throws IOException {
    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    }
  }

  protected void login() {
    System.out.println("Logging in...");
    instagramLoginPage.declineOptionalCookiesButton.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
    instagramLoginPage.usernameInput.sendKeys(properties.getProperty("instagram.username"));
    randomWait(SHORT_WAIT_BASE_VALUE);
    instagramLoginPage.passwordInput.sendKeys(properties.getProperty("instagram.password"));
    randomWait(SHORT_WAIT_BASE_VALUE);
    instagramLoginPage.loginButton.click();
    System.out.println("Logged in");
    randomWait(LONG_WAIT_BASE_VALUE);
//    instagramLoginPage.notSaveLoginInfo.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  protected void randomWait(int baseValue) {
    int sleepValue = random.nextInt(RANDOM_RANGE_LOWER_BOUND, RANDOM_RANGE_UPPER_BOUND);
    sleep(sleepValue + baseValue);
  }

  protected Set<String> readAccountsFromFiles(Path filePath) {
    Set<String> accounts = new HashSet<>();
    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        accounts.add(line);
      }
    } catch (IOException ex) {
      System.err.println(
              "IOException occurred while reading accounts from file: " + ex.getMessage());
    }
    return accounts;
  }
}

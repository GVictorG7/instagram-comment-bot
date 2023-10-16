package com.victor.instagramcommentbot;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.victor.instagramcommentbot.page.InstagramHomePage;
import com.victor.instagramcommentbot.page.InstagramLoginPage;
import com.victor.instagramcommentbot.page.InstagramPostPage;
import io.qameta.allure.selenide.AllureSelenide;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.edge.EdgeOptions;

public class MainPageTest {
  private static final int SHORT_WAIT_BASE_VALUE = 5_500;
  private static final int LONG_WAIT_BASE_VALUE = 11_000;
  private static final int VERY_LONG_WAIT_BASE_VALUE = 200_000;
  private static final int RANDOM_RANGE_LOWER_BOUND = 500;
  private static final int RANDOM_RANGE_UPPER_BOUND = 3_000;
  private static final String TAG_CHARACTER = "@";
  private final Random random = new Random();
  private final Properties properties = new Properties();
  private final InstagramLoginPage instagramLoginPage = new InstagramLoginPage();
  private final InstagramHomePage instagramHomePage = new InstagramHomePage();
  private final InstagramPostPage instagramPostPage = new InstagramPostPage();

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

  @Test
  void instagram() throws IOException {
    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    }
    login();
    navigateToTargetPost();
    int numberOfPostedComments = fillComments();
    assertEquals(273, numberOfPostedComments);
  }

  private void randomWait(int baseValue) {
    int sleepValue = random.nextInt(RANDOM_RANGE_LOWER_BOUND, RANDOM_RANGE_UPPER_BOUND);
    try {
      Thread.sleep(sleepValue + baseValue);
    } catch (InterruptedException e) {
      System.err.println("InterruptedException occurred" + e.getMessage());
    }
  }

  private void login() {
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
    instagramLoginPage.notSaveLoginInfo.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  private void navigateToTargetPost() {
    instagramHomePage.notTurnOnNotifications.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
    System.out.println("Navigating to the post link...");
    open(properties.getProperty("instagram.post"));
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  private int fillComments() {
    int numberOfPostedComments = 0;
    try (BufferedReader bufferedReader =
        new BufferedReader(new FileReader("src/test/resources/comments.txt"))) {
      System.out.println("Starting posting comments...");
      String commentLine = bufferedReader.readLine();
      do {
        postComment(commentLine);
        numberOfPostedComments++;
        commentLine = bufferedReader.readLine();
      } while (commentLine != null);
    } catch (IOException e) {
      System.err.println(
          "IOException occurred. Either the file was not found, or an error occurred at reading: "
              + e.getMessage());
    }
    System.out.println("Commenting finished!");
    return numberOfPostedComments;
  }

  private void postComment(String commentLine) {
    while (!instagramPostPage.commentInput.getText().isEmpty()) {
      System.out.println(
          "commentInput is not empty, maybe there is comment timeout. Waiting for "
              + VERY_LONG_WAIT_BASE_VALUE
              + "ms");
      randomWait(VERY_LONG_WAIT_BASE_VALUE);
    }

    String commentToPost = TAG_CHARACTER + commentLine;
    instagramPostPage.commentInput.sendKeys(commentToPost);
    System.out.println("Posting comment: " + instagramPostPage.commentInput.getText());
    randomWait(SHORT_WAIT_BASE_VALUE);
    instagramPostPage.postButton.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
  }
}

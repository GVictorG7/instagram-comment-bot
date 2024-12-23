package com.victor.instagramcommentbot;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.victor.instagramcommentbot.page.InstagramMyProfilePage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import com.victor.instagramcommentbot.utils.SetOperations;
import org.junit.jupiter.api.Test;

class InstagramFollowersBotTest extends InstagramTestBase {
  private static final Path FOLLOWERS_FILE_PATH = Paths.get("src/test/resources/followers.txt");
  private static final Path FOLLOWING_FILE_PATH = Paths.get("src/test/resources/following.txt");
  private static final Path FRIENDS_FILE_PATH = Paths.get("src/test/resources/friends.txt");
  private final InstagramMyProfilePage instagramMyProfilePage = new InstagramMyProfilePage();

  @Test
  void instagramFollowersBotTest() throws IOException {
    Set<String> oldFollowers = readAccountsFromFiles(FOLLOWERS_FILE_PATH);
    Set<String> oldFollowing = readAccountsFromFiles(FOLLOWING_FILE_PATH);

    instagramSetUp();

    System.out.println("Starting collecting Followers...");
    Set<String> followers =
        extractAccounts(
            instagramMyProfilePage.followersSectionButton,
            instagramMyProfilePage.followersSectionPanel,
            instagramMyProfilePage.followerElementList);
    System.out.println("Followers collected: " + followers.size());
    System.out.println("Starting collecting Following...");
    Set<String> following =
        extractAccounts(
            instagramMyProfilePage.followingSectionButton,
            instagramMyProfilePage.followingSectionPanel,
            instagramMyProfilePage.followingElementList);
    System.out.println("Followings collected: " + following.size());

    writeAccountsToFile(followers, FOLLOWERS_FILE_PATH);
    writeAccountsToFile(following, FOLLOWING_FILE_PATH);

    System.out.println("Followers lost: ");
    System.out.println(SetOperations.difference(oldFollowers, followers));

    System.out.println("New Followers :");
    System.out.println(SetOperations.difference(followers, oldFollowers));

    System.out.println("Followings removed: ");
    System.out.println(SetOperations.difference(oldFollowing, following));

    System.out.println("New Followings: ");
    System.out.println(SetOperations.difference(following, oldFollowing));

    System.out.println("Not following back: ");
    System.out.println(SetOperations.difference(following, followers));

    System.out.println("Fans: ");
    System.out.println(SetOperations.difference(followers, following));

    System.out.println("Friends: ");
    Set<String> friends = SetOperations.intersection(following, followers);
    System.out.println(friends);
    writeAccountsToFile(friends, FRIENDS_FILE_PATH);

    // TODO add blacklist
  }

  private void navigateToMyProfilePage() {
    randomWait(SHORT_WAIT_BASE_VALUE);
    System.out.println("Navigating to my profile link...");
    open(properties.getProperty("instagram.my_profile"));
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  private Set<String> extractAccounts(
      SelenideElement button, SelenideElement panel, ElementsCollection panelElements) {
    navigateToMyProfilePage();
    button.click();
    randomWait(SHORT_WAIT_BASE_VALUE);

    scrollUntilFetchComplete(panel, panelElements);

    String accountElements = panel.getText();
    String[] accountElementsList = accountElements.split("\n");

    return eliminateExtraElements(accountElementsList);
  }

  private void scrollUntilFetchComplete(SelenideElement panel, ElementsCollection panelElements) {
    int itemCountBeforeScroll, itemCountAfterScroll;
    ElementsCollection items;

    do {
      items = panelElements;
      itemCountBeforeScroll = items.size();
      executeJavaScript("arguments[0].scrollTop = arguments[0].scrollHeight", panel);
      randomWait(SCROLL_WAIT_VALUE);
      itemCountAfterScroll = items.size();
    } while (itemCountAfterScroll > itemCountBeforeScroll);
  }

  private Set<String> eliminateExtraElements(String[] accountElements) {
    Set<String> filteredElements = new HashSet<>();

    filteredElements.add(accountElements[0]);
    for (int i = 1; i < accountElements.length; i++) {
      if (accountElements[i - 1].equals("Remove") || accountElements[i - 1].equals("Following")) {
        filteredElements.add(accountElements[i]);
      }
    }

    return filteredElements;
  }

  private Set<String> readAccountsFromFiles(Path filePath) {
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

  private void writeAccountsToFile(Set<String> accounts, Path filePath) {
    try (BufferedWriter writer =
        Files.newBufferedWriter(
            filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      for (String account : accounts) {
        writer.write(account);
        writer.newLine();
      }
    } catch (IOException ex) {
      System.err.println("IOException occurred while writing accounts to files" + ex.getMessage());
    }
  }
}

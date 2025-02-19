package com.victor.instagramcommentbot;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.victor.instagramcommentbot.page.InstagramPostPage;
import com.victor.instagramcommentbot.utils.SetOperations;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InstagramCommentBotTest extends InstagramTestBase {
  private static final int VERY_LONG_WAIT_BASE_VALUE = 200_000;
  private static final int EXPECTED_NUMBER_OF_POSTED_COMMENTS = 242;
  private static final String TAG_CHARACTER = "@";
  private static final Path FRIENDS_FILE_PATH = Paths.get("src/test/resources/comments.txt");
  private static final Path BLACKLIST_FILE_PATH = Paths.get("src/test/resources/blacklist.txt");
  private static final Path ADDITIONS_FILE_PATH = Paths.get("src/test/resources/additions.txt");
  private final InstagramPostPage instagramPostPage = new InstagramPostPage();

  @Test
  void instagramCommentBotTest() throws IOException {
    instagramSetUp();
    navigateToTargetPost();
    Set<String> commentsToPost = getComments();
    int numberOfPostedComments = fillComments(commentsToPost);
    assertEquals(EXPECTED_NUMBER_OF_POSTED_COMMENTS, numberOfPostedComments);
  }

  private Set<String> getComments() {
    Set<String> friends = readAccountsFromFiles(FRIENDS_FILE_PATH);
    Set<String> additions = readAccountsFromFiles(ADDITIONS_FILE_PATH);
    Set<String> blacklisted = readAccountsFromFiles(BLACKLIST_FILE_PATH);
    Set<String> result = SetOperations.union(friends, additions);
    return SetOperations.difference(result, blacklisted);
  }

  private void navigateToTargetPost() {
    randomWait(SHORT_WAIT_BASE_VALUE);
    System.out.println("Navigating to the post link...");
    open(properties.getProperty("instagram.post"));
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  private int fillComments(Set<String> comments) {
    int numberOfPostedComments = 0;
    System.out.println("Starting posting comments...");
    for (String comment : comments) {
      postComment(comment);
      numberOfPostedComments++;
    }
    System.out.println("Commenting finished!");
    return numberOfPostedComments;
  }

  private void postComment(String commentLine) {
    while (!instagramPostPage.commentInput.getText().isEmpty()) {
      retryPostExistingComment();
    }

    String commentToPost = TAG_CHARACTER + commentLine;
    instagramPostPage.commentInput.sendKeys(commentToPost);
    System.out.println("Posting comment: " + instagramPostPage.commentInput.getText());
    randomWait(SHORT_WAIT_BASE_VALUE);
    instagramPostPage.postButton.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
  }

  private void retryPostExistingComment() {
    System.out.println(
        "commentInput is not empty, maybe there is comment timeout. Waiting for "
            + VERY_LONG_WAIT_BASE_VALUE
            + "ms");
    randomWait(VERY_LONG_WAIT_BASE_VALUE);
    instagramPostPage.postButton.click();
    randomWait(SHORT_WAIT_BASE_VALUE);
  }
}

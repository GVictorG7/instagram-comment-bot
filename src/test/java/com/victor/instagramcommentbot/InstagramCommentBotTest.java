package com.victor.instagramcommentbot;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.victor.instagramcommentbot.page.InstagramPostPage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class InstagramCommentBotTest extends InstagramTestBase {
  private static final int VERY_LONG_WAIT_BASE_VALUE = 200_000;
  private static final int EXPECTED_NUMBER_OF_POSTED_COMMENTS = 242;
  private static final String TAG_CHARACTER = "@";
  private final InstagramPostPage instagramPostPage = new InstagramPostPage();

  @Test
  void instagramCommentBotTest() throws IOException {
    instagramSetUp();
    navigateToTargetPost();
    int numberOfPostedComments = fillComments();
    assertEquals(EXPECTED_NUMBER_OF_POSTED_COMMENTS, numberOfPostedComments);
  }

  private void navigateToTargetPost() {
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

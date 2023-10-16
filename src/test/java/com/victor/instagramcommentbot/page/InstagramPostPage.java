package com.victor.instagramcommentbot.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class InstagramPostPage {
  public SelenideElement commentInput = $x("//textarea[@placeholder='Add a comment…']");
  public SelenideElement postButton = $x("//div[contains(text(), 'Post')]");
}

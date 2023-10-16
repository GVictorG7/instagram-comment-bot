package com.victor.instagramcommentbot.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class InstagramHomePage {
  public SelenideElement notTurnOnNotifications = $x("//button[contains(text(), 'Not Now')]");
}

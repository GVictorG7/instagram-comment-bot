package com.victor.instagramcommentbot.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class InstagramLoginPage {
  public SelenideElement declineOptionalCookiesButton =
      $x("//button[contains(text(), 'Decline optional cookies')]");
  public SelenideElement usernameInput = $x("//input[@name='username']");
  public SelenideElement passwordInput = $x("//input[@name='password']");
  public SelenideElement loginButton = $x("//div[contains(text(), 'Log in')]");
  public SelenideElement notSaveLoginInfo = $x("//div[contains(text(), 'Not Now')]");
}

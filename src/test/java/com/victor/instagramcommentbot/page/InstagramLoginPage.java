package com.victor.instagramcommentbot.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class InstagramLoginPage {
  public SelenideElement declineOptionalCookiesButton =
      $x("//button[contains(text(), 'Decline optional cookies')]");
  public SelenideElement usernameInput = $x("//input[@name='email']");
  public SelenideElement passwordInput = $x("//input[@name='pass']");
  public SelenideElement loginButton = $x("//span[contains(text(), 'Log in')]");
  public SelenideElement notSaveLoginInfo = $x("//div[contains(text(), 'Not now')]");
}

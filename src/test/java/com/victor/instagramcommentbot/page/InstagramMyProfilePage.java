package com.victor.instagramcommentbot.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class InstagramMyProfilePage {
  public SelenideElement followersSectionButton = $x("//a[contains(text(), ' followers')]");
  public SelenideElement followersSectionPanel =
      $x("//div[contains(text(), 'Followers')]/../../../../../div[3]");
  public ElementsCollection followerElementList = followersSectionPanel.$$("div");

  public SelenideElement followingSectionButton = $x("//a[contains(text(), ' following')]");
  public SelenideElement followingSectionPanel =
      $x("//div[contains(text(), 'Following')]/../../../../../div[4]");
  public ElementsCollection followingElementList = followingSectionPanel.$$("div");
}

Feature: Vodafone Mobile Automation

  @MobileTest @LiveTV
  Scenario: TC_01_live tv contents
    Given user click on TV
    When user click search button
    Then validate the contents from csv file

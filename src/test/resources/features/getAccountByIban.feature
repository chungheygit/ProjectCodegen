Feature: Get account by iban feature

  Scenario: Getting acount by iban from existing account while logged in as employee returns 200
    When I log in with email "mark@inholland.nl" and password "password" for GetAccountByIbanSteps
    And I get account by iban "NL58INHO0000000090"
    Then I get status 200 in GetAccountByIbanSteps

  Scenario: Getting acount by iban from non-existing account while logged in as employee returns 400
    When I log in with email "mark@inholland.nl" and password "password" for GetAccountByIbanSteps
    And I get account by iban "NL58INHO0000007852"
    Then I get status 400 in GetAccountByIbanSteps

  Scenario: Getting acount by iban from account that does not belong to logged in customer returns 403
    When I log in with email "user1@gmail.com" and password "password" for GetAccountByIbanSteps
    And I get account by iban "NL58INHO0123456788"
    Then I get status 403 in GetAccountByIbanSteps
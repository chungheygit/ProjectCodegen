Feature: Get all accounts feature

  Scenario: Getting all accounts as employee returns status 201
    When I log in with email "emp" and password "password" for account
    And I get all accounts
    Then I get status 201 in getAllAccountsSteps

  Scenario: Getting all accounts as customer returns status 403
    When I log in with email "cus" and password "password" for account
    And I get all accounts
    Then I get status 403 in getAllAccountsSteps




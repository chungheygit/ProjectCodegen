Feature: Create Account feature

  Scenario: Creating valid account as employee returns status 201
    When I log in with email "emp" and password "password" for account
    And I create an account with userId 6 and open "true" and balance 1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 201 in AccountSteps

  Scenario: Creating valid account as customer returns status 403
    When I log in with email "cus" and password "password" for account
    And I create an account with userId 7 and open "true" and balance 1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 403 in AccountSteps

  Scenario: Creating valid account with negative balance as employee returns status 403
    When I log in with email "emp" and password "password" for account
    And I create an account with userId 6 and open "true" and balance -1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 403 in AccountSteps

  Scenario: Creating valid account with negative absolute limit as employee returns status 403
    When I log in with email "emp" and password "password" for account
    And I create an account with userId 6 and open "true" and balance 1000 and absoluteLimit -1000 and accountType "CURRENT"
    Then I get status 403 in AccountSteps



Feature: update account feature

  Scenario: updating valid account as employee returns status 201
    When I log in with email "emp" and password "password" for account
    And I update an account with open "true" and balance 1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 201 in updateAccountSteps


  Scenario: updating valid account as customer returns status 403
    When I log in with email "cus" and password "password" for account
    And I update an account with open "true" and balance 1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 403 in updateAccountSteps

  Scenario: Creating valid account with negative balance as employee returns status 403
    When I log in with email "emp" and password "password" for account
    And I create an account with open "true" and balance -1000 and absoluteLimit 1000 and accountType "CURRENT"
    Then I get status 403 in updateAccountSteps


  Scenario: Creating valid account with negative absolute limit as employee returns status 403
    When I log in with email "emp" and password "password" for account
    And I create an account with open "true" and balance 1000 and absoluteLimit -1000 and accountType "CURRENT"
    Then I get status 403 in updateAccountSteps

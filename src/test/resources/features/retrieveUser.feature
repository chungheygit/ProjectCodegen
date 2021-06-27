Feature: User tests

  Scenario: Retrieve all users is status OK
    When I log in with email "emp" and password "password" for user
    And I retrieve all users
    Then I should get a 200 http status

  Scenario: Getting a list of all users
    When I log in with email "emp" and password "password" for user
    When I retrieve all users
    Then I get a list of 7 users
    Then I should get a 200 http status

  Scenario: Getting a list of all users with invalid Email
    When I log in with email "invalidemail" and password "password" for user
    Then I should get a 404 http status

  Scenario: Getting a list of all users with invalid password
    When I log in with email "emp" and password "invalidpassword" for user
    Then I should get a 404 http status



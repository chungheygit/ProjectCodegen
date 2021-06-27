Feature: Login test

  Scenario: Logging in with an existing user returns status 200 and jwt token
    When I log in with email "mark@inholland.nl" and password "password"
    Then I get status 200
    And The response contains a Jwt token

  Scenario: Logging in with wrong password returns status 404
    When I log in with email "mark@inholland.nl" and password "pawdard"
    Then I get status 404

  Scenario: Logging in with wrong email returns status 404
    When I log in with email "idontexist@gmail.com" and password "password"
    Then I get status 404


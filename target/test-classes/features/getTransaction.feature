Feature: Get Transactions feature

  Scenario: Getting transactions as employee returns status 200
  When I get transactions by iban
  Then I get status 200 in GetTransactionsSteps
  And I get array of transactions by iban

  Scenario: get transactions as employee returns status 200 when limit is 1
  When I get transactions by iban with limit 1
  Then I get status 200 in GetTransactionsSteps
  And I get array with length of 1 transactions by iban

  Scenario: get transaction as employee returns status 200 and all transactions are after 25/12/2019 12:00:00
    When I get transactions by iban with StartDateTime "25/12/2019 12:00:00"
    Then I get status 200 in GetTransactionsSteps
    And I get array of transactions by iban after "25/12/2019 12:00:00"

  Scenario: get transaction as employee returns status 200 and all transaction are before 20/12/2020 12:00:00
    When I get transactions by iban with EndDateTime "20/12/2020 12:00:00"
    Then I get status 200 in GetTransactionsSteps
    And I get array of transactions by iban before "20/12/2020 12:00:00"


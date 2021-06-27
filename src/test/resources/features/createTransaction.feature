Feature: Create transaction feature

  Scenario: Creating valid transaction as employee returns status 201
    When I log in with email "mark@inholland.nl" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456701" and amount 10 and description "test"
    Then I get status 201 in CreateTransactionSteps

  Scenario: Creating valid transaction as customer and owner of sender account returns status 201
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456701" and amount 10 and description "test"
    Then I get status 201 in CreateTransactionSteps

  Scenario: Creating transaction as customer and not the owner of sender account returns status 403 and message "You are not the sender"
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL01INHO0000000001" and receiver "NL58INHO0123456701" and amount 10 and description "test"
    Then I get status 403 in CreateTransactionSteps
    And I get message containing "You are not the sender" in CreateTransactionSteps

  Scenario: Creating transaction to a closed account returns status 400 and message "Can't transfer to/from closed accounts"
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456702" and amount 10 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Can't transfer to/from closed accounts" in CreateTransactionSteps

  Scenario: Creating transaction with same sender and receiver returns status 400 and message "Can't send money to same account"
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456788" and amount 10 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Can't send money to same account" in CreateTransactionSteps

  Scenario: Creating transaction exceeding transaction limit returns status 400 and message "Amount exceeded transaction limit"
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456701" and amount 1004 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Amount exceeded transaction limit" in CreateTransactionSteps

  Scenario: Creating transaction with negative amount returns status 400 and message "Invalid amount"
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0123456701" and amount -10 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Invalid amount" in CreateTransactionSteps

  Scenario: Creating valid withdrawal transaction returns status 201 and transaction will have transactiontype withdrawal
    When I log in with email "user1@gmail.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0000000089" and receiver "NL58INHO0000000090" and amount 10 and description "test"
    Then I get status 201 in CreateTransactionSteps
    And I get transaction with transactiontype withdrawal in CreateTransactionSteps

  Scenario: Creating transaction to savings account from other user returns status 403 and message "Forbidden to transfer from/to savings account from another user."
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0000000089" and amount 10 and description "test"
    Then I get status 403 in CreateTransactionSteps
    And I get message containing "Forbidden to transfer from/to savings account from another user." in CreateTransactionSteps

  Scenario: Creating transaction to savings account from other user returns status 403 and message "Forbidden to transfer from/to savings account from another user."
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0000000089" and amount 10 and description "test"
    Then I get status 403 in CreateTransactionSteps
    And I get message containing "Forbidden to transfer from/to savings account from another user." in CreateTransactionSteps

  Scenario: Creating transaction to savings account from other user returns status 403 and message "Forbidden to transfer from/to savings account from another user."
    When I log in with email "lio@test2.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0123456788" and receiver "NL58INHO0000000089" and amount 10 and description "test"
    Then I get status 403 in CreateTransactionSteps
    And I get message containing "Forbidden to transfer from/to savings account from another user." in CreateTransactionSteps

  Scenario: Creating transaction that hits absolute limit returns status 400 and message "Balance too low"
    When I log in with email "user1@gmail.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0000000089" and receiver "NL58INHO0000000090" and amount 60 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Balance too low" in CreateTransactionSteps

  Scenario: Creating transaction with invalid sender returns 400 and message containing "Invalid iban entered"
    When I log in with email "user1@gmail.com" and password "password" for transaction
    And I create a transaction with sender "argoaiejrgoiaejrg" and receiver "NL58INHO0000000090" and amount 60 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Invalid iban entered" in CreateTransactionSteps

  Scenario: Creating transaction with invalid receiver returns 400 and message containing "Invalid iban entered"
    When I log in with email "user1@gmail.com" and password "password" for transaction
    And I create a transaction with sender "NL58INHO0000000090" and receiver "wewewewe" and amount 60 and description "test"
    Then I get status 400 in CreateTransactionSteps
    And I get message containing "Invalid iban entered" in CreateTransactionSteps

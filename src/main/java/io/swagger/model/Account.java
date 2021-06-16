package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Account
 */
@Entity
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")


public class Account   {

  @JsonProperty("user")
  private User user = null;

  @Id
  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("balance") // ALL ACCOUNTS ARE EUR ACCOUNTS
  private BigDecimal balance = new BigDecimal(0);

  @JsonProperty("createdDate")
  private java.time.LocalDate createdDate = null;

  @Enumerated(EnumType.STRING)
  @JsonProperty("accountType")
  private AccountType accountType = null;

  @JsonProperty("absoluteLimit")
  private BigDecimal absoluteLimit = null;

  @JsonProperty("open")
  private Boolean open = null;

  public Account userId(Long userId) {
    this.user = userId;
    return this;
  }

  public Account(Account account, String iban, BigDecimal balance, java.time.LocalDate createdDate, AccountType accountType, BigDecimal absoluteLimit, Boolean open) {
    this.user = setUser(userId); //findUserByUserId
    this.iban = iban;
    this.balance = balance;
    this.createdDate = createdDate;
    this.accountType = accountType;
    this.absoluteLimit = absoluteLimit;
    this.open = open;
  }

  public Account() {
  }

  /**
   * Get userId
   * minimum: 1
   * @return userId
   **/
  @Schema(required = true, description = "")
      @NotNull

  @Min(1L)  public User getUser() {
    return user;
  }

  public void setUser(Long userId) {
    //findUserByID(userId)
    this.user = user;
  }

  public Account iban(String iban) {
    this.iban = iban;
    return this;
  }

  /**
   * Get iban
   * @return iban
   **/
  @Schema(required = true, description = "")
      @NotNull

  @Pattern(regexp="^NL\\d{2}INHO0\\d{9}$")   public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public Account balance(BigDecimal balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public Account createdDate(java.time.LocalDate createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  /**
   * Get createdDate
   * @return createdDate
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public java.time.LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.time.LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public Account accountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   **/
  @Schema(required = true, description = "")
      @NotNull

    public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public Account absoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
    return this;
  }

  /**
   * Get absoluteLimit
   * @return absoluteLimit
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public BigDecimal getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }

  public Account open(Boolean open) {
    this.open = open;
    return this;
  }

  /**
   * Get open
   * @return open
   **/
  @Schema(example = "false", required = true, description = "")
      @NotNull

    public Boolean isOpen() {
    return open;
  }

  public void setOpen(Boolean open) {
    this.open = open;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.user, account.user) &&
        Objects.equals(this.iban, account.iban) &&
        Objects.equals(this.balance, account.balance) &&
        Objects.equals(this.createdDate, account.createdDate) &&
        Objects.equals(this.accountType, account.accountType) &&
        Objects.equals(this.absoluteLimit, account.absoluteLimit) &&
        Objects.equals(this.open, account.open);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, iban, balance, createdDate, accountType, absoluteLimit, open);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    
    sb.append("    userId: ").append(toIndentedString(user)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
    sb.append("    open: ").append(toIndentedString(open)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

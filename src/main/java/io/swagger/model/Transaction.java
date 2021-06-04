package io.swagger.model;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transaction
 */
@Entity
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")

public class Transaction   {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id = null;

  //@ManyToOne()
  @JsonProperty("userPerforming")
  private Integer userPerforming = null;

  @JsonProperty("timestamp")
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private LocalDateTime timestamp = null;

  @JsonProperty("sender")
  private String sender = null;

  @JsonProperty("receiver")
  private String receiver = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("description")
  private String description = null;

  public Transaction(Integer userPerforming, LocalDateTime timestamp, String sender, String receiver, BigDecimal amount, String description) {
    this.userPerforming = userPerforming;
    this.timestamp = timestamp;
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
    this.description = description;
  }

  public Transaction id(Integer id) {
    this.id = id;
    return this;
  }

  public Transaction ( /*Integer userPerform... door Abdelkhalak, Yassine

Abdelkhalak, Yassine13:51
public Transaction(/*Integer userPerforming, */ String sender, String receiver, BigDecimal amount, String description) {
    //this.userPerforming = userPerforming;
    this.timestamp = LocalDateTime.now();
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
    this.description = description;
  }

  public Transaction() {
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Transaction userPerforming(Integer userPerforming) {
    this.userPerforming = userPerforming;
    return this;
  }

  /**
   * Get userPerforming
   * @return userPerforming
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getUserPerforming() {
    return userPerforming;
  }

  public void setUserPerforming(Integer userPerforming) {
    this.userPerforming = userPerforming;
  }

  public Transaction timestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Transaction sender(String sender) {
    this.sender = sender;
    return this;
  }

  /**
   * Get sender
   * @return sender
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public Transaction receiver(String receiver) {
    this.receiver = receiver;
    return this;
  }

  /**
   * Get receiver
   * @return receiver
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public Transaction amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0
   * @return amount
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
  @DecimalMin("0")  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Transaction description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.id, transaction.id) &&
        Objects.equals(this.userPerforming, transaction.userPerforming) &&
        Objects.equals(this.timestamp, transaction.timestamp) &&
        Objects.equals(this.sender, transaction.sender) &&
        Objects.equals(this.receiver, transaction.receiver) &&
        Objects.equals(this.amount, transaction.amount) &&
        Objects.equals(this.description, transaction.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userPerforming, timestamp, sender, receiver, amount, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userPerforming: ").append(toIndentedString(userPerforming)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
    sb.append("    receiver: ").append(toIndentedString(receiver)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

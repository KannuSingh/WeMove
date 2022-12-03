package com.wemove.model;

import java.util.Date;
import java.util.List;

public class MoveRequest {

    private String moveRequestId;
    private MoveStatus moveRequestStatus;
    private String moveRequestOwner;
    private String createdOn;
    private String updatedOn;
    private String moveTitle;
    private String moveType;
    private String moveDate;
    private MoveHelp moveHelp;
    private String pickupFloorLevel;
    private Address pickupAddress;
    private String deliveryFloorLevel;
    private Address deliveryAddress;
    private List<InventoryItemGroup> itemInventory;
    private List<PriceQuote> priceQuotes;

    public String getMoveRequestOwner() {
        return moveRequestOwner;
    }

    public void setMoveRequestOwner(String moveRequestOwner) {
        this.moveRequestOwner = moveRequestOwner;
    }

    public String getMoveRequestId() {
        return moveRequestId;
    }

    public void setMoveRequestId(String moveRequestId) {
        this.moveRequestId = moveRequestId;
    }

    public MoveStatus getMoveRequestStatus() {
        return moveRequestStatus;
    }

    public void setMoveRequestStatus(MoveStatus moveRequestStatus) {
        this.moveRequestStatus = moveRequestStatus;
    }

    public String getMoveTitle() {
        return moveTitle;
    }

    public void setMoveTitle(String moveTitle) {
        this.moveTitle = moveTitle;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(String moveDate) {
        this.moveDate = moveDate;
    }

    public MoveHelp getMoveHelp() {
        return moveHelp;
    }

    public void setMoveHelp(MoveHelp moveHelp) {
        this.moveHelp = moveHelp;
    }

    public String getPickupFloorLevel() {
        return pickupFloorLevel;
    }

    public void setPickupFloorLevel(String pickupFloorLevel) {
        this.pickupFloorLevel = pickupFloorLevel;
    }

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(Address pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryFloorLevel() {
        return deliveryFloorLevel;
    }

    public void setDeliveryFloorLevel(String deliveryFloorLevel) {
        this.deliveryFloorLevel = deliveryFloorLevel;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<InventoryItemGroup> getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(List<InventoryItemGroup> itemInventory) {
        this.itemInventory = itemInventory;
    }

    public List<PriceQuote> getPriceQuotes() {
        return priceQuotes;
    }

    public void setPriceQuotes(List<PriceQuote> priceQuotes) {
        this.priceQuotes = priceQuotes;
    }

    @Override
    public String toString() {
        return "MoveRequest{" +
                "moveRequestId='" + moveRequestId + '\'' +
                ", moveRequestStatus=" + moveRequestStatus +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", moveTitle='" + moveTitle + '\'' +
                ", moveType='" + moveType + '\'' +
                ", moveDate='" + moveDate + '\'' +
                ", moveHelp=" + moveHelp +
                ", pickupFloorLevel='" + pickupFloorLevel + '\'' +
                ", pickupAddress=" + pickupAddress +
                ", deliveryFloorLevel='" + deliveryFloorLevel + '\'' +
                ", deliveryAddress=" + deliveryAddress +
                ", itemInventory=" + itemInventory +
                '}';
    }
}

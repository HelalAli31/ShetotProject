package Shared_classes;

import java.io.Serializable;

public class Subscriber implements Serializable {
    private static final long serialVersionUID = 1L; // Add this for serialization

    private int id; // subscriber_id
    private String status; // ENUM('active', 'frozen')
    private int detailedSubscriptionHistory; // detailed_subscription_history
    private int lateReturns; // late_returns

    // Constructors
    public Subscriber() {
    }

    public Subscriber(int id, String status, int detailedSubscriptionHistory, int lateReturns) {
        this.id = id;
        this.status = status;
        this.detailedSubscriptionHistory = detailedSubscriptionHistory;
        this.lateReturns = lateReturns;
    }

    // Getters and Setters
    public int getSubscriberId() {
        return id;
    }

    public void setSubscriberId(int subscriberId) {
        this.id = subscriberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("active") || status.equals("frozen")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status. Must be 'active' or 'frozen'.");
        }
    }

    public int getDetailedSubscriptionHistory() {
        return detailedSubscriptionHistory;
    }

    public void setDetailedSubscriptionHistory(int detailedSubscriptionHistory) {
        this.detailedSubscriptionHistory = detailedSubscriptionHistory;
    }

    public int getLateReturns() {
        return lateReturns;
    }

    public void setLateReturns(int lateReturns) {
        this.lateReturns = lateReturns;
    }

    

    // Validation Method
    public boolean areDetailsValid() {
        return id > 0 && (status.equals("active") || status.equals("frozen"));
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "subscriberId=" + id +
                ", status='" + status + '\'' +
                ", detailedSubscriptionHistory=" + detailedSubscriptionHistory +
                ", lateReturns=" + lateReturns +
                '}';
    }
}

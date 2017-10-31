/**
 * Contribution info for specified recipient and zipcode
 */

public class RecipientZipcode {

    String recipientID;
    String zipCode;

    public RecipientZipcode(String recipientID, String zipCode) {
        this.recipientID = recipientID;
        this.zipCode = zipCode;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public int hashCode() { return recipientID.hashCode() ^ zipCode.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RecipientZipcode)) return false;
        RecipientZipcode other = (RecipientZipcode) o;
        return this.recipientID.equals(other.getRecipientID()) &&
                this.zipCode.equals(other.getZipCode());
    }
}

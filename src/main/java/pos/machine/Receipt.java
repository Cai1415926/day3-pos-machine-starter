package pos.machine;

import java.util.List;

public class Receipt {
    private final List<ReceiptItem> receiptItems;
    private final int totalPrice;

    public Receipt(List<ReceiptItem> receiptItems, int totalPrice) {
        this.receiptItems = receiptItems;
        this.totalPrice = totalPrice;
    }
}

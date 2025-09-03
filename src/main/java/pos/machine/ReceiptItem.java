package pos.machine;

public class ReceiptItem extends Item{
    private final int subTotal;

    public ReceiptItem(String barcode, String name, int price) {
        super(barcode, name, price);
        this.subTotal = 0;
    }

    public ReceiptItem(String barcode, String name, int price, int subTotal) {
        super(barcode, name, price);
        this.subTotal = subTotal;
    }

    public int getSubTotal() {
        return subTotal;
    }
}

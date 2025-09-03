package pos.machine;

import java.util.List;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        return null;
    }

    public List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> items = loadAllItems();
        return items.stream().map(item -> {
            int count = (int) barcodes.stream().filter(barcode -> barcode.equals(item.getBarcode())).count();
            if (count == 0) {
                throw new RuntimeException("Item with barcode " + item.getBarcode() + " not found in the input barcodes.");
            }
            return new ReceiptItem(item.getBarcode(), item.getName(), item.getPrice());
        }).toList();
    }

    public List<Item> loadAllItems() {
        return ItemsLoader.loadAllItems();
    }

    public Receipt calculateCost(List<ReceiptItem> receiptItems) {
        List<ReceiptItem> receiptItems1 = calculateItemsCost(receiptItems);
        int totalPrice = calculateTotalPrice(receiptItems1);
        return new Receipt(receiptItems1, totalPrice);
    }

    public List<ReceiptItem> calculateItemsCost(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .collect(Collectors.toMap(ReceiptItem::getBarcode,
                        item -> new ReceiptItem(item.getBarcode(), item.getName(), item.getPrice(), item.getPrice()),
                        (existing, incoming) -> new ReceiptItem(
                                existing.getBarcode(),
                                existing.getName(),
                                existing.getPrice(),
                                existing.getPrice() + incoming.getPrice()
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    public int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream().mapToInt(ReceiptItem::getSubTotal).sum();
    }

    public String renderReceipt(Receipt receipt) {
        String itemsReceipt = generateItemsReceipt(receipt);

        return null;
    }

    public String generateItemsReceipt(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        for (ReceiptItem item : receipt.getReceiptItems()) {
            sb.append(String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)\n",
                    item.getName(), item.getSubTotal() / item.getPrice(), item.getPrice(), item.getSubTotal()));
        }
        return sb.toString();
    }

    public String generateReceipt(String itemsReceipt, int totalPrice) {
        StringBuilder sb = new StringBuilder("***<store earning no money>Receipt***\n");
        sb.append(itemsReceipt);
        sb.append("----------------------\n");
        sb.append(String.format("Total: %d (yuan)\n", totalPrice));
        sb.append("**********************");
        return sb.toString();
    }
}

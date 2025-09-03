package pos.machine;

import java.util.List;

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

        return null;
    }

    public List<ReceiptItem> calculateItemsCost(List<ReceiptItem> receiptItems) {
        return receiptItems.stream().map(receiptItem -> {
            int count = (int) receiptItems.stream()
                    .filter(tmp -> tmp.getBarcode().equals(receiptItem.getBarcode())).count();
            return new ReceiptItem(receiptItem.getBarcode(), receiptItem.getName(), receiptItem.getPrice(), receiptItem.getPrice() * count);
        }).toList();
    }
}

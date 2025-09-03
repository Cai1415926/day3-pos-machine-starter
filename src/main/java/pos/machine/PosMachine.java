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

        return null;
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
}

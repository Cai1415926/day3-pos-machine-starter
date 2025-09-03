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
            return new ReceiptItem(item.getBarcode(), item.getName(), item.getPrice(), item.getPrice() * count);
        }).toList();
    }

    public List<Item> loadAllItems() {
        return ItemsLoader.loadAllItems();
    }
}

package pos.machine;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        Receipt receipt = calculateCost(receiptItems);
        return renderReceipt(receipt);
    }

    public List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> items = loadAllItems();
        Map<String, Item> itemMap = items.stream().collect(Collectors.toMap(Item::getBarcode, item -> item));
        return barcodes.stream().map(barcode -> {
            if (!itemMap.containsKey(barcode)) {
                throw new RuntimeException("Item with barcode " + barcode + " not found.");
            }
            Item item = itemMap.get(barcode);
            return new ReceiptItem(item.getBarcode(), item.getName(), item.getPrice());
        }).collect(Collectors.toList());
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
        Map<String, Integer> countMap = receiptItems.stream().collect(Collectors.groupingBy(ReceiptItem::getBarcode, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
        return receiptItems.stream()
                .collect(Collectors.toMap(ReceiptItem::getBarcode, item -> item, (existing, incoming) -> existing, LinkedHashMap::new))
                .entrySet()
                .stream()
                .map(entry -> {
                    ReceiptItem item = entry.getValue();
                    int count = countMap.get(item.getBarcode());
                    return new ReceiptItem(item.getBarcode(), item.getName(), item.getPrice(),item.getPrice() * count);
                })
                .collect(Collectors.toList());
    }

    public int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream().mapToInt(ReceiptItem::getSubTotal).sum();
    }

    public String renderReceipt(Receipt receipt) {
        String itemsReceipt = generateItemsReceipt(receipt);
        return generateReceipt(itemsReceipt, receipt.getTotalPrice());
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

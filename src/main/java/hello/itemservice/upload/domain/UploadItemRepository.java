package hello.itemservice.upload.domain;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 상품 리포지토리
 */
@Repository
public class UploadItemRepository {

    private final Map<Long, Item> store = new HashMap<>();
    private long sequence = 0L;

    public Item save(Item item) {

        item.setId(++sequence);

        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {

        return store.get(id);
    }
}

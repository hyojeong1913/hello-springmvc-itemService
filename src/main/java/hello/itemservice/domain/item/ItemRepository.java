package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 저장소
 */
@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    /**
     * 상품 등록
     *
     * @param item
     * @return
     */
    public Item save(Item item) {

        item.setId(++sequence);

        store.put(item.getId(), item);

        return item;
    }

    /**
     * 상품 상세 조회
     *
     * @param id
     * @return
     */
    public Item findById(Long id) {
        return store.get(id);
    }

    /**
     * 상품 목록 조회
     *
     * @return
     */
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * 상품 수정
     *
     * @param itemId
     * @param updateParam
     */
    public void update(Long itemId, Item updateParam) {

        Item findItem = findById(itemId);

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());

        findItem.setOpen(updateParam.getOpen());
        findItem.setRegions(updateParam.getRegions());
        findItem.setItemType(updateParam.getItemType());
        findItem.setDeliveryCode(updateParam.getDeliveryCode());
    }

    /**
     * 상품 리셋
     */
    public void clearStore() {
        store.clear();
    }
}

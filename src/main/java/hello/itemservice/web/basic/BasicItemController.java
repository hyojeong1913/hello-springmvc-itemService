package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @RequiredArgsConstruct 어노테이션
 * : final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
 */
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * 상품 목록 조회
     *
     * @param model
     * @return
     */
    @GetMapping
    public String items(Model model) {

        // itemRepository 에서 모든 상품을 조회
        List<Item> items = itemRepository.findAll();

        // 모델에 담는다.
        model.addAttribute("items", items);

        // 뷰 템플릿 호출
        return "basic/items";
    }

    /**
     * 상품 상세 조회
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {

        // PathVariable 로 넘어온 상품 ID 로 상품 조회
        Item item = itemRepository.findById(itemId);

        // 모델에 담아둔다.
        model.addAttribute("item", item);

        // 뷰 템플릿 호출
        return "basic/item";
    }

    /**
     * 상품 등록 폼
     *
     * @return
     */
    @GetMapping("/add")
    public String addForm() {

        return "basic/addForm";
    }

    /**
     * 상품 등록 처리
     *
     * @return
     */
    @PostMapping("/add")
    public String save() {

        return "basic/addForm";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}

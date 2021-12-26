package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * 상품 등록 처리 - @RequestParam
     *
     * itemName 요청 파라미터 데이터를 해당 변수에 받는다.
     *
     * @param itemName
     * @param price
     * @param quantity
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName,
            @RequestParam int price,
            @RequestParam Integer quantity,
            Model model
    ) {

        // Item 객체를 생성
        Item item = new Item();

        // itemRepository 를 통해서 저장
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        // 저장된 item 을 모델에 담아서 뷰에 전달
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * 상품 등록 처리 - @ModelAttribute
     *
     * @ModelAttribute 어노테이션
     *  : 요청 파라미터 처리
     *  : 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법 (setXxx) 으로 입력해준다.
     *  : 바로 모델 (Model) 에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다.
     *
     * @param item
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV2(
            @ModelAttribute("item") Item item,
            Model model
    ) {

        itemRepository.save(item);

        // @ModelAttribute 어노테이션으로 인해 주석 가능
//        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * addItemV3 - 상품 등록 처리 - ModelAttribute 이름 생략
     *
     * @ModelAttribute 의 이름을 생략 가능
     *
     * @ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용
     * ( 클래스의 첫글자만 소문자로 변경해서 등록 )
     *
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * addItemV4 - 상품 등록 처리 - ModelAttribute 전체 생략
     *
     * @ModelAttribute 어노테이션 자체도 생략 가능
     * ( 대상 객체는 모델에 자동 등록 )
     *
     * @param item
     * @return
     */
    @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);

        return "basic/item";
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

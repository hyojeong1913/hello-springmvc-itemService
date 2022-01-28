package hello.itemservice.web.basic;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;

import java.util.*;

/**
 * @RequiredArgsConstruct 어노테이션
 * : final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
 */
@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**
     * @ModelAttribute 어노테이션을 사용하여 해당 컨트롤러를 요청할 때 regions 에서 반환한 값을 자동으로 모델에 담는다.
     *
     * @return
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {

        Map<String, String> regions = new LinkedHashMap<>();

        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    /**
     * @ModelAttribute 어노테이션을 사용하여 해당 컨트롤러를 요청할 때 itemTypes 에서 반환한 값을 자동으로 모델에 담는다.
     * 
     * @return
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {

        return ItemType.values();
    }

    /**
     * @ModelAttribute 어노테이션을 사용하여 해당 컨트롤러를 요청할 때 deliveryCodes 에서 반환한 값을 자동으로 모델에 담는다.
     * 
     * @return
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {

        List<DeliveryCode> deliveryCodes = new ArrayList<>();

        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

        return deliveryCodes;
    }

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
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addForm(Model model) {

        model.addAttribute("item", new Item());

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
//    @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * 상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 수정
     *
     * "redirect:/basic/items/" + item.getId()
     *  => redirect 에서 + item.getId() 처럼 URL 에 변수를 더해서 사용하는 것은 URL 인코딩이 안 되기 때문에 위험
     *
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV5(Item item) {

        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * RedirectAttributes 를 사용하여 리다이렉트할 때 넘어온 값에 따라 뷰 템플릿 메세지 제어
     *
     * RedirectAttributes : URL 인코딩도 해주고, pathVariable, 쿼리 파라미터까지 처리
     *
     * @param item
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes, Model model) {

        // 상품 판매 여부 체크박스 체크 여부 로그
        log.info("item.open = {}", item.getOpen());

        // 등록 지역 체크박스 체크 여부 로그
        log.info("item.regions = {}", item.getRegions());

        // 상품 종류 라디오버튼 체크 여부 로그
        log.info("item.itemType = {}", item.getItemType());

        // 검증 오류 결과 보관
        Map<String, String> errors = new HashMap<>();

        // 상품명 검증
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }

        // 상품 가격 검증
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }

        // 상품 수량 검증
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {

            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {

                errors.put("globalError", "가격 * 수량 의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증 실패 시
        if (!errors.isEmpty()) {

            model.addAttribute("errors", errors);

            // 다시 상품 등록 화면으로
            return "basic/addForm";
        }

        /**
         * 검증 성공
         */

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // pathVariable 바인딩 ( 예: {itemId} )
        // 나머지는 쿼리 파라미터로 처리 ( 예: ?status=true )
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 상품 수정 폼
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model) {

        // 수정에 필요한 정보 조회
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);

        // 수정용 폼 뷰 호출
        return "basic/editForm";
    }

    /**
     * 상품 수정 개발
     *
     * @param itemId
     * @param item
     * @return
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item) {

        itemRepository.update(itemId, item);

        // 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출
        return "redirect:/basic/items/{itemId}";
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

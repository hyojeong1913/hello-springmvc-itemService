package hello.itemservice.web.basic;

import hello.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/basic/v2/items")
@RequiredArgsConstructor
public class BasicItemControllerV2 {

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
        return "basicV2/items";
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
        return "basicV2/item";
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

        return "basicV2/addForm";
    }

//    @PostMapping("/add")
    public String addItem(
            @Validated @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            return "basicV2/addForm";
        }

        /**
         * 검증 성공
         */

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basicV2/items/{itemId}";
    }

    /**
     * 상품 등록
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV2(
            @Validated(value = SaveCheck.class) @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            return "basicV2/addForm";
        }

        /**
         * 검증 성공
         */

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/v2/items/{itemId}";
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
        return "basicV2/editForm";
    }

    /**
     * 상품 수정 개발
     *
     * @param itemId
     * @param item
     * @param bindingResult
     * @return
     */
//    @PostMapping("/{itemId}/edit")
    public String edit(
            @PathVariable long itemId,
            @Validated @ModelAttribute Item item,
            BindingResult bindingResult
    ) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            return "basicV2/editForm";
        }

        itemRepository.update(itemId, item);

        // 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출
        return "redirect:/basic/v2/items/{itemId}";
    }

    /**
     * 상품 수정
     *
     * @param itemId
     * @param item
     * @param bindingResult
     * @return
     */
    @PostMapping("/{itemId}/edit")
    public String editV2(
            @PathVariable long itemId,
            @Validated(value = UpdateCheck.class) @ModelAttribute Item item,
            BindingResult bindingResult
    ) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            return "basicV2/editForm";
        }

        itemRepository.update(itemId, item);

        // 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출
        return "redirect:/basic/v2/items/{itemId}";
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

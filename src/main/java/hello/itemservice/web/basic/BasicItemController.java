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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
    private final ItemValidator itemValidator; // ItemValidator 를 스프링 빈으로 주입 받음

    /**
     * 해당 컨트롤러에서는 검증기를 자동으로 적용
     *
     * @param dataBinder
     */
    @InitBinder
    public void init(WebDataBinder dataBinder) {

        dataBinder.addValidators(itemValidator);
    }

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
     * @param model
     * @return
     */
//    @PostMapping("/add")
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
     * BindingResult1
     * 
     * 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아두면 된다.
     *
     * BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV7(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 상품명 검증
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        // 상품 가격 검증
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }

        // 상품 수량 검증
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {

            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {

                bindingResult.addError(new ObjectError("item", "가격 * 수량 의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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
     * 오류 발생시 사용자 입력 값 유지
     *
     * public FieldError(
     *      String objectName,              // 오류가 발생한 객체 이름
     *      String field,                   // 오류 필드
     *      @Nullable Object rejectedValue, // 사용자가 입력한 값 (거절된 값)
     *      boolean bindingFailure,         // 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
     *      @Nullable String[] codes,       // 메시지 코드
     *      @Nullable Object[] arguments,   // 메시지에서 사용하는 인자
     *      @Nullable String defaultMessage // 기본 오류 메시지
     * )
     *
     * 타임리프의 th:field 는 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 FieldError 에서 보관한 값을 사용해서 값을 출력
     *
     * 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어두고
     * 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출
     * 
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV8(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 상품명 검증
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }

        // 상품 가격 검증
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }

        // 상품 수량 검증
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {

            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {

                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량 의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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
     * 오류 코드와 메시지 처리1
     *
     * 메시지 코드는 하나가 아니라 배열로 여러 값을 전달할 수 있는데, 순서대로 매칭해서 처음 매칭되는 메시지가 사용된다.
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV9(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 상품명 검증
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        // 상품 가격 검증
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        // 상품 수량 검증
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {

            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {

                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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
     * 오류 코드와 메시지 처리2
     *
     * BindingResult 는 검증해야 할 객체인 target 바로 다음에 오기 때문에 검증해야 할 객체를 알고 있다.
     *
     * BindingResult 가 제공하는 rejectValue() , reject() 를 사용하면 FieldError , ObjectError 를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다.
     *
     * void rejectValue(
     *      @Nullable String field,         // 오류 필드명
     *      String errorCode,               // 오류 코드
     *      @Nullable Object[] errorArgs,   // 오류 메시지에서 변수를 치환하기 위한 값
     *      @Nullable String defaultMessage // 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
     * );
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV10(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        // 상품명 검증
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.rejectValue("itemName", "required");
//        }
        // 위 검증 코드를 한 줄로 요약 가능하지만 공백 검증 같은 단순한 기능만 제공
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        // 상품 가격 검증
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        // 상품 수량 검증
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {

            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {

                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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
     * Validator 분리1
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV11(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // ItemValidator 를 직접 호출
        itemValidator.validate(item, bindingResult);

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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
     * Validator 분리2
     *
     * @Validated 애노테이션 : 검증기를 실행하라는 애노테이션
     *
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV12(
            @Validated @ModelAttribute Item item,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        // 검증 실패 시
        if (bindingResult.hasErrors()) {

            log.info("errors = {}", bindingResult);

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

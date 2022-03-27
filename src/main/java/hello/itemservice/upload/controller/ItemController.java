package hello.itemservice.upload.controller;

import hello.itemservice.upload.domain.Item;
import hello.itemservice.upload.domain.UploadFile;
import hello.itemservice.upload.domain.UploadItemRepository;
import hello.itemservice.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final UploadItemRepository uploadItemRepository;
    private final FileStore fileStore;

    /**
     * 등록 폼 출력
     *
     * @param form
     * @return
     */
    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {

        return "item/form";
    }

    /**
     * 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트
     *
     * @param form
     * @param redirectAttributes
     * @return
     * @throws IOException
     */
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        Item item = new Item();

        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);

        uploadItemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    /**
     * 상품 출력
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {

        Item item = uploadItemRepository.findById(id);

        model.addAttribute("item", item);

        return "item/view";
    }
}

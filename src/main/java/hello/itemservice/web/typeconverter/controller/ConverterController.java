package hello.itemservice.web.typeconverter.controller;

import hello.itemservice.web.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConverterController {

    /**
     * 뷰 템플릿에 컨버터를 적용
     *
     * @param model
     * @return
     */
    @GetMapping("/converter/view")
    public String converterView(Model model) {

        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("172.0.0.1", 8080));

        return "converter/view";
    }

    /**
     * IpPort 를 뷰 템플릿 폼에 출력
     *
     * @param model
     * @return
     */
    @GetMapping("/converter/edit")
    public String converterForm(Model model) {

        IpPort ipPort = new IpPort("172.0.0.1", 8080);

        // Form 객체를 데이터를 전달하는 폼 객체로 사용
        Form form = new Form(ipPort);
        model.addAttribute("form", form);

        return "converter/form";
    }

    /**
     * 뷰 템플릿 폼의 IpPort 정보를 받아서 출력
     *
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {

        IpPort ipPort = form.getIpPort();

        model.addAttribute("ipPort", ipPort);

        return "converter/view";
    }

    /**
     * Form 객체
     */
    @Data
    static class Form {

        private IpPort ipPort;

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }
}

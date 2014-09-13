package org.yellowbinary.server.adminui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/")
    public String listPages(Model model) {
        model.addAttribute("msg", "Hello World");
        return "page/list";
    }

}

package org.yellowbinary.server.admin.basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/page")
public class PageController {

    @RequestMapping("/")
    public ModelAndView listPages() {
        return new ModelAndView("page/list", "msg", "Hello World");
    }

}

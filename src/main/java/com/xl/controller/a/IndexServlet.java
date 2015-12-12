package com.xl.controller.a;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Shen on 2015/12/12.
 */
@Controller
@RequestMapping(value = "/")
public class IndexServlet {

    @RequestMapping()
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello 聊烧!");
        return "index";
    }
}

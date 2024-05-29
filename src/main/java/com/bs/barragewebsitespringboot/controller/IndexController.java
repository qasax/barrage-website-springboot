package com.bs.barragewebsitespringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author morou
 */
@Controller
public class IndexController {

    @RequestMapping(value = {"/","index","index.html"}, method = RequestMethod.GET)
    public String index(){
        return "index";
    }
}
 
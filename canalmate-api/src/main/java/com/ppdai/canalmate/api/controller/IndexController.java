package com.ppdai.canalmate.api.controller;

import org.springframework.stereotype.Controller;
import com.ppdai.canalmate.api.core.BaseController;


@Controller
public class IndexController extends BaseController {

  static {
    System.out.println("==========加载IndexController===========");
  }

  // @ApiOperation(value="/首页", notes="/首页")
  // @RequestMapping(value = "/index", method = RequestMethod.GET)
  // public String index(Model model) {
  // return "redirect:/index.html";
  // }

}

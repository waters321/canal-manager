package com.ppdai.canalmate.api.controller.auth;

/**
 * 菜单controller
 *
 * @author yanxd
 * 
 * @RestController
 * @EnableAutoConfiguration public class MenuController {
 * 
 * @Qualifier(value = "menuDataAccess")
 * @Autowired MenuDataAccess menuDataAccess;
 * 
 *            // 新增菜单
 * @RequestMapping(value = "/menu/insert", method = RequestMethod.POST) ResultResponse
 *                       insertMenu(MenuDto menuDto) { if (menuDto == null ||
 *                       StringUtils.isBlank(menuDto.getMenuCode()) ||
 *                       StringUtils.isBlank(menuDto.getMenuName()) ||
 *                       StringUtils.isBlank(menuDto.getSuperiorMenu()) ||
 *                       StringUtils.isBlank(menuDto.getMenuUrl())) { return new
 *                       ResultResponse(false, UserReponseEnum.FAIL.getResCode(),
 *                       "菜单编号、菜单名称、父菜单、菜单URL非空"); } return menuDataAccess.insertMenu(menuDto); }
 * 
 *                       // 删除菜单
 * @RequestMapping(value = "/menu/delete", method = RequestMethod.POST) ResultResponse
 *                       deleteMenu(MenuDto menuDto) { if (menuDto == null || menuDto.getPkMenuId()
 *                       == null) { return new ResultResponse(false,
 *                       UserReponseEnum.FAIL.getResCode(), "主键非空"); } return
 *                       menuDataAccess.deleteMenu(menuDto); }
 * 
 * 
 * @RequestMapping(value = "/menu/update", method = RequestMethod.POST) ResultResponse
 *                       updateMenu(MenuDto menuDto) { if (menuDto == null || menuDto.getPkMenuId()
 *                       == null) { return new ResultResponse(false,
 *                       UserReponseEnum.FAIL.getResCode(), "主键非空"); } return
 *                       menuDataAccess.updateMenu(menuDto); }
 * 
 *                       // 查询LIST
 * @RequestMapping(value = "/menu/selectMenu", method = RequestMethod.POST) List<MenuDto>
 *                       selectMenu(MenuDto menuDto) { if (menuDto == null) { menuDto = new
 *                       MenuDto(); } return menuDataAccess.selectMenu(menuDto); } }
 */

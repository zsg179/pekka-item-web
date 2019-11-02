package com.pekka.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pekka.item.pojo.Item;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbItemDesc;
import com.pekka.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId, Model model) {
		// 取商品的基本信息
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		// 取商品描述
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		String itemCategory = itemService.getItemCategory(tbItem.getCid());
		// 把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		model.addAttribute("itemCategory", itemCategory);
		// 返回逻辑视图
		return "item";
	}
}

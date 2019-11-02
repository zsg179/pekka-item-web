package com.pekka.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pekka.item.pojo.Item;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbItemDesc;
import com.pekka.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String strId = textMessage.getText();
			long itemId = Long.parseLong(strId);
			// 等待事务提交
			Thread.sleep(1000);
			// 根据商品id查询商品
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			// 使用freemarker生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			// 创建模板
			// 加载模板对象
			Template template = configuration.getTemplate("item.ftl");
			// 准备模板需要的数据
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDest", itemDesc);
			// 指定输出的目录及文件名
			Writer out = new FileWriter(new File(HTML_OUT_PATH + strId + ".html"));
			// 生成静态页面
			template.process(data, out);
			// 关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

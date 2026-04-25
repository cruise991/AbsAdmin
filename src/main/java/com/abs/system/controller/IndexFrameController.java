package com.abs.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.system.api.IAbsViewService;
import com.abs.system.domain.AbsViewInfo;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/indexframe")
public class IndexFrameController {
	
	
	Logger logger=LoggerFactory.getLogger(IndexFrameController.class);

	@Autowired
	private IAbsViewService viewService;

	@RequestMapping("/getviewjson")
	public JSONObject getViewJson() {
		
		logger.info("开始执行方法：getviewjson");
		try {
			List<JSONObject> list_o = new ArrayList<JSONObject>();
			String isroot="1";
			List<AbsViewInfo> list = viewService.getViewListByIsRoot(isroot);
			for (AbsViewInfo view : list) {
				JSONObject tree = new JSONObject();
				tree.put("root", view);
			
		
				List<AbsViewInfo> sublist = viewService.getViewListByParentGuid(view.getRowguid());
				if (sublist != null && sublist.size() > 0) {
					tree.put("havesub", 1);
				} else {
					tree.put("havesub", 0);
				}
				tree.put("subs", sublist);
				list_o.add(tree);
			}
			
			logger.info("结束执行方法：getviewjson");
			return BuildJsonOfObject.getJsonString(list_o, MSG.SUCCESSCODE, MSG.ok);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.fail);
		}
	}

}

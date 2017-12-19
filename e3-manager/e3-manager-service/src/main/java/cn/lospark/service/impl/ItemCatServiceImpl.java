package cn.lospark.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lospark.common.pojo.EasyUITreeNode;
import cn.lospark.mapper.TbItemCatMapper;
import cn.lospark.pojo.TbItemCat;
import cn.lospark.pojo.TbItemCatExample;
import cn.lospark.pojo.TbItemCatExample.Criteria;
import cn.lospark.service.ItemCatService;
/**
 *条目分类管理
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private TbItemCatMapper itemCateMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		// 1.根据parentId子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCateMapper.selectByExample(example);
		// 2.把列表转为 EasyUITreeNode列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbItemCat itemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()? "closed":"open");
			nodeList.add(node);
		}
		// 3.返回列表
		return nodeList;
	}

}

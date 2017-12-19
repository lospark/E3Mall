package cn.lospark.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lospark.common.pojo.EasyUITreeNode;
import cn.lospark.content.service.ContentCategoryService;
import cn.lospark.mapper.TbContentCategoryMapper;
import cn.lospark.pojo.TbContentCategory;
import cn.lospark.pojo.TbContentCategoryExample;
import cn.lospark.pojo.TbContentCategoryExample.Criteria;
import cn.lospark.utils.E3Result;
/**
 * 内容分类管理service
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> list = new ArrayList<>();
		for (TbContentCategory contentCategory : contentCategoryList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent() ? "closed":"open");
			list.add(node);
		}
		
		return list;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//1.创建tb_content_category的对象
		TbContentCategory contentCategory = new TbContentCategory();
		//2.补全属性 
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		contentCategory.setStatus(1);//1-正常，2删除
		contentCategory.setSortOrder(1);//默认排序
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//3.插入到数据库
		contentCategoryMapper.insert(contentCategory);
		//4.判断父节点的parentId属性。如果不为true 更改之
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			//保存到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//5.返回结果
		return E3Result.ok(contentCategory);
	}

	@Override
	public E3Result updateContentCategory(long id, String name) {
		//根据id查询node节点，
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//更改名称后
		if (contentCategory == null) {
			return E3Result.build(104, "没有该节点");
		}
		contentCategory.setName(name);
		//保存到数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentCategory(long id) {
		//首先判断是否为父节点| 不为父节点不允许删除
		TbContentCategory content = contentCategoryMapper.selectByPrimaryKey(id);
		if (content.getIsParent()) {
			return E3Result.build(104, "存在子节点，不允许删除！");
		}
		//删除陈成功后，查询父节点上是否还有子节点，没有将父节点设置为 false
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(content.getParentId());
		int count = contentCategoryMapper.countByExample(example);
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(content.getParentId());
		if (count == 1) {
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		contentCategoryMapper.deleteByPrimaryKey(id);
		return null;
	}

}

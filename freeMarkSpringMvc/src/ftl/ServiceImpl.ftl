package ${packagePath};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shara.common.util.page.ValidateUtil;
import com.shara.common.util.page.PageUtil;
import com.shara.common.util.page.Json;

import ${className};
import ${javaMapperName};
import ${iServiceName};

@Service
public class ${simpleServiceImplName} implements ${simpleIServiceName} {
	@Resource
	private ${simpleMapperName} ${simpleMapperName?uncap_first};
	/**
	 * 根据主键删除
	 * 
	 * @param ${pid}
	 * @return
	 */
	public Json deleteByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid}) throws Exception {
		Json json = new Json();
		int result = -1;
		result = ${simpleMapperName?uncap_first}.deleteByPrimaryKey(${pid});
		if (result > 0) {
			json.setSuccess(true);
			json.setMsg(Json.DEL_SUCCESS);
		} else {
			json.setSuccess(false);
			json.setMsg(Json.DEL_FAIL);
		}
		return json;
	}
	
	/**
	 * 根据主键批量删除
	 * 
	 * @param list
	 * @return
	 */
	public Json deleteBatchByPrimaryKey(String ${pid}) throws Exception{
		Json json = new Json();
		int result = -1;
		result = ${simpleMapperName?uncap_first}.deleteBatchByPrimaryKey(PageUtil.getIdsForList(${pid}));
		if (result > 0) {
			json.setSuccess(true);
			json.setMsg(Json.DEL_SUCCESS);
		} else {
			json.setSuccess(false);
			json.setMsg(Json.DEL_FAIL);
		}
		return json;
	}
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	public Json insertSelective(${simpleclassName} record) throws Exception{
		Json json = new Json();
		int result = -1;
		Map<String, Object> conMap = new HashMap<String, Object>();
		<#list attrs as a>
    		if (ValidateUtil.isNullAndIsStr(String.valueOf(record.get${a.field?cap_first}()))) {
			  conMap.put("${a.field}", record.get${a.field?cap_first}());
			}
    	</#list>
		//存在条件判断
		List<${simpleclassName}> list=${simpleMapperName?uncap_first}.selectByPropertyByPage(conMap);
		int isExist =0;
		if(null != list &&list.size()>0){
			isExist=list.size();
		}
		
		/**
		 * 需要封装返回json类
		 */
		if(isExist>0){
			json.setSuccess(false);
			json.setMsg("客户名称已经存在！");
		}else{
			result = ${simpleMapperName?uncap_first}.insertSelective(record);
			if (result > 0) {
				json.setSuccess(true);
				json.setMsg(Json.ADD_SUCCESS);
			} else {
				json.setSuccess(false);
				json.setMsg(Json.ADD_FAIL);
			}
		}
		return json;
	}
	
	/**
	 * 根据主键查询对象
	 * @param ${pid}
	 * @return
	 */
	public ${simpleclassName} selectByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid}) throws Exception{
		return ${simpleMapperName?uncap_first}.selectByPrimaryKey(${pid});
	}
	
    /**
     * 修改
     * @param record
     * @return
     */
	public Json updateByPrimaryKeySelective(${simpleclassName} record) throws Exception{
		Json json = new Json();
		int result = -1;
		result = ${simpleMapperName?uncap_first}.updateByPrimaryKeySelective(record);
		if (result > 0) {
			json.setSuccess(true);
			json.setMsg(Json.UPDATE_SUCCESS);
		} else {
			json.setSuccess(false);
			json.setMsg(Json.UPDATE_FAIL);
		}
		return json;
	}
	
    /**
     * 分页查询数据列表
     * @param record
     * @param page
     * @return
     */
	public Map select${simpleclassName}ByPage(${simpleclassName} record, PageUtil page) throws Exception{
		List<${simpleclassName}> list = new ArrayList<${simpleclassName}>();
		Map<String, Object> maps = new HashMap<String, Object>();
		// 判断条件 
		<#list attrs as a>
    		if (ValidateUtil.isNullAndIsStr(String.valueOf(record.get${a.field?cap_first}()))) {
			  maps.put("${a.field}", record.get${a.field?cap_first}());
			}
    	</#list>
		// 起始分页
		maps.put("startindex", page.getStartIndex());
		// 结束分页
		maps.put("maxindex", page.getMaxIndex());
		// 查询分页
		list = ${simpleMapperName?uncap_first}.selectByPropertyByPage(maps);
		// 查询总数
		int count = ${simpleMapperName?uncap_first}.selectCountByProperty(maps);
		// 公共类 - 返回分页json对象
		return PageUtil.getResultMapArray(list, count, page.getPage());
	}
	 

}
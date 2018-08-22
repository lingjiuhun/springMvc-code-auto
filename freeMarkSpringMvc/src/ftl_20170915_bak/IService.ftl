package ${packagePath};

import java.util.List;
import java.util.Map;

import com.shara.common.util.page.PageUtil;
import com.shara.common.util.page.Json;
import ${className};

public interface ${simpleIServiceName} {

	/**
	 * 根据主键删除
	 * 
	 * @param ${pid}
	 * @return
	 */
	Json deleteByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid}) throws Exception;
	
	/**
	 * 根据主键批量删除
	 * 
	 * @param list
	 * @return
	 */
	Json deleteBatchByPrimaryKey(String ${pid}) throws Exception;
	
	/**
	 * 新增
	 * @param record
	 * @return
	 */
	Json insertSelective(${simpleclassName} record) throws Exception;
	
	/**
	 * 根据主键查询对象
	 * @param ${pid}
	 * @return
	 */
	${simpleclassName} selectByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid}) throws Exception;
	
    /**
     * 修改
     * @param record
     * @return
     */
	Json updateByPrimaryKeySelective(${simpleclassName} record) throws Exception;
	
    /**
     * 分页查询数据列表
     * @param record
     * @param page
     * @return
     */
	Map select${simpleclassName}ByPage(${simpleclassName} record, PageUtil page) throws Exception;
	 

}
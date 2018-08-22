package ${packagePath};

import java.util.List;
import java.util.Map;

import ${className};

public interface ${simpleMapperName} {

	 
     /*****
     *根据主键查询
     ****/
     ${simpleclassName} selectByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid});
	
	 /****
	 *根据主键删除
	 ****/
     int deleteByPrimaryKey(<#if 'Long' = pidType>Long<#else>String</#if> ${pid});
     
     /****
	 *根据主键批量删除
	 ****/
     int deleteBatchByPrimaryKey(List<String> list);
     
     /****
	 *新增
	 ****/
     int insertSelective(${simpleclassName} record);
     
     /****
	 *根据属性分页查询
	 ****/
     List<${simpleclassName}> selectByPropertyByPage(Map map);
     
     /****
     *根据属性查询记录数
	 ****/
	 int selectCountByProperty(Map map); 
	 
	 /****
     *根据主键更新记录
	 ****/
	 int updateByPrimaryKeySelective(${simpleclassName} record);
}
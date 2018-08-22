package ${packagePath};

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.shara.common.util.page.ValidateUtil;
import com.shara.common.util.page.PageUtil;
import com.shara.common.util.page.Json;

import ${className};
import ${serviceImplName};

@Controller
public class ${simpleControllerName}{
	@Resource
	private ${simpleServiceImplName} ${simpleServiceImplName?uncap_first};
	/**
	 * ADD管理跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/${simpleclassName?uncap_first}_add", method = RequestMethod.GET)
	public String ${simpleclassName?uncap_first}AddIndex(HttpServletRequest request) {
		return "/${parentPackageName}/jsp/add_${simpleclassName?uncap_first}";
	}

	/**
	 * EDIT管理跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/${simpleclassName?uncap_first}_edit", method = RequestMethod.GET)
	public String ${simpleclassName?uncap_first}Index(HttpServletRequest request) {
		<#if "String" != pidType>
		long key = Long.parseLong(request.getParameter("${pid}"));
		<#else>
		String key = request.getParameter("${pid}");
		</#if>
		
		${simpleclassName} ${simpleclassName?uncap_first};
		try {
			${simpleclassName?uncap_first} = ${simpleServiceImplName?uncap_first}.selectByPrimaryKey(key);
			request.setAttribute("${simpleclassName?uncap_first}", ${simpleclassName?uncap_first});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/${parentPackageName}/jsp/edit_${simpleclassName?uncap_first}";
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/${simpleclassName?uncap_first}/list", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String list${simpleclassName}(HttpServletRequest request, String pagination, String ${simpleclassName?uncap_first}) {
		String json = "";
		try {
			${simpleclassName} ${simpleclassName?uncap_first}O = new ${simpleclassName}();
			// 判断是否有查询条件
			if (ValidateUtil.isNullAndIsStr(${simpleclassName?uncap_first})) {
				 ${simpleclassName?uncap_first}O = JSON.parseObject(${simpleclassName?uncap_first}, ${simpleclassName}.class);
			}
			// 条件+分页查询
			json = JSON.toJSONString(${simpleServiceImplName?uncap_first}.select${simpleclassName}ByPage(${simpleclassName?uncap_first}O,PageUtil.getPageBean(request)));
		} catch (Exception e) {
			Json result = new Json();
			result.setMsg(Json.EXCEPTION);
			result.setSuccess(false);
			json = JSONObject.toJSONString(result);
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/${simpleclassName?uncap_first}/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String add${simpleclassName}(HttpServletRequest request, String ${simpleclassName?uncap_first}) {
		Json result = new Json();
		try {
			${simpleclassName} ${simpleclassName?uncap_first}O = JSON.parseObject(${simpleclassName?uncap_first}, ${simpleclassName}.class);
			result = ${simpleServiceImplName?uncap_first}.insertSelective(${simpleclassName?uncap_first}O);
		} catch (Exception e) {
			result.setMsg(Json.EXCEPTION);
			result.setSuccess(false);
			e.printStackTrace();
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/${simpleclassName?uncap_first}/edit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String edit${simpleclassName}(HttpServletRequest request, String ${simpleclassName?uncap_first}) {
		Json result = new Json();
		${simpleclassName} ${simpleclassName?uncap_first}O = JSON.parseObject(${simpleclassName?uncap_first}, ${simpleclassName}.class);
		try {
			result = ${simpleServiceImplName?uncap_first}.updateByPrimaryKeySelective(${simpleclassName?uncap_first}O);
		} catch (Exception e) {
			result.setMsg(Json.EXCEPTION);
			result.setSuccess(false);
			e.printStackTrace();
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/${simpleclassName?uncap_first}/del", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String del${simpleclassName}(HttpServletRequest request) {
		Json result = new Json();
		String key = request.getParameter("${pid}");
		try {
			result = ${simpleServiceImplName?uncap_first}.deleteBatchByPrimaryKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg(Json.EXCEPTION);
			result.setSuccess(false);
			e.printStackTrace();
		}
		return JSONObject.toJSONString(result);
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	@RequestMapping(value = "/${simpleclassName?uncap_first}/get", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public String get${simpleclassName}(HttpServletRequest request) {
		<#if "String" != pidType>
		long key = Long.parseLong(request.getParameter("${pid}"));
		<#else>
		String key = request.getParameter("${pid}");
		</#if>
		${simpleclassName} ${simpleclassName?uncap_first};
		try {
			${simpleclassName?uncap_first} = ${simpleServiceImplName?uncap_first}.selectByPrimaryKey(key);
			request.setAttribute("${simpleclassName?uncap_first}", ${simpleclassName?uncap_first});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/${parentPackageName}/jsp/detail_${simpleclassName?uncap_first}";
	}

}
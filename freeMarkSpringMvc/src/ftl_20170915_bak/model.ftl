package ${packageName};

/****
 * ${modelNote}
 * @author 
 *
 */
public class ${modelName} implements java.io.Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7663258960444717611L;
	
	<#list propertys as a>
		
	    private ${a.propertyType} ${a.propertyName};   //${a.propertyNote} 
	</#list>
	
	
	<#list propertys as a>
		
	    /***
	    *获取${a.propertyNote}
	    ***/
	    public ${a.propertyType} get${a.propertyName?cap_first}(){
	    	return ${a.propertyName};
	    }
	    
	    /***
	    *设置${a.propertyNote}
	    ***/
	    public void set${a.propertyName?cap_first}(){
	    	this.${a.propertyName} = ${a.propertyName};
	    }
		
	</#list>
	
	
	
}
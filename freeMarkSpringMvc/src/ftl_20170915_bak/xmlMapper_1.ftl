<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${mapperName}" >
  <resultMap id="BaseResultMap" type="${modelName}" >
    <id column="${dbPid}" property="${pid}" jdbcType="BIGINT" />
    <#list attrs as a>
    <result column="${a.dbField}" property="${a.field}" jdbcType="<#if a.type='Long'>BIGINT<#else>VARCHAR</#if>" />
    </#list>
  </resultMap>
  
 <sql id="Base_Column_List" >
    <#list attrs as a>
     <#if a_has_next>
     	${a.dbField},
     <#else>
     	${a.dbField}
     </#if>
    </#list>
  </sql>
  
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Long" >
    select
		<include refid="Base_Column_List" />
    from ${tableName} 
    where  ${dbPid} =${r" #{ "}${pid}${r",jdbcType=BIGINT}"}
  </select>
  
  <delete id="deleteByPrimaryKey" parameterType="java.lang.Long" >
    delete from ${tableName} 
    where ${dbPid} = ${r" #{ "}${pid}${r",jdbcType=BIGINT}"}
  </delete>
  
  <delete id="deleteBatchByPrimaryKey" parameterType="java.util.List" >
    delete from ${tableName}
    where ${dbPid} in
    <foreach collection="list" item="${pid}" index="index"
            open="(" close=")" separator=",">
            ${r" #{ "}${pid}${r",jdbcType=BIGINT}"}
    </foreach>
  </delete>
  
  
	<insert id="insertSelective" parameterType="${modelName}">
		insert into ${tableName}
		<trim prefix="(" suffix=")" suffixOverrides=",">
		   <#list attrs as a>
			<if test="${a.field} != null">
				${a.dbField},
			</if>
		   </#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
		  <#list attrs as a>
			<if test="${a.field} != null">
				${r" #{ "}${a.field}${r",jdbcType="}<#if a.type='Long'> BIGINT <#else> VARCHAR </#if>${r"}"},
			</if>
		  </#list>	
		</trim>
		<selectKey resultType="java.lang.Long" order="AFTER"
			keyProperty="${pid}">
			SELECT LAST_INSERT_ID() AS ${dbPid}
		</selectKey>
	</insert>
   
   <select id="selectByPropertyByPage" resultMap="BaseResultMap"
		parameterType="Map">
		select
		<include refid="Base_Column_List" />
		from ${tableName}
		where 1=1
		<#list attrs as a>
		  <if test="${a.field} != null">
			AND ${a.dbField} = ${r" #{ "}${a.field}${r",jdbcType="}<#if a.type='Long'> BIGINT <#else> VARCHAR </#if>${r"}"}
		  </if>
		</#list>
		ORDER BY ${dbPid} desc
		<if test="startindex!=null and maxindex!=null">
		 ${r"	limit #{startindex},#{maxindex} "}
		</if>
	</select>
	
	 <select id="selectCountByProperty"  resultType="java.lang.Integer"
		parameterType="Map">
		select
		count(1)
		from ${tableName}
		where 1=1
		<#list attrs as a>
		  <if test="${a.field} != null">
			AND ${a.dbField} = ${r" #{ "}${a.field}${r",jdbcType="}<#if a.type='Long'> BIGINT <#else> VARCHAR </#if>${r"}"}
		  </if>
		</#list>
	</select>
	
	<update id="updateByPrimaryKeySelective" parameterType="${modelName}">
		<if test="${pid} !=null">
			update ${tableName}
			<set>
			<#list attrs as a>
     			<#if a_has_next>
     			 ${a.dbField} = ${r" #{ "}${a.field}${r",jdbcType="}<#if a.type='Long'> BIGINT <#else> VARCHAR </#if>${r"}"},
     			<#else>
     			 ${a.dbField} = ${r" #{ "}${a.field}${r",jdbcType="}<#if a.type='Long'> BIGINT <#else> VARCHAR </#if>${r"}"}
     			</#if>
    		</#list>
			</set>
			where ${dbPid} = ${r" #{ "}${pid}${r",jdbcType=BIGINT}"}
		</if>
	</update>
  
</mapper>
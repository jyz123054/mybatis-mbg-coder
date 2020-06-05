package com.test.mbg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.test.mbg.entity.TRole;
import com.test.mbg.entity.TUser;
import com.test.mbg.mapper.TRoleMapper;
import com.test.mbg.mapper.TUserMapper;

/**
 * 	
 * @author jyz
 */
public class AppTest {
	
	private SqlSessionFactory sqlSessionFactory;
	
	@Before
	public void init() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 1.读取mybatis配置文件创SqlSessionFactory
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		inputStream.close();
	}
	
	/** 查询 **/
	@Test
	public void query() {
		//面向SqlSession编程
		SqlSession openSession = sqlSessionFactory.openSession();
		//面向接口编程
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
		TUser user = mapper.selectByPrimaryKey(1);
		System.out.println(user);
		
		//上述的面向接口查询,实际是基于下面的查询进行动态代理增强
		//1、SqlSession找到执行哪一个方法；
		//2、通过这个方法，找具体执行哪一条sql语句 ；
		//3、最终通过mappper.xml的namespace + id =》 com.test.mbg.mapper.TUserMapper.selectByPrimaryKey
		//缺点：这种iBatis风格编程，存在代码和mapper文件配置的强耦合，一旦mapper文件修改，代码也必须一并修改。
		TUser user2 = openSession.selectOne("com.test.mbg.mapper.TUserMapper.selectByPrimaryKey", "1");
		System.out.println(user2);
	}
	
	/**
	 * 	嵌套结果
	 */
	@Test
	public void assiciation1() {
		SqlSession openSession = sqlSessionFactory.openSession(true);
		TUserMapper mapper = openSession.getMapper(TUserMapper.class); 
		List<TUser> selectAssocition = mapper.selectAssocition();
		System.out.println(selectAssocition);
	}
	
	/**
	 * 	嵌套查询
	 */
	@Test
	public void assiciation2() {
		SqlSession openSession = sqlSessionFactory.openSession();
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
		List<TUser> selectAssocition = mapper.selectAssocition2();
		for(TUser u:selectAssocition) {
			//不涉及子表数据，在启用延迟加载fetchType="lazy"时，不查询子表数据。
			System.out.println(u.getEmail());
			//涉及子表数据，会查询子表数据。
			//System.out.println(u.getPosition());
		}
	}
	
	/**
	 * 	一对多嵌套查询
	 */
	@Test
	public void jobs() {
		SqlSession openSession = sqlSessionFactory.openSession();
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
//		List<TUser> list = mapper.selectIncludeJobs(2);
//		for(TUser u:list) {
//			System.out.println(u);
//		}
		TUser u = mapper.selectIncludeJobs(2);
		System.out.println(u);
	}
	
	/**
	 * 	鉴别器-分项查询
	 */
	@Test
	public void discriminator() {
		SqlSession openSession = sqlSessionFactory.openSession();
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
		List<TUser> reports = mapper.selectUserHealthReport();
		System.out.println(reports);
	}
	
	/**
	 * 	多对多查询
	 */
	@Test
	public void manyToMany() {
		//嵌套结果
		SqlSession openSession = sqlSessionFactory.openSession();
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
		List<TUser> roles = mapper.selectUserRolesInfo();
		System.out.println(roles);
		System.out.println("-----嵌套结果--------");
		//嵌套查询
		TRoleMapper roleMapper = openSession.getMapper(TRoleMapper.class);
		List<TRole> userRoles = roleMapper.selectUserRolesInfo2();
		for(TRole role:userRoles) {
			//没有使用到子表数据，暂时不会加载子表
			System.out.println(role.getRoleName());
			
			//使用到子表数据，会立即加载
			//System.out.println(role.getRoleName()+" - "+role.getUsers());
		}
	}
	
	/**
	 * 	缓存查询
	 */
	@Test
	public void cache() {
		//嵌套结果
		SqlSession openSession = sqlSessionFactory.openSession();
		TUserMapper mapper = openSession.getMapper(TUserMapper.class);
		List<TUser> roles = mapper.selectUserRolesInfo();
		System.out.println(roles);
		
		List<TUser> roles2 = mapper.selectUserRolesInfo();
		System.out.println(roles2);
		
		openSession.close();
		
		openSession = sqlSessionFactory.openSession();
		mapper = openSession.getMapper(TUserMapper.class);
		List<TUser> roles3 = mapper.selectUserRolesInfo();
		System.out.println(roles3);
	}
	
	/**
	 * 	MyBatis反射工具类
	 */
	@Test
	public void myBatisObjectReflect() {
		//用于实例化对象，并且通过反射获取类信息
		ObjectFactory objFactory = new DefaultObjectFactory();
		TUser createEntity = objFactory.create(TUser.class);
		
		ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
		
		//可以通过ObjectWrapperFactory 生成BeanWrapper类
		ObjectWrapperFactory wrapperFactory = new DefaultObjectWrapperFactory();
		
		//生成对象的类信息
		MetaObject metaObject = MetaObject.forObject(createEntity, 
				objFactory, wrapperFactory, reflectorFactory);
		
		//BeanWrapper才是实际操作对象，给对象设置值的。即将实体-查询结果 进行映射。
		ObjectWrapper beanWrapper = new BeanWrapper(metaObject, createEntity);
		
		PropertyTokenizer filedName = new PropertyTokenizer("realName");
		beanWrapper.set(filedName, "aaa");
		System.out.println(createEntity);
	}
	
	/**
	 * 	MyBatis反射工具类2
	 */
	@Test
	public void myBatisObjectReflect2() {
		//用于实例化对象，并且通过反射获取类信息
		ObjectFactory objFactory = new DefaultObjectFactory();
		TUser createEntity = objFactory.create(TUser.class);
		
		ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
		//通过ReflectorFactory 可以获取关于这个类的所有信息
		//Reflector reflectNoMethod = reflectorFactory.findForClass(ReflectNoMethod.class);
		
		//可以通过ObjectWrapperFactory 生成BeanWrapper类
		ObjectWrapperFactory wrapperFactory = new DefaultObjectWrapperFactory();
		
		//生成对象的类信息
		MetaObject metaObject = MetaObject.forObject(createEntity, 
				objFactory, wrapperFactory, reflectorFactory);
		
		//不允许此方式生成 ObjectWrapper wrapperFor = wrapperFactory.getWrapperFor(metaObject, createEntity);
		//BeanWrapper才是实际操作对象，给对象设置值的。即将实体-查询结果 进行映射。
		ObjectWrapper beanWrapper = new BeanWrapper(metaObject, createEntity);
		
		PropertyTokenizer filedName = new PropertyTokenizer("realName");
		beanWrapper.set(filedName, "aaa");
		System.out.println(createEntity);
	}
	
	/**
	 * 	从一个 Java 程序 使用 XML 配置文件逆向生成代码
	 * @throws FileNotFoundException
	 */
	@Test
	public void mybatisGeneratorTest() throws FileNotFoundException{
		List<String> warnings = new ArrayList<String>();  
        boolean overwrite = true;
        String genCfg = "generatorConfig.xml";  
        File configFile = new File(getClass().getClassLoader().getResource(genCfg).getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);  
        Configuration config = null;  
        try {  
            config = cp.parseConfiguration(configFile);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (XMLParserException e) {  
            e.printStackTrace();  
        }  
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);  
        MyBatisGenerator myBatisGenerator = null;  
        try {  
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);  
        } catch (InvalidConfigurationException e) {  
            e.printStackTrace();  
        }  
        try {  
            myBatisGenerator.generate(null);  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }
	
}

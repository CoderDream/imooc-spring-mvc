# imooc-spring-mvc
imooc-spring-mvc


Spring MVC起步

[http://www.imooc.com/learn/47](http://www.imooc.com/learn/47)

简介：Spring MVC为我们提供了一个基于组件和松耦合的MVC实现框架。在使用Java中其它MVC框架多年之后，面对Spring MVC有一种相见恨晚的感觉。Spring MVC是如此的优雅，轻盈与简洁， 让人从其它框架的桎梏解脱出来。本课程将带你步入Spring MVC。

第1章 MVC简介 
----------

1-1 Spring MVC起步课程简介 (01:29)

1-2 前端控制器 (03:50)

1-3 MVC概念 (05:05)

第2章 Spring MVC中的基本概念 
----------

2-1 Spring MVC的静态概念 (06:59)

2-2 Spring MVC的动态概念 (07:11)

第3章 配置Maven环境 
----------

3-1 Maven介绍 (07:12)

3-2 Maven的安装 (03:24)

3-3 Maven的配置 (03:57)

3-4 用Maven创建项目 (05:10)

3-5 Hello Spring MVC (12:51) [代码结构](https://github.com/CoderDream/imooc-spring-mvc/blob/master/v0.3.5.md)

执行结果：

![](https://raw.githubusercontent.com/CoderDream/imooc-spring-mvc/master/snapshot/imooc_spring_mvc_030501.png)

步骤：	

	1. 右击需要执行maven命令的工程，选择"Debug As"或"Run As"，再选择"Maven build..."
	2. Goals:输入我们需要执行的maven命令，一次执行多个命令用空格隔开。


Eclipse中执行maven命令

[http://blog.csdn.net/u011939453/article/details/43017865](http://blog.csdn.net/u011939453/article/details/43017865)


第4章 Spring MVC实操 
----------

4-1 从配置文件开始 (07:53)

4-2 Controller-基础代码 (06:00)

### 4-3 Controller-现代方式 (08:41) ###

#### 方式一：参数方式访问 ####

http://localhost:8080/courses/view?courseId=123

	// 本方法将处理 /courses/view?courseId=123 形式的URL
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId,
			Model model) {
		log.debug("In viewCourse, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		model.addAttribute(course);
		return "course_overview";
	}

console：

	8180 [qtp117460541-14] DEBUG com.coderdream.mvcdemo.controller.CourseController  - In viewCourse, courseId = 123


#### 方式二：RESTful方式访问 ####

http://localhost:8080/courses/view2/345

	// 本方法将处理 /courses/view2/123 形式的URL
	@RequestMapping("/view2/{courseId}")
	public String viewCourse2(@PathVariable("courseId") Integer courseId,
			Map<String, Object> model) {
		log.debug("In viewCourse2, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		model.put("course", course);
		return "course_overview";
	}

console：

	40501 [qtp635001030-18] DEBUG com.coderdream.mvcdemo.controller.CourseController  - In viewCourse2, courseId = 345


### 4-4 Controller-传统方式 (04:24) ###

#### 方式三：传统的传参方式 ####

[http://localhost:8080/courses/view3?courseId=456](http://localhost:8080/courses/view3?courseId=456)

	// 本方法将处理 /courses/view3?courseId=456 形式的URL
	@RequestMapping("/view3")
	public String viewCourse3(HttpServletRequest request) {
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		log.debug("In viewCourse3, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		request.setAttribute("course", course);
		return "course_overview";
	}

console：

	26116 [qtp8136897-16] DEBUG com.coderdream.mvcdemo.controller.CourseController  - In viewCourse3, courseId = 456

### 4-5 Binding (11:51) ###

绑定name，jsp页面控件的name要与模型中的名称匹配

	// 访问路径/admin?add
	@RequestMapping(value = "/admin", method = RequestMethod.GET, params = "add")
	public String createCourse() {
		return "course_admin/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String doSave(@ModelAttribute Course course) {
		log.debug("Info of Course:");
		// 输出对象的键值对
		log.debug(ReflectionToStringBuilder.toString(course));

		// 在此进行业务操作，比如数据库持久化
		course.setCourseId(123);
		
		// 重定向
		return "redirect:view2/" + course.getCourseId();
	}


console：
	
	145206 [qtp215614514-13] DEBUG com.coderdream.mvcdemo.controller.CourseController  - com.coderdream.mvcdemo.model.Course@71440f88[courseId=<null>,title=Test课程,imgPath=<null>,learningNum=<null>,duration=1200,level=1,levelDesc=<null>,descr=Spring MVC,chapterList=<null>]

![](https://raw.githubusercontent.com/CoderDream/imooc-spring-mvc/master/snapshot/imooc_spring_mvc_040601.png)


### 4-6 FileUpload--单文件上传 (12:54) ###


mvc-dispatcher-servlet.xml

	<!--200*1024*1024即200M resolveLazily属性启用是为了推迟文件解析，以便捕获文件大小异常 -->
	<bean id="multipartResolver"
		class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="maxUploadSize" value="209715200" />
		<property name="defaultEncoding" value="UTF-8" />
		<property name="resolveLazily" value="true" />
	</bean>

CourseController.java

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String showUploadPage() {
		return "course_admin/file";
	}

	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
	public String doUploadFile(@RequestParam("file") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			log.debug("Process file: {}", file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					"c:\\temp\\",
					System.currentTimeMillis() + file.getOriginalFilename()));
		}
		return "success";
	}

console：

	19319 [qtp196025267-19] DEBUG com.coderdream.mvcdemo.controller.CourseController  - Process file: mvc_0001.png
	19319 [qtp196025267-19] DEBUG com.coderdream.mvcdemo.controller.CourseController  - Process file: mvc_0001.png

运行结果：

![](https://raw.githubusercontent.com/CoderDream/imooc-spring-mvc/master/snapshot/imooc_spring_mvc_040601.png)

![](https://raw.githubusercontent.com/CoderDream/imooc-spring-mvc/master/snapshot/imooc_spring_mvc_040602.png)

4-7 JSON（上） (03:41)

http://localhost:8080/courses/345

http://localhost:8080/courses/jsontype/345

4-8 JSON（中） (05:23)

4-9 JSON（下） (04:46)

http://localhost:8080/course_json.jsp?courseId=123


第5章 总结 
----------

5-1 总结 (02:44)

mediaTypes错误（Bean property 'mediaTypes' is not writable or has an invalid setter method.）

http://blog.csdn.net/csdn_terence/article/details/53888741
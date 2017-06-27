package com.coderdream.mvcdemo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.coderdream.mvcdemo.model.Course;
import com.coderdream.mvcdemo.service.CourseService;

@Controller
@RequestMapping("/courses")
// /courses/**
public class CourseController {

	private static Logger log = LoggerFactory.getLogger(CourseController.class);

	private CourseService courseService;

	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	// 本方法将处理 /courses/view?courseId=123 形式的URL
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId,
			Model model) {
		log.debug("In viewCourse, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		model.addAttribute(course);
		return "course_overview";
	}

	// 本方法将处理 /courses/view2/345 形式的URL
	@RequestMapping("/view2/{courseId}")
	public String viewCourse2(@PathVariable("courseId") Integer courseId,
			Map<String, Object> model) {
		log.debug("In viewCourse2, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		model.put("course", course);
		return "course_overview";
	}

	// 本方法将处理 /courses/view3?courseId=456 形式的URL
	@RequestMapping("/view3")
	public String viewCourse3(HttpServletRequest request) {
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		log.debug("In viewCourse3, courseId = {}", courseId);
		Course course = courseService.getCoursebyId(courseId);
		request.setAttribute("course", course);
		return "course_overview";
	}

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
}
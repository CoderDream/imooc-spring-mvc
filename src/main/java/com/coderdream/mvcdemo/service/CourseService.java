package com.coderdream.mvcdemo.service;

import org.springframework.stereotype.Service;

import com.coderdream.mvcdemo.model.Course;

@Service
public interface CourseService {

	Course getCoursebyId(Integer courseId);

}

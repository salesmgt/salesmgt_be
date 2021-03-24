package com.app.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.SchoolDTO;
import com.app.demo.models.Level;
import com.app.demo.models.Scale;
import com.app.demo.models.SchoolType;
import com.app.demo.pagination.Paging;
import com.app.demo.services.ISchoolService;

@CrossOrigin
@RestController
@RequestMapping(path = "/schools")
public class SchoolController {

	@Autowired
	private ISchoolService iSchoolService;

	// limit: so item tren 1 page
	// totalPage: tong so Paging
	// maxItem: tong so item duoc tra ra
	// offset: vi tri bat dau cua item moi page
	// totalPage: 4 (limit, maxItem)

	// data gui ve tu client: page , limit
	// data tra ra tu server cho client: totalPage, page, list<>
	// tren url co dang https://vnexpress.vn/thoi-su?page=1&limit=2 (GET)
	// totalPage = (totalItem / limits)

	/**
	 * This method excutes when client request the filter object
	 * 
	 * @param district:  'Quận 1'.
	 * @param status:    'Đang hoạt động'.
	 * @param type:      'CONG_LAP'.
	 * @param level:     'THPT'.
	 * @param scale:     'Nhỏ'.
	 * @param key:       searching key.
	 * @param page:      page position, from 0 -> totalPage, default 0.
	 * @param limit:     elements of one page, default 5.
	 * @param column:    sorting based on attribute, default id
	 * @param direction: direction of sorting, ASC/DES, default ASC.
	 * @return ResponseEntity<Paging>(schoolPage, HttpStatus.OK - 200)
	 * 
	 * @author Nguyen Hoang Gia.
	 * @version 1.0
	 * @since 3/7/2021
	 */
	@GetMapping
	public Paging<SchoolDTO> getSchoolByFilter(@RequestParam(required = false) String district,
			@RequestParam(required = false) String status, @RequestParam(required = false) SchoolType type,
			@RequestParam(required = false) Level level, @RequestParam(required = false) Scale scale,
			@RequestParam(required = false) String key, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int limit, @RequestParam(defaultValue = "id") String column,
			@RequestParam(defaultValue = "ASC") String direction) {
		Paging<SchoolDTO> schoolPage = iSchoolService.getSchoolByFilter(district, status, type, level, scale, key, page,
				limit, column, direction);
		return schoolPage;
	}

	/**
	 * This method is used to receive and send response for the school data
	 * inserting command.
	 * 
	 * @param school: schoolDTO (Object).
	 * @return success: ResponseEntity (http status 200 OK + schoolDTO). fail :
	 *         ResponseEntity (http status CONFLICT + BAD REQUEST).
	 * @author Nguyen Hoang Gia
	 */
	@PostMapping
	public String insert(@RequestBody @Valid SchoolDTO school, BindingResult bindingResult) {
		iSchoolService.insert(school);
		return "Inserting is done";
	}

	@PutMapping("/{id}")
	public String update(@RequestBody @Valid SchoolDTO school, @PathVariable String id, BindingResult bindingResult) {
		iSchoolService.update(school);
		return "Updating is done";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		iSchoolService.delete(id);
		return "Removing is done";		
	}
}

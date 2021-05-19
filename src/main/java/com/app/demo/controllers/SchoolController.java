package com.app.demo.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.Principle;
import com.app.demo.dtos.SchoolDTO;
import com.app.demo.dtos.SchoolStatusRequest;
import com.app.demo.dtos.SchoolTimelineItem;
import com.app.demo.dtos.SuggestionSalesman;
import com.app.demo.models.SchoolType;
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
	public Paging<SchoolDTO> getSchoolByFilter(
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Boolean active, @RequestParam(required = false) String type,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) String key,@RequestParam(required = false) String schoolYear, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "schoolId") String column,
			@RequestParam(defaultValue = "ASC") String direction) {
		Paging<SchoolDTO> schoolPage = iSchoolService.getSchoolByFilter(active,district, status, SchoolType.valueOfLabel(type),level, key,schoolYear, page,
				limit, column, direction);
		return schoolPage;
	}
	
	@GetMapping("/targets-creating")
	public Paging<SchoolDTO> getSchoolForTarget(
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Boolean active, @RequestParam(required = false) String type,
			@RequestParam(required = false) String level,
			@RequestParam(required = false) String key,@RequestParam(required = false) String schoolYear, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "schoolId") String column,
			@RequestParam(defaultValue = "ASC") String direction) {
		Paging<SchoolDTO> schoolPage = iSchoolService.getSchoolForTarget(district, status, type, level,schoolYear, page, limit, column, direction);
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
	 * @throws SQLIntegrityConstraintViolationException 
	 */
	@PostMapping
	public String insert(@RequestBody @Valid SchoolDTO school, BindingResult bindingResult) throws SQLIntegrityConstraintViolationException{
			
				boolean result = iSchoolService.insert(school);
				if(!result)
					throw new SQLIntegrityConstraintViolationException("duplicate key");
			
		return "Inserting is done";
			}

	@PutMapping("/{id}")
	public String update(@RequestBody @Valid SchoolDTO school, @PathVariable String id) {
		
		iSchoolService.update(id,school);
		return "Updating is done";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable @Valid String id, BindingResult bindingResult) {
		iSchoolService.delete(id);
		return "Removing is done";		
	}
	@GetMapping("/{schoolId}")
	public SchoolDTO getOne(@PathVariable String schoolId) {
		return iSchoolService.getOne(schoolId);
	}
	@PostMapping("/import")
	public String importSchool(@RequestBody List<SchoolDTO> dtos) throws SQLIntegrityConstraintViolationException {
		return iSchoolService.saveAll(dtos);
	}
	@PatchMapping("/{schoolId}")
	public String updateStatus(@RequestBody SchoolStatusRequest request, @PathVariable String schoolId) {
		iSchoolService.updateStatus(schoolId, request);
		return "Updated";
	}
	@PatchMapping("/principal/{schoolId}")
	public String updatePrinciple(@PathVariable String schoolId, @RequestBody Principle request) {
		iSchoolService.updatePrinciple(schoolId, request);
		return "Updated";
	}
	
	@GetMapping("/timeline/{schoolId}")
	public List<SchoolTimelineItem> getTimeline(@PathVariable String schoolId) {
		return iSchoolService.getTimeline(schoolId);
	}
	@PostMapping("/suggestion")
	public List<SuggestionSalesman> getSuggestion(@RequestBody List<String> schoolId) {
		return iSchoolService.getSuggestion(schoolId);
	}
}

package com.app.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.demo.dtos.UserDTO;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Role;
import com.app.demo.models.Role_;
import com.app.demo.models.User;
import com.app.demo.models.PersonalActivity;
import com.app.demo.models.User_;
import com.app.demo.pagination.Paging;
import com.app.demo.repositories.RoleRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserRepository repo;

	@Override
	public UserDTO getOne(String username) {
		User entity = repo.findByUsername(username);
		UserDTO dto = Mapper.getMapper().map(entity, UserDTO.class);
		return dto;
	}

	
	@Override
	public void insert(UserDTO dto) {
		User userEntity = Mapper.getMapper().map(dto, User.class); // Map convert dto -> entity
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String encode = bcrypt.encode(dto.getPasswordHash()); // password được mã hóa bằng Bcrypt.
		userEntity.setPasswordHash(encode);
		userEntity.setActive(true);
		Role role = roleRepo.findByName(dto.getRoleName());
		userEntity.setRole(role);
		repo.save(userEntity);
	}

	/*
	 * user.setPasswordHash(bcrypt.encode(dto.getPasswordHash()));
	 * user.setAddress(dto.getAddress()); user.setBirthDate(dto.getBirthDate());
	 * user.setFullName(dto.getFullName()); user.setAvatar(dto.getAvatar());
	 * user.setEmail(dto.getEmail()); user.setEmail(dto.getEmail());
	 */
	@Override
	public void delete(String id) {
		User user = repo.findByUsername(id);
		if (user.isActive() == true) {
			user.setActive(false);
		}
	}

	private Pageable paging(int page, int limit, String column, String direction) {
		Pageable paging;
		if (direction.equalsIgnoreCase("DES")) {
			paging = PageRequest.of(page, limit, Sort.by(column).descending());
		} else {
			paging = PageRequest.of(page, limit, Sort.by(column).ascending());
		}
		return paging;
	}

	@Override
	public Paging<UserDTO> getUserByFilter(String key, int page, int limit, String column, String direction,
			boolean isActive, String role) {

		Page<User> entities = (Page<User>) repo.findAll((Specification<User>) (root, query, builder) -> {
			Join<User, Role> user_role = root.join(User_.ROLE);
			Predicate p = builder.conjunction();
			if (!ObjectUtils.isEmpty(key)) {
				Predicate username = builder.like(root.get(User_.USERNAME), "%" + key + "%");
				Predicate fullname = builder.like(root.get(User_.FULL_NAME), "%" + key + "%");
				Predicate address = builder.like(root.get(User_.ADDRESS), "%" + key + "%");
				Predicate email = builder.like(root.get(User_.EMAIL), "%" + key + "%");
				Predicate phone = builder.like(root.get(User_.PHONE), "%" + key + "%");
				p = builder.or(username, fullname, address, email, phone);
			}
			if (!ObjectUtils.isEmpty(isActive)) {
				p = builder.and(p, builder.isTrue(root.get(User_.ACTIVE)));
			}
			if (!ObjectUtils.isEmpty(role)) {
				p = builder.and(p, builder.equal(user_role.get(Role_.NAME), role));
			}
			
			return p;
		}, paging(page, limit, column, direction));
		List<UserDTO> results = new ArrayList<UserDTO>();
		Paging<UserDTO> userPage = new Paging<UserDTO>();
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				UserDTO dto = Mapper.getMapper().map(item, UserDTO.class);
				dto.setRoleName(item.getRole().getName());
				results.add(dto);
			});
			userPage.setList(results);
			userPage.setTotalElements(entities.getTotalElements());
			userPage.setTotalPage(entities.getTotalPages());
		}
		return userPage;
	}

}

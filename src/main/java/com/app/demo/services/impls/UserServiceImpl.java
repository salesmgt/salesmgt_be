package com.app.demo.services.impls;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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

import com.app.demo.dtos.Paging;
import com.app.demo.dtos.RecoverRequest;
import com.app.demo.dtos.RequestPasswordDTO;
import com.app.demo.dtos.UserDTO;
import com.app.demo.dtos.UserKPI;
import com.app.demo.emails.EmailSenderService;
import com.app.demo.mappers.Mapper;
import com.app.demo.models.Role;
import com.app.demo.models.Role_;
import com.app.demo.models.User;
import com.app.demo.models.User_;
import com.app.demo.repositories.RoleRepository;
import com.app.demo.repositories.ServiceRepository;
import com.app.demo.repositories.UserRepository;
import com.app.demo.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserRepository repo;
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private EmailSenderService email;
	
	@Override
	public UserDTO getOne(String username) {
		User entity = repo.findByUsername(username);
		UserDTO dto = Mapper.getMapper().map(entity, UserDTO.class);
		dto.setPasswordHash(null);
		return dto;
	}
	@Override
	public void update(String username, UserDTO dto) throws SQLIntegrityConstraintViolationException {
	  User entity = repo.findByUsername(username);
	  if (!ObjectUtils.isEmpty(entity)) {
		entity.setUsername(username);
		  if (!entity.getRole().getName().equalsIgnoreCase(dto.getRoleName())) {
		  Role role = roleRepo.findByName(dto.getRoleName());
		  entity.setRole(role);
		  }
		  entity.setFullName(dto.getFullName());
		  entity.setAddress(dto.getAddress());
		  entity.setActive(dto.getActive());
		  entity.setMale(dto.isMale());
		  entity.setPhone(dto.getPhone());
		  entity.setBirthDate(dto.getBirthDate());
		  entity.setLatitude(dto.getLatitude());
		  entity.setLongitude(dto.getLongitude());
		  repo.save(entity);
	  }else 
		  throw new SQLIntegrityConstraintViolationException("cant found this user");
  }
	
	@Override
	public void insert(UserDTO dto) throws SQLIntegrityConstraintViolationException {
		if(repo.existsById(dto.getUsername())) {
			throw new SQLIntegrityConstraintViolationException("Duplicate username");
		}
		User userEntity = Mapper.getMapper().map(dto, User.class); // Map convert dto -> entity
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String code=generatePassword();
		try {
			String encode = bcrypt.encode(code);
			userEntity.setPasswordHash(encode);
			userEntity.setActive(true);
			Role role = roleRepo.findByName(dto.getRoleName());
			userEntity.setRole(role);
			repo.save(userEntity);
			email.sendSimpleEmail(dto.getEmail(),dto.getFullName(), code);
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	@Override
	public void delete(String id) {
		User user = repo.findByUsername(id);
		if (user.isActive() == true) {
			user.setActive(false);
		}
	}

	private Pageable paging(int page, int limit, String column, String direction) {
		Pageable paging;
		if (direction.equalsIgnoreCase("DESC")) {
			paging = PageRequest.of(page, limit, Sort.by(column).descending());
		} else {
			paging = PageRequest.of(page, limit, Sort.by(column).ascending());
		}
		return paging;
	}

	@Override
	public Paging<UserDTO> getUserByFilter(String key, int page, int limit, String column, String direction,
			Boolean isActive, String role, String fullName) {

		Page<User> entities = (Page<User>) repo.findAll((Specification<User>) (root, query, builder) -> {
			Join<User, Role> user_role = root.join(User_.ROLE);
			Predicate p = builder.conjunction();
			if (!ObjectUtils.isEmpty(key) ) {
				Predicate username = builder.like(root.get(User_.USERNAME), "%" + key + "%");
				
				if(ObjectUtils.isEmpty(fullName)) {
				Predicate name= builder.like(root.get(User_.FULL_NAME), "%" + key + "%");
				 username = builder.or(username, name);
				}
				Predicate address = builder.like(root.get(User_.ADDRESS), "%" + key + "%");
				Predicate email = builder.like(root.get(User_.EMAIL), "%" + key + "%");
				Predicate phone = builder.like(root.get(User_.PHONE), "%" + key + "%");
				p = builder.or(username, address, email, phone);
			}
			if (!ObjectUtils.isEmpty(isActive)) {
				if(isActive)
				p = builder.and(p, builder.isTrue(root.get(User_.ACTIVE)));
				else
				p = builder.and(p, builder.isFalse(root.get(User_.ACTIVE)));
			}
			if (!ObjectUtils.isEmpty(role)) {
				p = builder.and(p, builder.equal(user_role.get(Role_.NAME), role));
			}
			if(!ObjectUtils.isEmpty(fullName)) {
				p = builder.and(p,builder.like(root.get(User_.FULL_NAME), "%" + fullName + "%"));
				
			}
			return p;
		}, paging(page, limit, column, direction));
		List<UserDTO> results = new ArrayList<UserDTO>();
		Paging<UserDTO> userPage = new Paging<UserDTO>();
		if (entities.hasContent()) {
			entities.getContent().forEach(item -> {
				UserDTO dto = Mapper.getMapper().map(item, UserDTO.class);
				dto.setRoleName(item.getRole().getName());
				dto.setPasswordHash(null);
				dto.setActive(item.isActive());
				results.add(dto);
			});
			userPage.setList(results);
			userPage.setTotalElements(entities.getTotalElements());
			userPage.setTotalPage(entities.getTotalPages());
		}
		return userPage;
	}

	@Override
	public void updateProfile(String username, String attribute, double longitude, double latitude, String value) {
		User entity = repo.findByUsername(username);
		if(!ObjectUtils.isEmpty(entity)) {
			switch (attribute) {
			case "address":
			User user = repo.getOne(username);
			user.setAddress(value);
			user.setLatitude(latitude);
			user.setLongitude(longitude);
			repo.save(user);
			break;
			case "phone":repo.updatePhone(username, value);
			break;
			case "email": repo.updateEmail(username, value);
			break;
			case "avatar": repo.updateAvatar(username, value);
			break;
			default: throw new NullPointerException("Can not update");
			}
		}
	}
	public String generatePassword() {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    return generatedString;
	}
	@Override
	public void updatePassword(String username, RequestPasswordDTO dto) throws SQLIntegrityConstraintViolationException{
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		User user = repo.findByUsername(username);
		if(!ObjectUtils.isEmpty(user)) {
			if(bcrypt.matches(dto.getOldPassword(), user.getPasswordHash())){
				String newPassword = bcrypt.encode(dto.getNewPassword());
				user.setPasswordHash(newPassword);
				repo.save(user);
			}
			else throw new SQLIntegrityConstraintViolationException("password not match");
		}else
			throw new SQLIntegrityConstraintViolationException("cant not found this user");
	}
	@Override
	public boolean validateToken(RecoverRequest user) {
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		User entity = repo .findByUsernameAndActive(user.getUsername(), true);
		if(bcrypt.matches(user.getPrivateToken(), entity.getPrivateToken())){
			return true;
		}
		return false;
	}
	
	@Override
	public void updatePasswordRecover(RecoverRequest request) {
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		User user = repo.findByUsername(request.getUsername());
		String newPassword = bcrypt.encode(request.getPassword());
		user.setPasswordHash(newPassword);
		user.setPrivateToken(null);
		repo.save(user);
	}
	public void clearToken(String username) {
		Thread countDownThread = new Thread() {
	        @Override
	        public void run() {
	                try {
	                    Thread.sleep(900000);//15 mins
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                User user = repo.getOne(username);
	                user.setPrivateToken(null);
	                repo.save(user);
	        }
	    };
	    countDownThread.start();
	}
	public void generateToken(String username) throws Exception {
		User user = repo.getOne(username);
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String generate = generatePassword();
		String encode = bcrypt.encode(generate);
		user.setPrivateToken(encode);
		email.sendToken(user.getEmail(),user.getFullName(),generate);
		repo.save(user);
		clearToken(username);
	}
	private String getCurrentYear() {
		int year = Year.now().getValue();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String yearStr;
		if (month > 4)
			yearStr = String.valueOf(year) + "-" + String.valueOf(year + 1);
		else
			yearStr = String.valueOf(year - 1) + "-" + String.valueOf(year);
		return yearStr;
	}
	public UserKPI getKPI(String username){
		List<com.app.demo.models.Service> list = serviceRepo.findByUsernameAndSchoolYear(username, "2020-2021");
		User u = repo.getOne(username);
		double rev =0;
		if(!ObjectUtils.isEmpty(list)) {
			 rev = list.stream().mapToDouble(item -> item.getPricePerSlot()*item.getSlotNumber()).sum();
		}
		UserKPI user = new UserKPI(username, u.getFullName(), u.getRole().getName(), list.size(),rev);
		return user;
	}
}

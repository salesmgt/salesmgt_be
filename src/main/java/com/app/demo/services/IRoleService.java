package com.app.demo.services;

import java.util.List;

public interface IRoleService {
	List<?> getAll();
	<T> T getOne(String id);
}

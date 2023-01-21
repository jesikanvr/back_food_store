package com.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.store.entitys.Municipalities;
import com.store.entitys.User;

//DTO (Data Transfer Object) y es un patrón de diseño diferente a DAO (Data Access Object)
//DTO permite un acceso mas seguro de los datos, por eso lo estoy usando para la entidad User
@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);

	public Optional<User> findByUserOrEmail(String username, String emial);

	public Optional<User> findByUser(String username);

	public Boolean existsByUser(String username);

	public Boolean existsByEmail(String email);

	public Optional<User> findByToken(String token);

	public List<User> findByMunicipalities(Municipalities municipalities);

	@Query(value = "SELECT u.* FROM [dbo].[users] as u JOIN [dbo].[user_address] as ua ON u.id = ua.user_id "
			+
			"WHERE ua.id = ?1", nativeQuery = true)
	public User findByAddress(Long addressId);
}

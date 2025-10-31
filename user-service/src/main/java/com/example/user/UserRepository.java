package com.example.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends ListCrudRepository<User, Long> {
}

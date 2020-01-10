package ru.c57m7a.dbr.dto

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.c57m7a.dbr.api.UserId
import ru.c57m7a.dbr.db.User

@Repository
interface UserRepository : CrudRepository<User, UserId>
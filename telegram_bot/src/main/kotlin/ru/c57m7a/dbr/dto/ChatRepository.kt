package ru.c57m7a.dbr.dto

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.c57m7a.dbr.db.vm.TChat

@Repository
interface ChatRepository : CrudRepository<TChat, String>
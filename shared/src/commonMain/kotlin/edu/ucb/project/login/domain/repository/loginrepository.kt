package edu.ucb.project.login.domain.repository

import edu.ucb.project.login.domain.model.UserModel
import edu.ucb.project.login.domain.vo.EmailVo
import edu.ucb.project.login.domain.vo.PasswordVo

interface LoginRepository {
    suspend fun login(email: EmailVo, password: PasswordVo): UserModel
}
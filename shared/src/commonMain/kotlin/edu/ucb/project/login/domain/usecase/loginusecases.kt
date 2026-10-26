package edu.ucb.project.login.domain.usecase

import edu.ucb.project.login.domain.vo.EmailVo
import edu.ucb.project.login.domain.vo.PasswordVo

class LoginUseCase {
    suspend operator fun invoke(email: EmailVo, password: PasswordVo): Result<Boolean> {
        return if (email.value == "calyr.software@gmail.com" && password.value == "123456") {
            Result.success(true)
        } else {
            Result.failure(Exception("Credential Invalid"))
        }
    }
}

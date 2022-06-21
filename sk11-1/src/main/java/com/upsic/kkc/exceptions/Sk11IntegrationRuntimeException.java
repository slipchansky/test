package com.upsic.kkc.exceptions;

public class Sk11IntegrationRuntimeException extends RuntimeException {

    /**
     * ошибка интеграции
     *
     * @param message заголовок ошибки, который виден пользователю
     * @param cause   детали ошибки
     *                подразумевается что cause.getMessage - системное описание ошибки для отображения пользователю в графе "детали"
     */
    public Sk11IntegrationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

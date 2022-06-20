package com.upsic.kkc.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public class AppConstants {
    /**
     * Текстовые константы
     */
    public static final String ROLE = "ROLE_";
    public static final String EMPTY_STR = "";
    public static final String PREFIX = "Bearer ";
    public static final String TYPE = "type";
    public static final String DC_ID_CLAIM = "dc_id";
    public static final String ZONE_OFFSET_CLAIM = "zone_offset";
    
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER = "user";
    
    public static final String FORBIDDEN = "Forbidden";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String OK = "ok";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public static class LdapAttributes {
        public static final String MAIL = "mail";
        public static final String COMPANY = "company";
        public static final String TITLE = "title";
        public static final String CN = "cn";
        public static final String GROUP = "group";
        public static final String OBJECT_CLASS = "objectClass";
        public static final String MEMBER = "member";
        public static final String SAM = "sAMAccountName";
        public static final String LOCKOUT_DURATION = "lockoutDuration";
        public static final String BUILTIN = "Builtin";
        public static final String LOCKOUT_TIME = "lockoutTime";
    }

    public static class Security {
        public static final String AUTHORITY = "authority";
        public static final String AUTHORITY_TYPE = "authority_type";
        public static final String AUTHORITY_TYPE_USER = "authority_type_user";
    }

    public static class RoleNames {
        public static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_TECH = "ROLE_TECH";
        public static final String ROLE_USER = "ROLE_USER";
    }
    
    public static class Authorities {
        public static final String SYSTEM_ROLE_VIEW = "SYSTEM_ROLE_VIEW";
        public static final String SYSTEM_ROLE_UPDATE = "SYSTEM_ROLE_UPDATE";
        public static final String LDAP_ROLES_CREATE = "LDAP_ROLES_CREATE";
        public static final String LDAP_ROLES_UPDATE = "LDAP_ROLES_UPDATE";
        public static final String LDAP_ROLES_VIEW = "LDAP_ROLES_VIEW";
        public static final String LDAP_ROLES_DELETE = "LDAP_ROLES_DELETE";
        public static final String DC_CREATE = "DC_CREATE";
        public static final String DC_UPDATE = "DC_UPDATE";
        public static final String DC_VIEW = "DC_VIEW";
        public static final String DC_DELETE = "DC_DELETE" ;
        public static final String SENSORS_CREATE = "SENSORS_CREATE";
        public static final String SENSORS_UPDATE = "SENSORS_UPDATE";
        public static final String SENSORS_VIEW = "SENSORS_VIEW";
        public static final String SENSORS_DELETE = "SENSORS_DELETE";
        public static final String METRICS_VIEW = "METRICS_VIEW";
    }
    
  /**
  * Описание возвращаемых ошибок
  */
    public static class ErrorDescriptions {
        public static final String AUTH_ERROR = "Неверные данные пользователя";
        public static final String TOKEN_DECODE_ERROR = "Ошибка в декодировании jwt-токена";
        public static final String FIELD_ACCESS_DENY = "Недостаточно прав для изменения поля: %s";
        public static final String ROLE_NOT_FOUND = "Роль не найдена";
        public static final String DC_NOT_FOUND = "Диспетчерский Центр не найден";
        public static final String LA_NOT_FOUND = "Роль LDAP не найдена";
        public static final String LA_ALREADY_ATTACHED = "Роль LDAP уже привязана к данной системной роли";
        public static final String LA_ISNT_ATTACHED = "Роль LDAP не была привязана к данной системной роли";
        public static final String AUTHORITY_NOT_FOUND = "Привилегия не найдена";
        public static final String USER_NOT_FOUND = "Пользователь не найден";
        public static final String AUTH_SPECIAL_CHARACTER_ERROR = "Неверные данные пользователя. Логин не должен содержать домен";
        public static final String DENIED_ACCESS_TO_SUBSYSTEM = "У пользователя отсутствует доступ к подсистеме ККЧ";
        public static final String INVALID_REFRESH_TOKEN = "Некорректный refresh токен";
        
        
        public static final String SK11_ADD_LUA_SCRIPT_NAME_FORMATTER = "%s, luaScript: %s";
        public static final String SK11_ADD_DETAILS_FORMATTER = "%s, Детали: %s";
        public static final String SK11_INVALID_RESULT_FORMAT = "Формат ответа от сервера СК-11 не соответствует ожидаемому";
        public static final String SK11_COMMAND_EXECUTING_FAIL = "Ошибка обработки команды на сервере СК-11";
        public static final String SK11_EXECUTING_TIMEOUT = "Превышен таймаут выполнения запроса на сервере СК-11";
        public static final String SK11_CONNECTION_ERROR = "Ошибка соединения с сервером СК-11";
        public static final String SK11_OTHER_ERROR = "Ошибка отправки запроса на сервер СК-11";
        public static final String SK11_AUTH_ERROR = "Ошибка авторизации на сервере СК-11";
        public static final String CACHE_IS_INITIALIZED_OR_OUTDATED = "Кеш инициализируется или просрочен";
        
    }
    
    public static class Cache {
        public static final String MODELS_CACHE = "modelsCache";
        public static final String ADDRESSES_CACHE = "addressesCache";
        public static final String CHILDREN_CACHE = "childrenCache";
        public static final String SK11_AUTH_CACHE = "sk11AuthCache";
        public static final String CATEGORIES_CACHE = "categoriesCache";
    }
    
    
    public class Api {
        public final static String VERSION_PREFIX = "/v1";
        public final static String API_PREFIX = "/api"+VERSION_PREFIX;
    }
    

}

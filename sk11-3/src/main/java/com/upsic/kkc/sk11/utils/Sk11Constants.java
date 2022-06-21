package com.upsic.kkc.sk11.utils;

public class Sk11Constants {

    public static class Cache {
        public static final String SK11_ADDRESS_ENTITIES_CACHE = "addressEntitiesCache";
        public static final String SK11_AUTH_CACHE = "sk11AuthCache";
    }

    public static class QCode {
        public final static Long EXTERNAL_VALID_Q_CODE = -2147483646L; //Значение посчитано верно во внешней системе: 0x80000002
        public final static Long EXTERNAL_INVALID_Q_CODE = -2147483136L; //Во внешней Недостоверность параметра расчёта: 0x80000200
        public final static Long INTERNAL_VALID_Q_CODE = 268435458L; //Значение посчитано верно во внешней системе: 0x10000002
        public final static Long INTERNAL_INVALID_Q_CODE = 268435488L; //Недостоверность параметра расчёта: 0x10000200


    }

    public static class Error {
        public static final String VALIDATION_ERROR = "Ошибка валидации входных данных";
        public static final String SK11_ADD_LUA_SCRIPT_NAME_FORMATTER = "%s, luaScript: %s";
        public static final String SK11_ADD_DETAILS_FORMATTER = "%s, Детали: %s";
        public static final String SK11_INVALID_RESULT_FORMAT = "Формат ответа от сервера СК-11 не соответствует ожидаемому";
        public static final String SK11_COMMAND_EXECUTING_FAIL = "Ошибка обработки команды на сервере СК-11";
        public static final String SK11_EXECUTING_TIMEOUT = "Превышен таймаут выполнения запроса на сервере СК-11";
        public static final String SK11_CONNECTION_ERROR = "Ошибка соединения с сервером СК-11";
        public static final String SK11_OTHER_ERROR = "Ошибка отправки запроса на сервер СК-11";
        public static final String SK11_AUTH_ERROR = "Ошибка авторизации на сервере СК-11";
        public static final String NO_ALLOWED_ADDRESSES = "Отсутствует хотя бы один доступный адрес";
        public static final String DB_SAVE_ERROR = "Ошибка сохранения в базу данных: %s";
        public static final String FULL_MINUTE_OR_SECOND_ERROR = "Минута, установленная в секундах не может быть дробной";
        public static final String SK11_NOT_HAVE_OBJECT = "Объект %s не найден в СК-11";
        public static final String SK11_NOT_HAVE_OBJECT_UDG = "Объект %s не имеет УДГ";
        public static final String SK11_ENUM_DESERIALIZE_ERROR = "Ошибка десериализации типа СК-11: %s";
        public static final String SK11_ENUM_SERIALIZE_ERROR = "Ошибка сериализации типа СК-11: %s";
        public static final String NEXTLINK_NOT_IMPLEMENTED = "Функция постраничного перехода по результату не реализована";
        public static final String ADDER_CYCLED = "При расчете сумматоров для отправки в СК-11 произошло зацикливание";
    }

    public static class LuaScriptParams {
        public final static String OBJECT_UID_ARRAY = "objectUidArray";
        public final static String OBJECT_VOLTAGE_ARRAY = "objectVoltageArray";
        public final static String MEASUREMENT_TYPES_ARRAY = "measurementTypesArray";
        public final static String MEASUREMENT_VALUE_TYPE = "measurementValueTypeUid";
        public final static String MEASUREMENT_TYPE = "measurementTypeUid";
        public final static String OPERATIONAL_AUTHORITY = "operationalAuthorityUid";
    }
}

package org.activiti.redis.exception;




/**
 * Ошибки редиса
 * 
 * @author inna
 * 
 */
public class RedisException extends BaseRedisException {
	
	/**
     * Код ошибки -ошибка сервиса 
     */
    public static final String CODE_REDIS_EXCEPTION_ERROR = "REDEER";
    /**
     * Код ошибки - ключ не найден
     */
    public static final String DATA_NOT_FOUND = "NODATA";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedisException(String prCode, String prMess) {
		super(prCode, prMess);
	}

	public RedisException(String prCode, String prMess, Throwable cause) {
		super(prCode, prMess, cause);
	}


}

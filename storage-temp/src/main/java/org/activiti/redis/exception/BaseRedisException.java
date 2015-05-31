package org.activiti.redis.exception;
public class BaseRedisException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Код ошибки - кривой входящий запрос
     */
    public static final String CODE_INVALID_REQUEST = "INVREQ";
    /**
     * Код ошибки - нет данных
     */
    public static final String CODE_NO_DATA = "NODATA";
    /**
     * Код обработки - ошибка обработки
     */
    public static final String CODE_PERROR = "PERROR";
    /**
     * Статус ошибки
     */
    public static final String STATE_ERROR = "e";

    private String prState;
    private String prCode;
    private String prMess;

    public BaseRedisException() {
        super();
    }

    public BaseRedisException(String prMess) {
        super(prMess);
        this.prMess = prMess;
    }

    public BaseRedisException(String prCode, String prMess) {
        super(prMess);
        this.prState = STATE_ERROR;
        this.prCode = prCode;
        this.prMess = prMess;
    }

    public BaseRedisException(String prState, String prCode, String prMess) {
        super(prMess);
        this.prState = prState;
        this.prCode = prCode;
        this.prMess = prMess;
    }
    
    

    public BaseRedisException(String prCode, String prMess, Throwable cause) {
		super(prMess, cause);
        this.prState = STATE_ERROR;
        this.prCode = prCode;
        this.prMess = prMess;
	}

	@Override
    public String getMessage() {
        return "[" + this.getPrCode() + "] " + this.getPrMess();
    }

    /**
     * @return the prState
     */
    public String getPrState() {
        return prState;
    }

    /**
     * @param prState the prState to set
     */
    public void setPrState(String prState) {
        this.prState = prState;
    }

    /**
     * @return the prCode
     */
    public String getPrCode() {
        return prCode;
    }

    /**
     * @param prCode the prCode to set
     */
    public void setPrCode(String prCode) {
        this.prCode = prCode;
    }

    /**
     * @return the prMess
     */
    public String getPrMess() {
        return prMess;
    }

    /**
     * @param prMess the prMess to set
     */
    public void setPrMess(String prMess) {
        this.prMess = prMess;
    }
}

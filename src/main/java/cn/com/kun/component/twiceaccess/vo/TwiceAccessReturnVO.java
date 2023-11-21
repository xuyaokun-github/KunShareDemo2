package cn.com.kun.component.twiceaccess.vo;

public class TwiceAccessReturnVO {

    /**
     * 返回类型
     * 0： 初次请求返回，返回requestId
     *
     */
    private String rtnType;

    private long waitTime;

    private String requestId;

    private String message;

    public static TwiceAccessReturnVO firstRtnVO(long waitTime, String requestId) {

        TwiceAccessReturnVO twiceAccessReturnVO = new TwiceAccessReturnVO();
        twiceAccessReturnVO.setRtnType("0");
        twiceAccessReturnVO.setWaitTime(waitTime);
        twiceAccessReturnVO.setRequestId(requestId);
        return twiceAccessReturnVO;
    }

    public static TwiceAccessReturnVO resultNotReadyVO(long waitTime, String requestId) {

        TwiceAccessReturnVO twiceAccessReturnVO = new TwiceAccessReturnVO();
        twiceAccessReturnVO.setRtnType("1");
        twiceAccessReturnVO.setMessage("Result Not Ready");
        return twiceAccessReturnVO;
    }

    public static TwiceAccessReturnVO resultResultAlreadyReturnedVO(long waitTime, String requestId) {

        TwiceAccessReturnVO twiceAccessReturnVO = new TwiceAccessReturnVO();
        twiceAccessReturnVO.setRtnType("2");
        twiceAccessReturnVO.setMessage("Result Already returned");
        return twiceAccessReturnVO;
    }

    public String getRtnType() {
        return rtnType;
    }

    public void setRtnType(String rtnType) {
        this.rtnType = rtnType;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

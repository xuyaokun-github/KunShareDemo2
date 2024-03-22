package cn.com.kun.framework.apache.skywalking.extend.blacktechnology;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.*;

/**
 * skywalking黑科技
 *
 * author:xuyaokun_kzx
 * date:2024/3/21
 * desc:
*/
public class SwExtendRunnable implements Runnable{

    private Runnable runnable;

    public SwExtendRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {

        SwExtendRunnableHolder.set(runnable);

        //模拟一个web环境
        HttpServletRequest mockRequest = buildMockRequest();
        mockRequest.setAttribute("org.springframework.web.util.UrlPathHelper.PATH", "/skywalking-extend/entry-method");
        mockRequest.setAttribute("org.springframework.web.util.ServletRequestPathUtils.PATH", new RequestPath() {
            @Override
            public PathContainer contextPath() {
                return null;
            }

            @Override
            public PathContainer pathWithinApplication() {
                return null;
            }

            @Override
            public RequestPath modifyContextPath(String contextPath) {
                return null;
            }

            @Override
            public String value() {
                return null;
            }

            @Override
            public List<Element> elements() {
                return null;
            }
        });
        HttpServletResponse mockResponse = buildMockResponse();
        //必须要放HttpServletResponse，否则skywalking拦截器会抛空指针
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest, mockResponse);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        //随便调用一个controller的方法
//            SkywalkingDemoController controller = SpringContextUtil.getBean("skywalkingDemoController");
//            try {
//                controller.emptyMethod();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            SkywalkingDemoController controller2 = new SkywalkingDemoController();
//            try {
//                controller2.emptyMethod();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        HandlerExecutionChain handler = null;
        List<HandlerMapping>  handlerMappings = DispatcherServletAccessor.getDispatcherServletAccessor().getDispatcherServlet().getHandlerMappings();
        if (handlerMappings != null) {
            for (HandlerMapping mapping : handlerMappings) {
                try {
                    handler = mapping.getHandler(mockRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (handler != null) {
                    Object obj = handler.getHandler();
                    if (obj instanceof HandlerMethod){
                        HandlerMethod handlerMethod = (HandlerMethod) obj;
                        Object bean = handlerMethod.getBean();
//                            LOGGER.info("触发getBean方法");
                        Method method = handlerMethod.getMethod();
                        try {
                            Object result = method.invoke(bean, null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }



    private HttpServletResponse buildMockResponse() {

        return new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String name) {
                return false;
            }

            @Override
            public String encodeURL(String url) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String url) {
                return null;
            }

            @Override
            public String encodeUrl(String url) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String url) {
                return null;
            }

            @Override
            public void sendError(int sc, String msg) throws IOException {

            }

            @Override
            public void sendError(int sc) throws IOException {

            }

            @Override
            public void sendRedirect(String location) throws IOException {

            }

            @Override
            public void setDateHeader(String name, long date) {

            }

            @Override
            public void addDateHeader(String name, long date) {

            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public void addHeader(String name, String value) {

            }

            @Override
            public void setIntHeader(String name, int value) {

            }

            @Override
            public void addIntHeader(String name, int value) {

            }

            @Override
            public void setStatus(int sc) {

            }

            @Override
            public void setStatus(int sc, String sm) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String charset) {

            }

            @Override
            public void setContentLength(int len) {

            }

            @Override
            public void setContentLengthLong(long len) {

            }

            @Override
            public void setContentType(String type) {

            }

            @Override
            public void setBufferSize(int size) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale loc) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
    }

    private HttpServletRequest buildMockRequest() {

        return new HttpServletRequest()
        {

            private Map<String, Object> map = new HashMap<>();

            @Override
            public Object getAttribute(String name) {
                return map.get(name);
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return "UTF-8";
            }

            @Override
            public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public long getContentLengthLong() {
                return 0;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return null;
            }

            @Override
            public String[] getParameterValues(String name) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public String getScheme() {
                return null;
            }

            @Override
            public String getServerName() {
                return null;
            }

            @Override
            public int getServerPort() {
                return 0;
            }

            @Override
            public BufferedReader getReader() throws IOException {
                return null;
            }

            @Override
            public String getRemoteAddr() {
                return null;
            }

            @Override
            public String getRemoteHost() {
                return null;
            }

            @Override
            public void setAttribute(String name, Object o) {
                map.put(name, o);
            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                return null;
            }

            @Override
            public String getRealPath(String path) {
                return null;
            }

            @Override
            public int getRemotePort() {
                return 0;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public String getLocalAddr() {
                return null;
            }

            @Override
            public int getLocalPort() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                return null;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {
                return null;
            }

            @Override
            public String getAuthType() {
                return null;
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[0];
            }

            @Override
            public long getDateHeader(String name) {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return null;
            }

            @Override
            public int getIntHeader(String name) {
                return 0;
            }

            @Override
            public String getMethod() {
                return "GET";
            }

            @Override
            public String getPathInfo() {
                return null;
            }

            @Override
            public String getPathTranslated() {
                return null;
            }

            @Override
            public String getContextPath() {
                return "/kunsharedemo27";
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getRequestedSessionId() {
                return null;
            }

            @Override
            public String getRequestURI() {
                return "/kunsharedemo27/skywalking-extend/entry-method";
            }

            @Override
            public StringBuffer getRequestURL() {
                return new StringBuffer("/kunsharedemo27/skywalking-extend/entry-method");
            }

            @Override
            public String getServletPath() {
                return "/kunsharedemo27";
            }

            @Override
            public HttpSession getSession(boolean create) {
                return null;
            }

            @Override
            public HttpSession getSession() {
                return null;
            }

            @Override
            public String changeSessionId() {
                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromUrl() {
                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
                return false;
            }

            @Override
            public void login(String username, String password) throws ServletException {

            }

            @Override
            public void logout() throws ServletException {

            }

            @Override
            public Collection<Part> getParts() throws IOException, ServletException {
                return null;
            }

            @Override
            public Part getPart(String name) throws IOException, ServletException {
                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
                return null;
            }
        };
    }


}

package com.sophon.summer.util;

import net.minidev.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebFilter("*")
public class TokenChecker extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //TODO 放行无需校验token的请求

        // 校验token
        String token=request.getHeader("token");
        Map<String, Object> resultMap=Jwt.validToken(token);
        TokenState state=TokenState.getTokenState((String)resultMap.get("state"));
        switch (state) {
            case VALID:
                //取出payload中数据,放入到request作用域中
                request.setAttribute("data", resultMap.get("data"));
                //放行
                chain.doFilter(request, response);
                break;
            case EXPIRED:
            case INVALID:
                System.out.println("无效token");
                //token过期或者无效，则输出错误信息返回给ajax
                JSONObject outputMSg=new JSONObject();
                outputMSg.put("success", false);
                outputMSg.put("msg", "您的token不合法或者过期了，请重新登陆");
                output(outputMSg.toJSONString(), response);
                break;
        }
    }

    private void output(String jsonStr, HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8;");
        PrintWriter out = response.getWriter();
        out.write(jsonStr);
        out.flush();
        out.close();

    }
}

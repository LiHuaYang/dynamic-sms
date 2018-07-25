package com.xxx.rh.sms.ui.controller;

import com.xxx.rh.sms.common.Globals;
import com.xxx.rh.sms.common.IOUtils;
import com.xxx.rh.sms.service.SMSServiceHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 短信服务接口
 *
 * @author lihy
 * @version 1.0  2018/7/1
 */
@Controller
@RequestMapping("/")
public class SMSController {

    private Logger logger = Logger.getLogger(this.getClass());

    private SMSServiceHandler smsServiceHandler = (SMSServiceHandler) Globals.getBean("smsServiceHandler");

    /**
     * 发送短信服务
     * /common/smssvr/v1/send
     * @author lihy  v1.0   2018/7/1
     */
    @RequestMapping(value = "v1/send", method = RequestMethod.POST)
    public void send(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "application/json;charset=utf-8");
        String resp = "";
        try {
            String resString = IOUtils.parseRequst(request, "utf-8");
            if(StringUtils.isBlank(resString)) {
                logger.error(String.format("请求内容为空：%s", resString));
                resp = IOUtils.getFailResp();
            }
            logger.info(String.format("请求内容：%s", resString));
            resp = smsServiceHandler.handleSendReq(resString);
        }
        catch(Exception e) {
            logger.error("短信服务异常：", e);
        }
        finally {
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (Exception e) {
                logger.error("响应时出现异常：", e);
            } finally {
                if (writer != null) {
                    writer.print(resp);
                    writer.flush();
                }
            }
        }
    }
}

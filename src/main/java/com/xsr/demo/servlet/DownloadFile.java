package com.xsr.demo.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by xs on 2017/6/12.
 */
public class DownloadFile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        String path = request.getSession().getServletContext().getRealPath(File.separator) + "tmp";
        String path =  this.getClass().getClassLoader().getResource("/").getPath() + "tmp";
        String fileName = request.getParameter("fileName");
        InputStream in = null;
        OutputStream out = null;
        try{
            in = new FileInputStream(path + "/" +fileName);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int readLength;
            while ((readLength = in.read(buffer)) > 0){
                out.write(buffer, 0, readLength);
                out.flush();
            }
        }catch (Exception ex){
            throw new RuntimeException("文件下载失败");
        }finally {
            if(out != null){
                out.close();
            }
            if(in != null){
                in.close();
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }


}

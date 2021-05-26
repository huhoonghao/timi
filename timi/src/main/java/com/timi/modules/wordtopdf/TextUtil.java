package com.timi.modules.wordtopdf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.regex.Pattern;

/**
 *
 * @Title: word 转 PDF
 * @Description:   ------------------------------------------警告-----------------------------必看-------------------------
 * 需要安装 libreoffice 软件, 这里主要说明 linux 安装
 * linux 版本: center os 7
 * 安装参考链接:
 * http://blog.csdn.net/aoshilang2249/article/details/49429135
 * http://blog.csdn.net/ljihe/article/details/77250206
 *unoconv 没有成功, 也是可以正常使用
 *
 * 需要引入依赖
 * <dependency>
 * <groupId>org.artofsolving.jodconverter</groupId>
 * <artifactId>jodconverter-core</artifactId>
 * <version>3.0-NX10</version>
 * <exclusions>
 * <exclusion>
 * <groupId>commons-io</groupId>
 * <artifactId>commons-io</artifactId>
 * </exclusion>
 * </exclusions>
 * </dependency>
 *
 * 镜像仓库   阿里云没拉下来  好像没有  这里使用这个
 *     <repositories>
 *         <repository>
 *             <id>jboss-maven2-release-repository</id>
 *             <url>http://maven.nuxeo.org/nexus/content/groups/public</url>
 *         </repository>
 *     </repositories>
 *
 * @Author hhh
 * @Date 2021/5/26 14:24
 * @Param LIBREOFFIC_HOME_WINDOWS 软件安装地址
 * @Return
 */

public class TextUtil {

    //LireOffice  安装目录
    private static String LIBREOFFIC_HOME_WINDOWS = "C:/Program Files/LibreOffice";
    private static String LIBREOFFIC_HOME_LINUX = "/usr/bin";
    public static void main(String[] args) throws IOException {
      /*  File input = new File("C:\\Users\\Desktop\\test.xlsx");
        File output = new File("C:\\Users\\Desktop\\execl.pdf");*/
        FileInputStream fileInputStream = new FileInputStream("D:123.doc");
        FileOutputStream fileOutputStream = new FileOutputStream("D:io.pdf");
        libreOffice2PDF(fileInputStream,fileOutputStream,"doc");
    }
    public static String getLibreOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return LIBREOFFIC_HOME_LINUX;
        } else if (Pattern.matches("Windows.*", osName)) {
            return LIBREOFFIC_HOME_WINDOWS;
        }
        return null;
    }
    /**
     * 转换libreoffice支持的文件为pdf
     * @param inputfile 输入文件
     * @param outputfile 输出的文件
     */
    public static void libreOffice2PDF(File inputfile, File outputfile) {
        String LibreOffice_HOME = getLibreOfficeHome();
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        // libreOffice的安装目录
        if(StringUtils.isEmpty(LibreOffice_HOME)){
            throw new RuntimeException("libreoffice_home is not found");//Exception("libreoffice_home is not found");
        }
        configuration.setOfficeHome(new File(LibreOffice_HOME));
        // 端口号
        configuration.setPortNumber(8100);
        configuration.setTaskExecutionTimeout(1000 * 60 * 25L);
        //设置任务执行超时为10分钟
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
        //设置任务队列超时为24小时
        OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.getFormatRegistry();
        try {
            converter.convert(inputfile, outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            officeManager.stop();
        }
    }
    private static File txtHandler(File file) {
        //或GBK
        String code = "gb2312";
        byte[] head = new byte[3];
        try {
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(head);
            if (head[0] == -1 && head[1] == -2) {
                code = "UTF-16";
            } else if (head[0] == -2 && head[1] == -1) {
                code = "Unicode";
            } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
                code = "UTF-8";
            }
            inputStream.close();

            System.out.println(code);
            if (code.equals("UTF-8")) {
                return file;
            }
            String str = FileUtils.readFileToString(file, code);
            FileUtils.writeStringToFile(file, str, "UTF-8");
            System.out.println("转码结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * office文件转pdf
     * @param fileInputStream 输入流
     * @param fileOutputStream 输出流
     * @param fileExt 文件后缀 doc,docx,xls,xlsx,ppt,pptx,txt
     */
    public static void libreOffice2PDF(FileInputStream fileInputStream, FileOutputStream fileOutputStream,String fileExt) throws IOException {
        if(StringUtils.isEmpty(fileExt)){
            throw new RuntimeException("error.fileExt" + fileExt);
        }
        File inputFile = null;
        switch (fileExt){
            case "doc":
                inputFile = new File("src/main/resources/tempFile/tempDoc.doc");
                break;
            case "docx":
                inputFile = new File("src/main/resources/tempFile/tempDocx.docx");
                break;
            case "xls":
                inputFile = new File("src/main/resources/tempFile/tempXls.xls");
                break;
            case "xlsx":
                inputFile = new File("src/main/resources/tempFile/tempXlss.xlsx");
                break;
            case "ppt":
                inputFile = new File("src/main/resources/tempFile/tempPpt.ppt");
                break;
            case "pptx":
                inputFile = new File("src/main/resources/tempFile/tempPptx.pptx");
                break;
            case "txt":
                inputFile = new File("src/main/resources/tempFile/tempTxt.txt");
                txtHandler(inputFile);
                break;
            default:
                throw new RuntimeException("error.fileExt" + fileExt);
        }
        byte[] byteArr = IOUtils.toByteArray(fileInputStream);
        FileUtils.writeByteArrayToFile(inputFile,byteArr);

        File outFile = new File("/tempFile/tempPdf.pdf");
        libreOffice2PDF(inputFile,outFile);
        byte[] bytes = FileUtils.readFileToByteArray(outFile);
        fileOutputStream.write(bytes);
    }
}

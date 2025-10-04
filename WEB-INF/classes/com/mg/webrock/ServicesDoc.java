package com.mg.webrock;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import com.mg.webrock.annotation.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
public class ServicesDoc
{
private static ArrayList<File> classesToScan=new ArrayList<>();
public static void main(String gg[])
{
try
{
String pathToClassesFolder=gg[0];
JOptionPane.showMessageDialog(null,"Choose the path to save your PDF file","Save PDF",JOptionPane.INFORMATION_MESSAGE);
JFileChooser jfc = new JFileChooser(new File("."));
jfc.setDialogTitle("Save PDF File");
jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
int result = jfc.showSaveDialog(null);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile = jfc.getSelectedFile();
String filePath = selectedFile.getAbsolutePath();
if (!filePath.toLowerCase().endsWith(".pdf"))
{
filePath += ".pdf";
}
traverse(pathToClassesFolder);
exportToPDF(filePath, pathToClassesFolder);
}
else
{
System.out.println("Save operation cancelled.");
}
}catch(Exception e)
{
e.printStackTrace();
}
}
private static void traverse(String folder)
{
File f=new File(folder);
File []files=f.listFiles();
for(File ff:files)
{
if(ff.isDirectory()) traverse(ff.getAbsolutePath());
else
{
if(ff.getAbsolutePath().endsWith(".class")) classesToScan.add(ff);
}
}//for loop ends here
}

public static void exportToPDF(String pdfFilePath, String pathToClassesFolder) {
    Document document = new Document(PageSize.A4, 40, 40, 40, 40);
    try {
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        document.open();

        // Fonts
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font classFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
        Font bulletFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.DARK_GRAY);
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.RED);

        // Header
        Paragraph header = new Paragraph("MG Companies", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20f);
        document.add(header);

        // Iterate over all .class files
        for (File f : classesToScan) {
            try {
                String absolutePath = f.getAbsolutePath();
                String basePackagePath = pathToClassesFolder + File.separator;
                String relativePath = absolutePath.substring(basePackagePath.length());
                String classNameWithSlashes = relativePath.substring(0, relativePath.length() - ".class".length());
                String className = classNameWithSlashes.replace('\\', '.').replace('/', '.');

                // Only process user classes

                // Load class safely (no servlet dependency issues)
                Class<?> c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                if (!c.isAnnotationPresent(com.mg.webrock.annotation.Path.class)) continue;

                // Class section header
                Paragraph classNamePara = new Paragraph("Class : " + className, classFont);
                classNamePara.setSpacingBefore(15f);
                classNamePara.setSpacingAfter(8f);
                document.add(classNamePara);

                // ---- CLASS LEVEL ANNOTATIONS ----
                String classPath = "";
                String globalMethodType = null;
                boolean globalSecured = false;

                if (c.isAnnotationPresent(com.mg.webrock.annotation.Path.class))
                    classPath = c.getAnnotation(com.mg.webrock.annotation.Path.class).value();
                if (c.isAnnotationPresent(com.mg.webrock.annotation.Get.class))
                    globalMethodType = "GET";
                if (c.isAnnotationPresent(com.mg.webrock.annotation.Post.class))
                    globalMethodType = "POST";
                if (c.isAnnotationPresent(com.mg.webrock.annotation.SecuredAccess.class))
                    globalSecured = true;

                Paragraph pathOnClass = new Paragraph("Class Path : " + classPath, bulletFont);
                pathOnClass.setSpacingAfter(5f);
                document.add(pathOnClass);

                // ---- BULLET LIST (Injections & Flags) ----
                com.itextpdf.text.List bulletList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                bulletList.setSymbolIndent(12f);

                if (c.isAnnotationPresent(com.mg.webrock.annotation.InjectApplicationDirectory.class))
                    bulletList.add(new ListItem("InjectApplicationDirectory", bulletFont));
                if (c.isAnnotationPresent(com.mg.webrock.annotation.InjectApplicationScope.class))
                    bulletList.add(new ListItem("InjectApplicationScope", bulletFont));
                if (c.isAnnotationPresent(com.mg.webrock.annotation.InjectSessionScope.class))
                    bulletList.add(new ListItem("InjectSessionScope", bulletFont));
                if (c.isAnnotationPresent(com.mg.webrock.annotation.InjectRequestScope.class))
                    bulletList.add(new ListItem("InjectRequestScope", bulletFont));

                for (Field field : c.getDeclaredFields()) {
                    if (field.isAnnotationPresent(com.mg.webrock.annotation.AutoWired.class))
                        bulletList.add(new ListItem("AutoWired : " + field.getName(), bulletFont));
                    if (field.isAnnotationPresent(com.mg.webrock.annotation.InjectRequestParameter.class))
                        bulletList.add(new ListItem("InjectRequestParameter : " + field.getName(), bulletFont));
                }

                if (c.isAnnotationPresent(com.mg.webrock.annotation.SecuredAccess.class))
                    bulletList.add(new ListItem("SecuredAccess : YES", bulletFont));

                if (bulletList.size() > 0) {
                    document.add(bulletList);
                    document.add(Chunk.NEWLINE);
                }

                // ---- TABLE FOR SERVICES ----
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(15f);
                table.setWidths(new float[]{3.5f, 2.5f, 3.5f, 2.5f, 2.5f, 2.5f, 3f});

                // Table headers
                String[] headers = {"Path", "Method", "Parameters", "Return Type", "Method Type", "ForwardTo", "Security"};
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h, tableHeaderFont));
                    cell.setBackgroundColor(new BaseColor(60, 63, 65)); // dark gray header
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPadding(6);
                    table.addCell(cell);
                }

                // ---- METHODS ----
                int rowIndex = 0;
                for (Method m : c.getDeclaredMethods()) {
                    if (!m.isAnnotationPresent(com.mg.webrock.annotation.Path.class)) continue;

                    com.mg.webrock.annotation.Path pathAnno = m.getAnnotation(com.mg.webrock.annotation.Path.class);
                    String methodPath = pathAnno.value();
                    String fullPath = classPath + methodPath;

                    // Params
                    Class<?>[] params = m.getParameterTypes();
                    StringBuilder paramStr = new StringBuilder();
                    for (int i = 0; i < params.length; i++) {
                        paramStr.append(params[i].getSimpleName());
                        if (i < params.length - 1) paramStr.append(", ");
                    }

                    String returnType = m.getReturnType().getSimpleName();

                    String methodType = globalMethodType != null ? globalMethodType : "ANY";
                    if (m.isAnnotationPresent(com.mg.webrock.annotation.Get.class)) methodType = "GET";
                    if (m.isAnnotationPresent(com.mg.webrock.annotation.Post.class)) methodType = "POST";

                    String forwardTo = "";
                    if (m.isAnnotationPresent(com.mg.webrock.annotation.Forward.class)) {
                        com.mg.webrock.annotation.Forward fwd = m.getAnnotation(com.mg.webrock.annotation.Forward.class);
                        forwardTo = fwd.value();
                    }

                    String security = globalSecured ? "Secured" : "None";
                    if (m.isAnnotationPresent(com.mg.webrock.annotation.SecuredAccess.class)) security = "Secured";

                    // Alternating row colors
                    BaseColor bgColor = (rowIndex % 2 == 0) ? BaseColor.WHITE : new BaseColor(245, 245, 245);
                    String[] values = {fullPath, m.getName(), paramStr.toString(), returnType, methodType, forwardTo, security};

                    for (String val : values) {
                        PdfPCell dataCell = new PdfPCell(new Phrase(val, tableCellFont));
                        dataCell.setBackgroundColor(bgColor);
                        dataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        dataCell.setPadding(5);
                        table.addCell(dataCell);
                    }
                    rowIndex++;
                }

                document.add(table);

                // ---- Separator line between classes ----
                LineSeparator ls = new LineSeparator();
                ls.setLineColor(BaseColor.LIGHT_GRAY);
                ls.setOffset(-2f);
                ls.setLineWidth(0.5f);
                document.add(new Chunk(ls));

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // Footer
        Paragraph footer = new Paragraph("Software By : Mridul Gehlot (CEO @ MGCompanies)", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20f);
        document.add(footer);

        document.close();
        System.out.println("PDF created successfully at " + pdfFilePath);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}

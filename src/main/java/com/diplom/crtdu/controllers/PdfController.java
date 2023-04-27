package com.diplom.crtdu.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;

@Controller
public class PdfController {

    @Autowired
    private ThymeleafViewResolver viewResolver;

    @GetMapping("/spec/portfolio-pdf")
    public void generatePdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Отключение хедера и кнопок
        request.setAttribute("header", false);
        request.setAttribute("buttons", false);

        // Создание документа PDF
        Document document = new Document(PageSize.A4);

        // Создание потока для записи PDF-документа
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        // Открытие документа
        document.open();

        // Получение контента страницы в виде HTML
       // String html = viewResolver.resolveViewName("my-page", Locale.getDefault()).render(new HashMap<>(), request, response);

        // Создание HTMLWorker для парсинга HTML-контента и добавления его в документ
        HTMLWorker htmlWorker = new HTMLWorker(document);
       // htmlWorker.parse(new StringReader(html));

        // Закрытие документа
        document.close();

        // Отправка сгенерированного PDF-документа в ответе HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=my-document.pdf");
        response.setContentLength(baos.size());
        OutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
    }
}

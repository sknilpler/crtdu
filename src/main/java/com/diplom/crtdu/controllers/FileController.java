package com.diplom.crtdu.controllers;

import com.diplom.crtdu.repo.CreativeAssociationRepository;
import com.diplom.crtdu.repo.KidRepository;
import com.diplom.crtdu.repo.KrujokRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Controller
public class FileController {

    @Autowired
    private KidRepository kidRepository;
    @Autowired
    private CreativeAssociationRepository creativeAssociationRepository;
    @Autowired
    private KrujokRepository krujokRepository;


    @GetMapping("/spec/kids-stat-load")
    public void printKidsStatistic(HttpServletResponse response) throws IOException {
        // Создание книги Excel
        Workbook workbook = new XSSFWorkbook();
        int rowNumber = 0;
// Создание листа Excel
        Sheet sheet = workbook.createSheet("Отчет");

// Создание заголовка
        Row header = sheet.createRow(4);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Справка");

// Заполнение данных таблицы
        Row row = sheet.createRow(5);
        row.createCell(0).setCellValue("ПДО (директор)");
        row.createCell(1).setCellValue("Название объединения");
        row.createCell(2).setCellValue("Всего");
        row.createCell(3).setCellValue("Мальчики (кол-во)");
        row.createCell(4).setCellValue("Девочки (кол-во)");
        row.createCell(5).setCellValue("РФ (кол-во)");
        row.createCell(6).setCellValue("РК (кол-во)");
        row.createCell(7).setCellValue("Другое гражданство");

// Заполнение данных строк таблицы
        Row dataRow = sheet.createRow(6);
        dataRow.createCell(0).setCellValue("ПДО1");
        dataRow.createCell(1).setCellValue("Объединение1");
        dataRow.createCell(2).setCellValue(2903);
        dataRow.createCell(3).setCellValue(854);
        dataRow.createCell(4).setCellValue(2049);
        dataRow.createCell(5).setCellValue(928);
        dataRow.createCell(6).setCellValue(1971);
        dataRow.createCell(7).setCellValue(4);

// Автоматическая ширина столбцов
        for(int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        // Форматирование даты для имени файла
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()));


        // Установка заголовков для ответа
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"Report_" + date + ".xlsx\"");

        // Создание файла Excel
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

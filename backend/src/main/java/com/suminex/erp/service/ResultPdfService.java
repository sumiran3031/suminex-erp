package com.suminex.erp.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.suminex.erp.entity.MarksEntry;
import com.suminex.erp.entity.SemesterResult;
import com.suminex.erp.entity.Student;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.MarksEntryRepository;
import com.suminex.erp.repository.SemesterResultRepository;
import com.suminex.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultPdfService {

    private final StudentRepository studentRepository;
    private final SemesterResultRepository semesterResultRepository;
    private final MarksEntryRepository marksEntryRepository;
    private final QrCodeService qrCodeService;

    @Value("${app.verification-base-url:http://localhost:8080/api/results/verify}")
    private String verificationBaseUrl;

    public ResultPdfService(StudentRepository studentRepository,
                            SemesterResultRepository semesterResultRepository,
                            MarksEntryRepository marksEntryRepository,
                            QrCodeService qrCodeService) {
        this.studentRepository = studentRepository;
        this.semesterResultRepository = semesterResultRepository;
        this.marksEntryRepository = marksEntryRepository;
        this.qrCodeService = qrCodeService;
    }

    public byte[] generateResultPdf(Long studentId, Long semesterId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        SemesterResult semesterResult = semesterResultRepository.findByStudentIdAndSemesterId(studentId, semesterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No calculated semester result found. Calculate SGPA first."));

        List<MarksEntry> entries = marksEntryRepository.findByStudentId(studentId).stream()
                .filter(e -> e.getSubjectOffering().getSemester().getId().equals(semesterId))
                .collect(Collectors.toList());

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(byteStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);

            // Register the watermark handler BEFORE any page is added — it fires
            // automatically every time a new page finishes being created.
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkEventHandler());

            Document document = new Document(pdfDoc);

            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
            PdfFont regularFont = PdfFontFactory.createFont("Helvetica");

            Paragraph collegeName = new Paragraph("SumiNex Institute of Technology")
                    .setFont(boldFont).setFontSize(18).setTextAlignment(TextAlignment.CENTER);
            document.add(collegeName);

            Paragraph subtitle = new Paragraph("Semester Result Statement")
                    .setFont(regularFont).setFontSize(12).setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(subtitle);

            Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth().setMarginBottom(15);
            addDetailRow(detailsTable, "Student Name:", student.getFirstName() + " " + student.getLastName(), regularFont, boldFont);
            addDetailRow(detailsTable, "Roll Number:", student.getRollNumber(), regularFont, boldFont);
            addDetailRow(detailsTable, "PRN:", student.getPrn() != null ? student.getPrn() : "-", regularFont, boldFont);
            addDetailRow(detailsTable, "Semester:", String.valueOf(semesterResult.getSemester().getSemesterNumber()), regularFont, boldFont);
            document.add(detailsTable);

            Table marksTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1, 1, 1, 1}))
                    .useAllAvailableWidth().setMarginBottom(15);

            String[] headers = {"Subject", "Internal", "External", "Practical", "Total", "Grade", "GP"};
            for (String header : headers) {
                marksTable.addHeaderCell(new Cell().add(new Paragraph(header).setFont(boldFont))
                        .setBackgroundColor(new DeviceRgb(230, 230, 230)));
            }

            for (MarksEntry entry : entries) {
                marksTable.addCell(new Cell().add(new Paragraph(entry.getSubjectOffering().getSubject().getName()).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getInternalMarks())).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getExternalMarks())).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getPracticalMarks())).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getTotal())).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(entry.getGrade()).setFont(regularFont)));
                marksTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getGradePoint())).setFont(regularFont)));
            }
            document.add(marksTable);

            Paragraph sgpaLine = new Paragraph("SGPA: " + semesterResult.getSgpa())
                    .setFont(boldFont).setFontSize(14).setMarginBottom(20);
            document.add(sgpaLine);

            String verificationUrl = verificationBaseUrl + "?studentId=" + studentId + "&semesterId=" + semesterId;
            byte[] qrBytes = qrCodeService.generateQrCode(verificationUrl, 150);
            Image qrImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(qrBytes))
                    .setWidth(100).setHeight(100);

            Paragraph qrCaption = new Paragraph("Scan to verify").setFont(regularFont).setFontSize(8);

            Table qrTable = new Table(1).useAllAvailableWidth();
            qrTable.addCell(new Cell().add(qrImage).add(qrCaption)
                    .setBorder(null).setTextAlignment(TextAlignment.CENTER));
            document.add(qrTable);

            Paragraph footer = new Paragraph(
                    "This is a computer-generated document and does not require a physical signature. Generated on: "
                            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")))
                    .setFont(regularFont).setFontSize(8).setMarginTop(30)
                    .setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.GRAY);
            document.add(footer);

            document.close();
            return byteStream.toByteArray();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate result PDF: " + ex.getMessage(), ex);
        }
    }

    private void addDetailRow(Table table, String label, String value, PdfFont regular, PdfFont bold) {
        table.addCell(new Cell().add(new Paragraph(label).setFont(bold)).setBorder(null));
        table.addCell(new Cell().add(new Paragraph(value).setFont(regular)).setBorder(null));
    }

    /**
     * Draws a diagonal, semi-transparent "SUMINEX ERP" watermark on every page.
     * Fires automatically at the end of each page via iText's event system —
     * this is the correct pattern for watermarking regardless of page count.
     */
    private static class WatermarkEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfDocument pdfDoc = docEvent.getDocument();

            Rectangle pageSize = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page);

            PdfExtGState gState = new PdfExtGState().setFillOpacity(0.15f);
            canvas.saveState();
            canvas.setExtGState(gState);

            try {
                PdfFont font = PdfFontFactory.createFont("Helvetica-Bold");
                float x = pageSize.getWidth() / 2;
                float y = pageSize.getHeight() / 2;

                Canvas watermarkCanvas = new Canvas(canvas, pageSize);
                watermarkCanvas.showTextAligned(
                        new Paragraph("SUMINEX ERP").setFont(font).setFontSize(60)
                                .setFontColor(ColorConstants.GRAY),
                        x, y, pdfDoc.getPageNumber(page),
                        TextAlignment.CENTER,
                        com.itextpdf.layout.properties.VerticalAlignment.MIDDLE,
                        45 // rotation in degrees — diagonal watermark
                );
                watermarkCanvas.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to draw watermark", e);
            }

            canvas.restoreState();
        }
    }
}
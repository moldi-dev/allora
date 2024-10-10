package com.moldi_sams.se_project.service.implementation;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PDFGeneratorService {
    public byte[] generatePdfFromHtml(String htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            renderer.createPDF(outputStream);

            outputStream.close();

            return outputStream.toByteArray();
        }

        catch (IOException | DocumentException e) {
            throw new RuntimeException("Couldn't generate the PDF: " + e);
        }
    }
}

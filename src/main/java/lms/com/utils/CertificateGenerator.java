package lms.com.utils;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CertificateGenerator {

    public static byte[] generateCertificate(String studentName, String courseName,
                                             LocalDate startDate, LocalDate endDate) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf, PageSize.A4.rotate())) {

            document.setMargins(0, 0, 0, 0); // No margins for background drawing

            PdfCanvas canvas = new PdfCanvas(pdf.addNewPage());
            float width = PageSize.A4.rotate().getWidth();
            float height = PageSize.A4.rotate().getHeight();

            // --- Define Colors ---
            Color blueColor = new DeviceRgb(27, 43, 90); // Darker Blue from image
            Color lightBlueColor = new DeviceRgb(60, 80, 150); // Lighter Blue from image
            Color goldColor = new DeviceRgb(212, 175, 55); // Gold color
            Color textColor = new DeviceRgb(50, 50, 50); // Dark grey for general text

            // --- Background: White for top, Blue for bottom geometric shape ---
            // Draw the white background first (entire page)
            canvas.setFillColor(new DeviceRgb(255, 255, 255));
            canvas.rectangle(0, 0, width, height);
            canvas.fill();

            // Draw the blue geometric shape at the bottom
            canvas.saveState();
            canvas.setFillColor(blueColor);
            canvas.moveTo(0, height * 0.4f) // Start point for the bottom shape
                    .lineTo(0, 0)
                    .lineTo(width, 0)
                    .lineTo(width, height * 0.2f)
                    .lineTo(width * 0.7f, height * 0.45f) // Adjust these points to refine the shape
                    .lineTo(width * 0.3f, height * 0.6f)
                    .closePath()
                    .fill();

            // Optionally, add a lighter blue overlay/shape for depth (similar to the image)
            canvas.setFillColor(lightBlueColor);
            canvas.moveTo(0, height * 0.3f)
                    .lineTo(0, 0)
                    .lineTo(width * 0.5f, 0)
                    .lineTo(width * 0.6f, height * 0.25f)
                    .lineTo(width * 0.2f, height * 0.45f)
                    .closePath()
                    .fill();
            canvas.restoreState();

            // --- Gold Border ---
            canvas.saveState();
            canvas.setStrokeColor(goldColor);
            canvas.setLineWidth(2); // Thicker line for the border
            canvas.rectangle(10, 10, width - 20, height - 20); // Slightly inset from page edge
            canvas.stroke();
            canvas.restoreState();

            // --- Text Content ---
            // Define fonts - using iText's default and bold for approximation
            PdfFont greatVibesFont; // For "Certificate" and "Name Surname"
            PdfFont openSansRegular; // For general text
            PdfFont openSansBold;    // For "OF APPRECIATION" and "PROUDLY PRESENTED TO" (if we want to emulate bold)

            try {
                // To get the exact script font like in the image, you would need to
                // load a custom font file (e.g., Great Vibes, Allura).
                // For demonstration, we'll use a standard serif font and try to make it look script-like
                // or just use a default font if custom font loading is not an option.
                // For a more accurate "script" look without external font files,
                // iText's built-in fonts are limited. Using TIMES_ROMAN as a placeholder.
                greatVibesFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
                openSansRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                openSansBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                // If you have font files:
                // greatVibesFont = PdfFontFactory.createFont("path/to/GreatVibes-Regular.ttf");
                // openSansRegular = PdfFontFactory.createFont("path/to/OpenSans-Regular.ttf");
                // openSansBold = PdfFontFactory.createFont("path/to/OpenSans-Bold.ttf");

            } catch (IOException e) {
                System.err.println("Could not load font, using default: " + e.getMessage());
                greatVibesFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                openSansRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                openSansBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            }

            // Restore margins for text content to be placed within the visible area
            // These margins create the 'frame' for the text.
            document.setMargins(70, 70, 70, 70);

            // Title "Certificate"
            Paragraph certificateTitle = new Paragraph("Certificate")
                    .setFont(greatVibesFont)
                    .setFontSize(70) // Larger font size
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(height * 0.15f); // Position it higher

            // Subtitle "OF APPRECIATION"
            Paragraph ofAppreciation = new Paragraph("OF APPRECIATION")
                    .setFont(openSansRegular) // Use regular for this line as in the image
                    .setFontSize(18)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER);

            // Add the title and subtitle to the document first so their positions are calculated.
            document.add(certificateTitle);
            document.add(ofAppreciation);

            // Horizontal line below "OF APPRECIATION"
            // Now, we can try to use their actual rendered positions if available,
            // or use estimated fixed positions if not directly exposed for paragraphs that are added.
            // Since PdfCanvas operations are low-level and happen before document layout,
            // using a fixed Y coordinate based on visual estimation is generally more reliable
            // if you draw directly on the canvas without iterating rendered elements.
            float lineYPosition = height * 0.15f + 70 + 25; // Estimate based on top margin and text height
            canvas.saveState();
            canvas.setStrokeColor(goldColor); // Gold line
            canvas.setLineWidth(1.5f);
            canvas.moveTo(width / 2 - 80, lineYPosition)
                    .lineTo(width / 2 + 80, lineYPosition)
                    .stroke();
            canvas.restoreState();

            // "PROUDLY PRESENTED TO"
            Paragraph proudlyPresented = new Paragraph("PROUDLY PRESENTED TO")
                    .setFont(openSansRegular)
                    .setFontSize(14)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(40); // Space after the line

            // Student name
            Paragraph nameParagraph = new Paragraph(studentName.toUpperCase())
                    .setFont(greatVibesFont)
                    .setFontSize(48)
                    .setFontColor(blueColor) // Name is in the darker blue
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);

            // Description/Course Text
            String descriptionText = String.format("This certifies that %s has successfully completed the\n%s program from %s to %s.",
                    studentName, courseName,
                    startDate.format(DateTimeFormatter.ofPattern("dd MMMMॲ")),
                    endDate.format(DateTimeFormatter.ofPattern("dd MMMMॲ")));

            Paragraph courseDescription = new Paragraph(descriptionText)
                    .setFont(openSansRegular)
                    .setFontSize(14)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(50);

            // Add remaining main text elements to the document
            document.add(proudlyPresented);
            document.add(nameParagraph);
            document.add(courseDescription);

            // --- Signature/Footer Area (positioning relative to page bottom) ---
            // Signatures
            Paragraph signatureLeft = new Paragraph("SIGNATURE")
                    .setFont(openSansRegular)
                    .setFontSize(10)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(document.getLeftMargin(), document.getBottomMargin() + 30, 150); // Width of signature area

            Paragraph signatureRight = new Paragraph("SIGNATURE")
                    .setFont(openSansRegular)
                    .setFontSize(10)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(width - document.getRightMargin() - 150, document.getBottomMargin() + 30, 150);

            // Signature lines
            canvas.saveState();
            canvas.setStrokeColor(textColor);
            canvas.setLineWidth(0.5f);
            canvas.moveTo(document.getLeftMargin(), document.getBottomMargin() + 50)
                    .lineTo(document.getLeftMargin() + 150, document.getBottomMargin() + 50)
                    .stroke();
            canvas.moveTo(width - document.getRightMargin() - 150, document.getBottomMargin() + 50)
                    .lineTo(width - document.getRightMargin(), document.getBottomMargin() + 50)
                    .stroke();
            canvas.restoreState();

            document.add(signatureLeft);
            document.add(signatureRight);

            // --- "BEST AWARD" Badge (simulated) ---
            // For a real image, you would load an image. Here, we'll draw a simplified circle.
            float badgeCenterX = width / 2;
            float badgeCenterY = document.getBottomMargin() + 50; // Position above signatures
            float badgeRadius = 40;

            canvas.saveState();
            canvas.setFillColor(blueColor); // Dark blue for the badge
            canvas.circle(badgeCenterX, badgeCenterY, badgeRadius);
            canvas.fill();

            // Gold border for the badge
            canvas.setStrokeColor(goldColor);
            canvas.setLineWidth(2);
            canvas.circle(badgeCenterX, badgeCenterY, badgeRadius);
            canvas.stroke();

            // Text inside the badge
            canvas.beginText();
            canvas.setFontAndSize(openSansBold, 10);
            canvas.setFillColorRgb(255, 255, 255); // White text
            canvas.setTextMatrix(badgeCenterX - 25, badgeCenterY + 10); // Adjust position for "BEST"
            canvas.showText("BEST");
            canvas.setTextMatrix(badgeCenterX - 25, badgeCenterY - 5); // Adjust position for "AWARD"
            canvas.showText("AWARD");
            canvas.endText();

            // Simulate stars (simple dots)
            canvas.setFillColorRgb(255, 255, 255); // White stars
            canvas.circle(badgeCenterX - 15, badgeCenterY + 25, 1);
            canvas.fill();
            canvas.circle(badgeCenterX + 15, badgeCenterY + 25, 1);
            canvas.fill();
            canvas.circle(badgeCenterX, badgeCenterY + 30, 1);
            canvas.fill();
            canvas.circle(badgeCenterX - 20, badgeCenterY + 10, 1);
            canvas.fill();
            canvas.circle(badgeCenterX + 20, badgeCenterY + 10, 1);
            canvas.fill();

            canvas.restoreState();

        }

        return baos.toByteArray();
    }
}
package ru.academits.servlet;

import ru.academits.PhoneBook;
import ru.academits.coverter.ContactConverter;
import ru.academits.coverter.ContactValidationConverter;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;
import ru.academits.service.ContactValidation;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteListContactServlet extends HttpServlet {
    private ContactService phoneBookService = PhoneBook.phoneBookService;
    private ContactConverter contactConverter = PhoneBook.contactConverter;
    private ContactValidationConverter contactValidationConverter = PhoneBook.contactValidationConverter;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (OutputStream responseStream = resp.getOutputStream()) {
            String contactJson = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            List<Contact> contacts = contactConverter.convertListFormJson(contactJson);

            ContactValidation contactListRemoveValidation = phoneBookService.removeListContact(contacts);
            String contactListRemoveValidationJson = contactValidationConverter.convertToJson(contactListRemoveValidation);
            if (!contactListRemoveValidation.isValid()) {
                resp.setStatus(500);
            }

            responseStream.write(contactListRemoveValidationJson.getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            System.out.println("error DeleteContactServlet  POST: ");
            e.printStackTrace();
        }
    }


}

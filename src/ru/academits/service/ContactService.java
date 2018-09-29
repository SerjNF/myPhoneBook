package ru.academits.service;

import ru.academits.PhoneBook;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;
import java.util.List;


public class ContactService {
    private ContactDao contactDao = PhoneBook.contactDao;

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();
        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public ContactValidation validateContact(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);
        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Телефон должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }
        return contactValidation;
    }

    public ContactValidation addContact(Contact contact) {
        ContactValidation contactValidation = validateContact(contact);
        if (contactValidation.isValid()) {
            contactDao.add(contact);
        }
        return contactValidation;
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public ContactValidation removeContact(Contact contact) {
        return validateRemoveContact(contact);
    }

    private ContactValidation validateRemoveContact(Contact contact) {
        ContactValidation contactRemoveValidation = new ContactValidation();
        contactRemoveValidation.setValid(true);
        if(!contactDao.removeContact(contact)){
            contactRemoveValidation.setValid(false);
            contactRemoveValidation.setError("Номер не удален(отсутствует)");
        }
        return contactRemoveValidation;
    }

    public ContactValidation removeListContact(List<Contact> contact) {
        return validateRemoveListContact(contact);
    }

    private ContactValidation validateRemoveListContact(List<Contact> contact) {
        ContactValidation contactRemoveValidation = new ContactValidation();
        contactRemoveValidation.setValid(true);
        if(!contactDao.removeListContact(contact).isEmpty()){
            contactRemoveValidation.setValid(false);
            contactRemoveValidation.setError("Номер(а) не удален(ы)(отсутствуют)");
        }
        return contactRemoveValidation;
    }
}

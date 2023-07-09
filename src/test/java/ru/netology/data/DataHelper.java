package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    public static Faker faker = new Faker(new Locale("en"));

    @Value
    public static class Card {
        private String card;
    }

    @Value
    public static class Month {
        private String month;
    }

    @Value
    public static class Year {
        private String year;
    }

    @Value
    public static class Owner {
        private String owner;
    }

    @Value
    public static class CVC {
        private String cvc;
    }

    public static Card approvedCard() {
        return new Card("1111 2222 3333 4444");
    }

    public static Card approvedStatus() {
        return new Card("APPROVED");
    }

    public static Card declinedCard() {
        return new Card("5555 6666 7777 8888");
    }

    public static Card declinedStatus() {
        return new Card("DECLINED");
    }

    public static Card randomCard() {
        return new Card(faker.business().creditCardNumber());
    }

    public static Card latinCard() {
        return new Card("ssss ssss ssss ssss");
    }

    public static Card specialCharactersCard() {
        return new Card("**** **** **** ****");
    }

    public static Card shortCard() {
        return new Card("1111 2222 3333 444");
    }

    public static Card longCard() {
        return new Card("1111 2222 3333 4444 4");
    }

    public static Month validMonth() {
        String validMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        return new Month(validMonth);
    }

    public static Month invalidMonth() {
        return new Month("0");
    }

    public static Month latinMonth() {
        return new Month("SS");
    }

    public static Month specialCharactersMonth() {
        return new Month("**");
    }

    public static Month longMonth() {
        return new Month("13");
    }

    public static Year validYear() {
        String validYear = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yy"));
        return new Year(validYear);
    }

    public static Year latinYear() {
        return new Year("SS");
    }

    public static Year specialCharactersYear() {
        return new Year("**");
    }

    public static Year invalidYear() {
        String invalidYear = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        return new Year(invalidYear);
    }

    public static Year futureYear() {
        String futureYear = LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
        return new Year(futureYear);
    }

    public static Owner validOwner() {
        return new Owner(faker.name().fullName());
    }

    public static Owner cyrillicOwner() {
        Faker faker = new Faker(new Locale("ru"));
        return new Owner(faker.name().fullName());
    }

    public static Owner specialCharactersOwner() {
        return new Owner("***** ******");
    }

    public static Owner dashSurnameOwner() {
        return new Owner("Julius Caesar-Caesar");
    }

    public static Owner longOwner() {
        return new Owner("Julius CaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesar");
    }

    public static CVC validCVC() {
        return new CVC(faker.number().digits(3));
    }

    public static CVC invalidCVC() {
        return new CVC("0");
    }

    public static CVC latinCVC() {
        return new CVC("SSS");
    }

    public static CVC specialCharactersCVC() {
        return new CVC("***");
    }

    public static CVC longCVC() {
        return new CVC(faker.number().digits(4));
    }

    public static CVC shortCVC() {
        return new CVC(faker.number().digits(2));
    }

    public static Card emptyCard() {
        return new Card("");
    }

    public static Month emptyMonth() {
        return new Month("");
    }

    public static Year emptyYear() {
        return new Year("");
    }

    public static Owner emptyOwner() {
        return new Owner("");
    }

    public static CVC emptyCVC() {
        return new CVC("");
    }
}

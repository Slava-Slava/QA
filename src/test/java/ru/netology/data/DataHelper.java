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
    @RequiredArgsConstructor
    public static class Card {
        private String card;
        private String month;
        private String year;
        private String owner;
        private String CVC;

        public static String getApprovedCard() {
            return ("1111 2222 3333 4444");
        }

        public static String getApprovedStatus() {
            return ("APPROVED");
        }

        public static String getDeclinedCard() {
            return ("5555 6666 7777 8888");
        }

        public static String getDeclinedStatus() {
            return ("DECLINED");
        }

        public static String getRandomCard() {
            return (faker.business().creditCardNumber());
        }

        public static String getLatinCard() {
            return ("ssss ssss ssss ssss");
        }

        public static String getSpecialCharactersCard() {
            return ("**** **** **** ****");
        }

        public static String getShortCard() {
            return ("1111 2222 3333 444");
        }

        public static String getLongCard() {
            return ("1111 2222 3333 4444 4");
        }

        public static String getValidMonth() {
            String validMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
            return (validMonth);
        }

        public static String getEnterNullValue() {
            return ("0");
        }

        public static String getLatinLanguageValue() {
            return ("SS");
        }

        public static String getEmptyFieldValue() {
            return ("");
        }

        public static String getSpecialCharactersValue() {
            return ("**");
        }

        public static String getLongMonth() {
            return ("13");
        }

        public static String getValidYear() {
            String validYear = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yy"));
            return (validYear);
        }

        public static String getInvalidYear() {
            String invalidYear = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
            return (invalidYear);
        }

        public static String getFutureYear() {
            String futureYear = LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
            return (futureYear);
        }

        public static String getValidOwner() {
            return (faker.name().fullName());
        }

        public static String getCyrillicOwner() {
            Faker faker = new Faker(new Locale("ru"));
            return (faker.name().fullName());
        }

        public static String getSpecialCharactersOwner() {
            return ("***** ******");
        }

        public static String getDashSurnameOwner() {
            return ("Julius Caesar-Caesar");
        }

        public static String getLongOwner() {
            return ("Julius CaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesarCaesar");
        }

        public static String getValidCVC() {
            return (faker.number().digits(3));
        }

        public static String getLatinCVC() {
            return ("SSS");
        }

        public static String getSpecialCharactersCVC() {
            return ("***");
        }

        public static String getLongCVC() {
            return (faker.number().digits(4));
        }

        public static String getShortCVC() {
            return (faker.number().digits(2));
        }

    }
}
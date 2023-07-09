package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.Buy;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyTest {
    String validCard = DataHelper.approvedCard().getCard();
    String validMonth = DataHelper.validMonth().getMonth();
    String validYear = DataHelper.validYear().getYear();
    String validOwner = DataHelper.validOwner().getOwner();
    String validCvc = DataHelper.validCVC().getCvc();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanDatabase();
    }

    @Test
    void openingFormPaymentByCard() {
        var buy = new Buy();
        buy.openPaymentGate();
    }

    @Test
    void approvedCard() {
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buy.transactionApprovedBank();
        assertEquals(DataHelper.approvedStatus().getCard(), SQLHelper.buyPaymentStatus());
    }

    @Test
    void deniedCard() {
        String declinedCard = DataHelper.declinedCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buy.transactionDeniedBank();
        assertEquals(DataHelper.declinedStatus().getCard(), SQLHelper.buyPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        String card = DataHelper.randomCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.transactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        String card = DataHelper.latinCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        String card = DataHelper.specialCharactersCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        String card = DataHelper.shortCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        String card = DataHelper.longCard().getCard();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        String month = DataHelper.invalidMonth().getMonth();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        String month = DataHelper.latinMonth().getMonth();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        String month = DataHelper.specialCharactersMonth().getMonth();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInLong() {
        String month = DataHelper.longMonth().getMonth();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringYearInLatin() {
        String year = DataHelper.latinYear().getYear();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        String year = DataHelper.specialCharactersYear().getYear();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void enterLastYear() {
        String year = DataHelper.invalidYear().getYear();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        String year = DataHelper.futureYear().getYear();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        String owner = DataHelper.cyrillicOwner().getOwner();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        String owner = DataHelper.specialCharactersOwner().getOwner();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        String owner = DataHelper.dashSurnameOwner().getOwner();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        String owner = DataHelper.longOwner().getOwner();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        String cvc = DataHelper.invalidCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        String cvc = DataHelper.latinCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        String cvc = DataHelper.specialCharactersCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
        String cvc = DataHelper.longCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
        String cvc = DataHelper.shortCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        String emptyCard = DataHelper.emptyCard().getCard();
        String emptyMonth = DataHelper.emptyMonth().getMonth();
        String emptyYear = DataHelper.emptyYear().getYear();
        String emptyOwner = DataHelper.emptyOwner().getOwner();
        String emptyCvc = DataHelper.emptyCVC().getCvc();
        var buy = new Buy();
        buy.openPaymentGate();
        buy.applicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buy.blankFormError();
    }

}
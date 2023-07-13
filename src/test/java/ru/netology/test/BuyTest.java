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
    String validCard = DataHelper.Card.getApprovedCard();
    String validMonth = DataHelper.Card.getValidMonth();
    String validYear = DataHelper.Card.getValidYear();
    String validOwner = DataHelper.Card.getValidOwner();
    String validCvc = DataHelper.Card.getValidCVC();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanDatabase();
    }

    @Test
    void openingFormPaymentByCard() {
        var buy = new Buy();
        buy.checkOpenPaymentGate();
    }

    @Test
    void approvedCard() {
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionApprovedBank();
        assertEquals(DataHelper.Card.getApprovedStatus(), SQLHelper.getBuyPaymentStatus());
    }

    @Test
    void deniedCard() {
        var declinedCard = DataHelper.Card.getDeclinedCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionDeniedBank();
        assertEquals(DataHelper.Card.getDeclinedStatus(), SQLHelper.getBuyPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        var card = DataHelper.Card.getRandomCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        var card = DataHelper.Card.getLatinCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        var card = DataHelper.Card.getSpecialCharactersCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        var card = DataHelper.Card.getShortCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        var card = DataHelper.Card.getLongCard();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.setCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        var month = DataHelper.Card.getEnterNullValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        var month = DataHelper.Card.getLatinLanguageValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        var month = DataHelper.Card.getSpecialCharactersValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringMonthInLong() {
        var month = DataHelper.Card.getLongMonth();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.setMonthError();
    }

    @Test
    void enteringYearInLatin() {
        var year = DataHelper.Card.getLatinLanguageValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        var year = DataHelper.Card.getSpecialCharactersValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void enterLastYear() {
        var year = DataHelper.Card.getInvalidYear();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        var year = DataHelper.Card.getFutureYear();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.setYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        var owner = DataHelper.Card.getCyrillicOwner();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        var owner = DataHelper.Card.getSpecialCharactersOwner();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        var owner = DataHelper.Card.getDashSurnameOwner();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        var owner = DataHelper.Card.getLongOwner();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.setOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        var cvc = DataHelper.Card.getEnterNullValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        var cvc = DataHelper.Card.getLatinCVC();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        var cvc = DataHelper.Card.getSpecialCharactersCVC();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
        var cvc = DataHelper.Card.getLongCVC();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
        var cvc = DataHelper.Card.getShortCVC();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.setCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        var emptyCard = DataHelper.Card.getEmptyFieldValue();
        var emptyMonth = DataHelper.Card.getEmptyFieldValue();
        var emptyYear = DataHelper.Card.getEmptyFieldValue();
        var emptyOwner = DataHelper.Card.getEmptyFieldValue();
        var emptyCvc = DataHelper.Card.getEmptyFieldValue();
        var buy = new Buy();
        buy.checkOpenPaymentGate();
        buy.checkApplicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buy.setBlankFormError();
    }

}
package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.BuyPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyTest {
    String validCard = DataHelper.Card.getApprovedCard();
    String validMonth = DataHelper.Card.getValidMonth();
    String validYear = DataHelper.Card.getValidYear();
    String validOwner = DataHelper.Card.getValidOwner();
    //    String validCvc = DataHelper.Card.getValidCVC();
    String validCvc = DataHelper.Card.getDigitCvc(3);

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
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
    }

    @Test
    void approvedCard() {
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionApprovedBank();
        assertEquals(DataHelper.Card.getApprovedStatus(), SQLHelper.getBuyPaymentStatus());
    }

    @Test
    void deniedCard() {
        var declinedCard = DataHelper.Card.getDeclinedCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionDeniedBank();
        assertEquals(DataHelper.Card.getDeclinedStatus(), SQLHelper.getBuyPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        var card = DataHelper.Card.getRandomCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.waitEventTransactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        var card = DataHelper.Card.getLatinCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.checkCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        var card = DataHelper.Card.getSpecialCharactersCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.checkCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        var card = DataHelper.Card.getShortCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.checkCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        var card = DataHelper.Card.getLongCard();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buy.checkCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        var month = DataHelper.Card.getEnterNullValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.checkMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        var month = DataHelper.Card.getLatinLanguageValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.checkMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        var month = DataHelper.Card.getSpecialCharactersValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.checkMonthError();
    }

    @Test
    void enteringMonthInLong() {
        var month = DataHelper.Card.getLongMonth();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buy.checkMonthError();
    }

    @Test
    void enteringYearInLatin() {
        var year = DataHelper.Card.getLatinLanguageValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.checkYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        var year = DataHelper.Card.getSpecialCharactersValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.checkYearError();
    }

    @Test
    void enterLastYear() {
        var year = DataHelper.Card.getInvalidYear();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.checkExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        var year = DataHelper.Card.getFutureYear();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buy.checkYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        var owner = DataHelper.Card.getCyrillicOwner();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.checkOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        var owner = DataHelper.Card.getSpecialCharactersOwner();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.checkOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        var owner = DataHelper.Card.getDashSurnameOwner();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.checkOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        var owner = DataHelper.Card.getLongOwner();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buy.checkOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        var cvc = DataHelper.Card.getEnterNullValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.checkCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        var cvc = DataHelper.Card.getLatinCVC();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.checkCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        var cvc = DataHelper.Card.getSpecialCharactersCVC();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.checkCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
//        var cvc = DataHelper.Card.getLongCVC();
        var cvc = DataHelper.Card.getDigitCvc(4);
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.checkCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
//        var cvc = DataHelper.Card.getShortCVC();
        var cvc = DataHelper.Card.getDigitCvc(2);
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buy.checkCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        var emptyCard = DataHelper.Card.getEmptyFieldValue();
        var emptyMonth = DataHelper.Card.getEmptyFieldValue();
        var emptyYear = DataHelper.Card.getEmptyFieldValue();
        var emptyOwner = DataHelper.Card.getEmptyFieldValue();
        var emptyCvc = DataHelper.Card.getEmptyFieldValue();
        var buy = new BuyPage();
        buy.checkOpenPaymentGate();
        buy.setApplicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buy.checkBlankFormError();
    }

}
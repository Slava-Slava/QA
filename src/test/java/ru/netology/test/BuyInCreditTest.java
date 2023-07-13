package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.BuyInCredit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyInCreditTest {
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
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
    }

    @Test
    void approvedCard() {
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionApprovedBank();
        assertEquals(DataHelper.Card.getApprovedStatus(), SQLHelper.getBuyCreditPaymentStatus());
    }

    @Test
    void deniedCard() {
        var declinedCard = DataHelper.Card.getDeclinedCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionDeniedBank();
        assertEquals(DataHelper.Card.getDeclinedStatus(), SQLHelper.getBuyCreditPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        var card = DataHelper.Card.getRandomCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        var card = DataHelper.Card.getLatinCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        var card = DataHelper.Card.getSpecialCharactersCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        var card = DataHelper.Card.getShortCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        var card = DataHelper.Card.getLongCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        var month = DataHelper.Card.getEnterNullValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        var month = DataHelper.Card.getLatinLanguageValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        var month = DataHelper.Card.getSpecialCharactersValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInLong() {
        var month = DataHelper.Card.getLongMonth();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringYearInLatin() {
        var year = DataHelper.Card.getLatinLanguageValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        var year = DataHelper.Card.getSpecialCharactersValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void enterLastYear() {
        var year = DataHelper.Card.getInvalidYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        var year = DataHelper.Card.getFutureYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        var owner = DataHelper.Card.getCyrillicOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        var owner = DataHelper.Card.getSpecialCharactersOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        var owner = DataHelper.Card.getDashSurnameOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        var owner = DataHelper.Card.getLongOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        var cvc = DataHelper.Card.getEnterNullValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        var cvc = DataHelper.Card.getLatinCVC();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        var cvc = DataHelper.Card.getSpecialCharactersCVC();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
        var cvc = DataHelper.Card.getLongCVC();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
        var cvc = DataHelper.Card.getShortCVC();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        var emptyCard = DataHelper.Card.getEmptyFieldValue();
        var emptyMonth = DataHelper.Card.getEmptyFieldValue();
        var emptyYear = DataHelper.Card.getEmptyFieldValue();
        var emptyOwner = DataHelper.Card.getEmptyFieldValue();
        var emptyCvc = DataHelper.Card.getEmptyFieldValue();
        var buyInCredit = new BuyInCredit();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.checkApplicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buyInCredit.setBlankFormError();
    }

}

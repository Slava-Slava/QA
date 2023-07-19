package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.BuyInCreditPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyInCreditTest {
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
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
    }

    @Test
    void approvedCard() {
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionApprovedBank();
        assertEquals(DataHelper.Card.getApprovedStatus(), SQLHelper.getBuyCreditPaymentStatus());
    }

    @Test
    void deniedCard() {
        var declinedCard = DataHelper.Card.getDeclinedCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionDeniedBank();
        assertEquals(DataHelper.Card.getDeclinedStatus(), SQLHelper.getBuyCreditPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        var card = DataHelper.Card.getRandomCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.waitEventTransactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        var card = DataHelper.Card.getLatinCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.checkCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        var card = DataHelper.Card.getSpecialCharactersCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.checkCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        var card = DataHelper.Card.getShortCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.checkCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        var card = DataHelper.Card.getLongCard();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.checkCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        var month = DataHelper.Card.getEnterNullValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.checkMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        var month = DataHelper.Card.getLatinLanguageValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.checkMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        var month = DataHelper.Card.getSpecialCharactersValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.checkMonthError();
    }

    @Test
    void enteringMonthInLong() {
        var month = DataHelper.Card.getLongMonth();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.checkMonthError();
    }

    @Test
    void enteringYearInLatin() {
        var year = DataHelper.Card.getLatinLanguageValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.checkYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        var year = DataHelper.Card.getSpecialCharactersValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.checkYearError();
    }

    @Test
    void enterLastYear() {
        var year = DataHelper.Card.getInvalidYear();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.checkExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        var year = DataHelper.Card.getFutureYear();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.checkYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        var owner = DataHelper.Card.getCyrillicOwner();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.checkOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        var owner = DataHelper.Card.getSpecialCharactersOwner();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.checkOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        var owner = DataHelper.Card.getDashSurnameOwner();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.checkOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        var owner = DataHelper.Card.getLongOwner();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.checkOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        var cvc = DataHelper.Card.getEnterNullValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.checkCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        var cvc = DataHelper.Card.getLatinCVC();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.checkCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        var cvc = DataHelper.Card.getSpecialCharactersCVC();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.checkCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
//        var cvc = DataHelper.Card.getLongCVC();
        var cvc = DataHelper.Card.getDigitCvc(4);
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.checkCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
//        var cvc = DataHelper.Card.getShortCVC();
        var cvc = DataHelper.Card.getDigitCvc(2);
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.checkCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        var emptyCard = DataHelper.Card.getEmptyFieldValue();
        var emptyMonth = DataHelper.Card.getEmptyFieldValue();
        var emptyYear = DataHelper.Card.getEmptyFieldValue();
        var emptyOwner = DataHelper.Card.getEmptyFieldValue();
        var emptyCvc = DataHelper.Card.getEmptyFieldValue();
        var buyInCredit = new BuyInCreditPage();
        buyInCredit.checkOpenPaymentGate();
        buyInCredit.setApplicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buyInCredit.checkBlankFormError();
    }

}

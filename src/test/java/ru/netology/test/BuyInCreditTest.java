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
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
    }

    @Test
    void approvedCard() {
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.transactionApprovedBank();
        assertEquals(DataHelper.approvedStatus().getCard(), SQLHelper.buyCreditPaymentStatus());
    }

    @Test
    void deniedCard() {
        String declinedCard = DataHelper.declinedCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(declinedCard, validMonth, validYear, validOwner, validCvc);
        buyInCredit.transactionDeniedBank();
        assertEquals(DataHelper.declinedStatus().getCard(), SQLHelper.buyCreditPaymentStatus());
    }

    @Test
    void enteringRandomCardNumber() {
        String card = DataHelper.randomCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.transactionDeniedBank();
    }

    @Test
    void enteringCardNumberInLatin() {
        String card = DataHelper.latinCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberInSpecialCharacters() {
        String card = DataHelper.specialCharactersCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberShort() {
        String card = DataHelper.shortCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enteringCardNumberLong() {
        String card = DataHelper.longCard().getCard();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(card, validMonth, validYear, validOwner, validCvc);
        buyInCredit.setCardNumberError();
    }

    @Test
    void enterMonthSingleDigit() {
        String month = DataHelper.invalidMonth().getMonth();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInLetters() {
        String month = DataHelper.latinMonth().getMonth();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInSpecialCharacters() {
        String month = DataHelper.specialCharactersMonth().getMonth();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringMonthInLong() {
        String month = DataHelper.longMonth().getMonth();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, month, validYear, validOwner, validCvc);
        buyInCredit.setMonthError();
    }

    @Test
    void enteringYearInLatin() {
        String year = DataHelper.latinYear().getYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void enteringYearWithSpecialCharacters() {
        String year = DataHelper.specialCharactersYear().getYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void enterLastYear() {
        String year = DataHelper.invalidYear().getYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setExpiredCardError();
    }

    @Test
    void enterYearPlusSixYearsBeforeExpiration() {
        String year = DataHelper.futureYear().getYear();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, year, validOwner, validCvc);
        buyInCredit.setYearError();
    }

    @Test
    void cardholderInputInCyrillic() {
        String owner = DataHelper.cyrillicOwner().getOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enteringOwnerWithSpecialCharacters() {
        String owner = DataHelper.specialCharactersOwner().getOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enterOwnersLastNameThroughDash() {
        String owner = DataHelper.dashSurnameOwner().getOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void ownerInputMoreThan64Characters() {
        String owner = DataHelper.longOwner().getOwner();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, owner, validCvc);
        buyInCredit.setOwnerError();
    }

    @Test
    void enteringWrongCvcCode() {
        String cvc = DataHelper.invalidCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void enterCvcCodeInLatin() {
        String cvc = DataHelper.latinCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void enterCvcCodeInSpecialCharacters() {
        String cvc = DataHelper.specialCharactersCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void cvcCodeInput4Characters() {
        String cvc = DataHelper.longCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void cvcCodeInput2Characters() {
        String cvc = DataHelper.shortCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(validCard, validMonth, validYear, validOwner, cvc);
        buyInCredit.setCvcError();
    }

    @Test
    void submittingAnEmptyForm() {
        String emptyCard = DataHelper.emptyCard().getCard();
        String emptyMonth = DataHelper.emptyMonth().getMonth();
        String emptyYear = DataHelper.emptyYear().getYear();
        String emptyOwner = DataHelper.emptyOwner().getOwner();
        String emptyCvc = DataHelper.emptyCVC().getCvc();
        var buyInCredit = new BuyInCredit();
        buyInCredit.openPaymentGate();
        buyInCredit.applicationForm(emptyCard, emptyMonth, emptyYear, emptyOwner, emptyCvc);
        buyInCredit.blankFormError();
    }

}

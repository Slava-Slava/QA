package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class BuyInCreditPage {
    private SelenideElement paymentButton = $(byText("Купить в кредит"));
    private SelenideElement paymentByCard = $(byText("Кредит по данным карты"));
    private SelenideElement cardNumber = $(byText("Номер карты")).parent().$(".input__control");
    private SelenideElement monthValid = $(byText("Месяц")).parent().$(".input__control");
    private SelenideElement yearValid = $(byText("Год")).parent().$(".input__control");
    private SelenideElement ownerValid = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvcValid = $(byText("CVC/CVV")).parent().$(".input__control");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement cardNumberError = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement monthError = $(byText("Месяц")).parent().$(".input__sub");
    private SelenideElement yearError = $(byText("Год")).parent().$(".input__sub");
    private SelenideElement expiredCardError = $(byText("Истек срок действия карты")).parent().$(".input__sub");
    private SelenideElement ownerError = $(byText("Владелец")).parent().$(".input__sub");
    private SelenideElement cvcError = $(byText("CVC/CVV")).parent().$(".input__sub");
    private SelenideElement bankApproved = $("div.notification_status_ok");
    private SelenideElement bankDenied = $("div.notification_status_error");

    public void checkOpenPaymentGate() {
        paymentButton.click();
        paymentByCard
                .shouldBe(visible)
                .shouldHave(text("Кредит по данным карты"));
    }

    public void setApplicationForm(String card, String month, String year, String owner, String cvc) {
        cardNumber.setValue(card);
        monthValid.setValue(month);
        yearValid.setValue(year);
        ownerValid.setValue(owner);
        cvcValid.setValue(cvc);
        continueButton.click();
    }

    public void checkBlankFormError() {
        continueButton.click();
        cardNumberError.shouldBe(visible);
        monthError.shouldBe(visible);
        yearError.shouldBe(visible);
        ownerError.shouldBe(visible);
        cvcError.shouldBe(visible);
    }

    public void waitEventTransactionApprovedBank() {
        bankApproved.shouldBe(visible, Duration.ofSeconds(25));
    }

    public void waitEventTransactionDeniedBank() {
        bankDenied.shouldBe(visible, Duration.ofSeconds(25));
    }

    public void checkCardNumberError() {
        cardNumberError.shouldBe(visible);

    }

    public void checkMonthError() {
        monthError.shouldBe(visible);
    }

    public void checkYearError() {
        yearError.shouldBe(visible);
    }

    public void checkExpiredCardError() {
        expiredCardError.shouldBe(visible);
    }

    public void checkOwnerError() {
        ownerError.shouldBe(visible);
    }

    public void checkCvcError() {
        cvcError.shouldBe(visible);
    }
}

package ru.raiffeisen.cources.atm;

import org.junit.jupiter.api.*;
import ru.raiffeisen.cources.atm.model.constants.CurrencyHolder;
import ru.raiffeisen.cources.atm.model.money.Money;
import ru.raiffeisen.cources.atm.model.score.CreditScore;
import ru.raiffeisen.cources.atm.model.score.CurrentScore;
import ru.raiffeisen.cources.atm.model.score.DebetScore;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {
    private static ATM atm;

    @BeforeAll
    static void init(){
        CreditScore creditScore =
                new CreditScore(
                        new Money(0.0d,
                                CurrencyHolder.getCurrencyByName("RUR").getName()
                        ), null, 1);
        DebetScore debetScore =
                new DebetScore(
                        new Money(0.0d,
                                CurrencyHolder.getCurrencyByName("RUR").getName()
                        ), null, 1, creditScore);
        CurrentScore currentScore =
                new CurrentScore(
                        new Money(0.0d,
                                CurrencyHolder.getCurrencyByName("RUR").getName()
                        ), null, 1, debetScore);
        atm = new ATM(currentScore, debetScore, creditScore);
    }

    @BeforeEach
    void fillData(){
        Money money = new Money(1000, "RUR");
        setMoneyToCredit(money);
    }

    @Test
    void addMoneyToScore() {
        Money money = new Money(100, "RUR");
        atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
        Money moneyNew = getMoneyFromCredit();
        assertEquals(1100 , moneyNew.getValue());

        Money moneyNegative = new Money(-100, "RUR");
        boolean exceptionWere = false;
        try {
            atm.addMoneyToScore(money, ScoreTypeEnum.CREDIT);
        } catch (IllegalArgumentException ex){
            exceptionWere = true;
        }
        assertTrue(exceptionWere);
    }


    @AfterEach
    void cleanData(){
        Money money = new Money(0, "RUR");
        setMoneyToCredit(money);
    }

    private Money getMoneyFromCredit(){
        Money money = null;

        Class atmClass = atm.getClass();
        try {
            Field creditScoreField = atmClass.getDeclaredField("creditScore");
            creditScoreField.setAccessible(true);

            CreditScore creditScore = (CreditScore) creditScoreField.get(atm);
            Class scoreClass = creditScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);

            money = (Money) moneyField.get(creditScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return money;
    }

    private void setMoneyToCredit(Money money) {
        Class atmClass = atm.getClass();
        try {
            Field creditScoreField = atmClass.getDeclaredField("creditScore");
            creditScoreField.setAccessible(true);

            CreditScore creditScore = (CreditScore) creditScoreField.get(atm);
            Class scoreClass = creditScore.getClass().getSuperclass();

            Field moneyField = scoreClass.getDeclaredField("balance");
            moneyField.setAccessible(true);
            moneyField.set(creditScore, money);

            creditScoreField.set(atm, creditScore);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void cleanUp(){
        atm = null;
    }

}
package com.kmvp.billcoinexhange.app.services;

import com.kmvp.billcoinexhange.app.exceptions.InvalidValueException;
import com.kmvp.billcoinexhange.app.exceptions.NotEnoughCoinsException;
import com.kmvp.billcoinexhange.app.models.MachineState;
import com.kmvp.billcoinexhange.app.utils.ExchangeUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author kalil.peixoto
 * @date 4/4/25 10:28
 * @email kalilmvp@gmail.com
 */
@Service("exchangeBillCoinLeast")
public class ExchangeBillCoinLeast implements ExchangeBillCoin {
    private Map<Integer, Integer> coins;

    @PostConstruct
    public void init() {
        this.initializeCoins();
    }

    @Override
    public Map<Integer, Integer> execute(int amountDollars) {
        if (amountDollars <= 0) {
            throw new InvalidValueException("Value has to be greater than 0");
        }

        int amountInCents = amountDollars * 100;
        Map<Integer, Integer> result = new HashMap<>();

        for (int value : ExchangeUtils.COIN_VALUES) {
            int count = Math.min(amountInCents / value, this.getCoins().get(value));
            if (count > 0) {
                result.put(value, count);
                amountInCents -= count * value;
                this.coins.put(value, this.getCoins().get(value) - count);
            }
        }
        if (amountInCents > 0) {
            throw new NotEnoughCoinsException("Not enough coins to exchange the amount.");
        }
        return result;
    }

    @Override
    public MachineState getState(int amountDollars) {
        var coinCountResult = ExchangeUtils.calculaCoinsCount(this.getCoins());
        return MachineState.with(amountDollars, coinCountResult.coinsCount(), coinCountResult.totalCoinsAmount() / 100, (amountDollars + coinCountResult.totalCoinsAmount() / 100));
    }

    @Override
    public void initializeCoins() {
        this.coins = new HashMap<>();
        for (int value : ExchangeUtils.COIN_VALUES) {
            this.coins.put(value, 100);
        }
    }

    public Map<Integer, Integer> getCoins() {
        return coins;
    }
}

/*
 * Copyright 2021 ICONation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iconloop.score.token.irc31;

import com.iconloop.score.test.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;

public class MintBurnTest extends MultiTokenTest {

    @BeforeEach
    void setup() throws Exception {
        token_setup();
        reset(spy);
    }

    void check_balance(Account account, BigInteger id, BigInteger amount) {
        BigInteger balance = (BigInteger) score.call("balanceOf", account.getAddress(), id);
        assertEquals(amount, balance);
    }

    @Test
    void testMint() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 1));
        BigInteger newId = mint_token(supply);
        check_balance(owner, newId, supply);
    }

    @Test
    void testMintAlreadyExists() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 1));
        BigInteger newId = mint_token(supply);

        // mint with the existing id
        assertThrows(AssertionError.class, () ->
                score.invoke(owner, "mint", newId, supply, "uri"));
    }

    @Test
    void testBurnInvalidToken() {
        assertThrows(AssertionError.class, () ->
                score.invoke(owner, "burn", getTokenId(), BigInteger.ONE));
    }

    @Test
    void testBurn() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 2));
        BigInteger newId = mint_token(supply);

        // burn with creator
        BigInteger burn_amount = BigInteger.ONE;
        score.invoke(owner, "burn", newId, burn_amount);
    }

    @Test
    void testBurnAllSupply() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 2));
        BigInteger newId = mint_token(supply);

        BigInteger burn_amount = supply;
        score.invoke(owner, "burn", newId, burn_amount);
    }

    @Test
    void testBurnTooMuch() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 2));
        BigInteger newId = mint_token(supply);

        // burn with creator
        BigInteger burn_amount = supply.add(BigInteger.ONE);
        assertThrows(AssertionError.class, () ->
                score.invoke(owner, "burn", newId, burn_amount));
    }

    @Test
    void testBurnNotOwner() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 2));
        BigInteger newId = mint_token(supply);

        // burn with eve
        BigInteger burn_amount = BigInteger.ONE;
        assertThrows(AssertionError.class, () ->
                score.invoke(eve, "burn", newId, burn_amount));
    }

    @Test
    void testBurnAfterTransferOwnership() {
        BigInteger supply = BigInteger.valueOf((int) (Math.random() * 100 + 2));
        BigInteger newId = mint_token(supply);

        // transfer ownership
        score.invoke(owner, "transferFrom",
                owner.getAddress(),
                alice.getAddress(),
                newId,
                supply,
                "test".getBytes());

        check_balance(owner, newId, BigInteger.ZERO);
        check_balance(alice, newId, supply);

        // burn with new owner
        BigInteger burn_amount = BigInteger.ONE;
        score.invoke(alice, "burn", newId, burn_amount);
        check_balance(alice, newId, supply.subtract(burn_amount));
        check_balance(owner, newId, BigInteger.ZERO);
    }
}
